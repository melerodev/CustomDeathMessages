package io.github.melerodev.customdeathmessages.death;

import io.github.melerodev.customdeathmessages.api.event.DeathMessageEvent;
import io.github.melerodev.customdeathmessages.config.PluginConfig;
import io.github.melerodev.customdeathmessages.utility.Cfg;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Picks and sends death messages.
 */
public final class DeathMessageService {

    /**
     * A template list and the key it was found under.
     */
    private record Templates(@NotNull String key, @NotNull List<String> lines) {
    }

    /**
     * Handle a death event according to the config.
     *
     * @param event the event
     */
    public void handle(@NotNull PlayerDeathEvent event) {
        final PluginConfig cfg = Cfg.get();

        switch (cfg.mode) {
            case VANILLA -> {
                return;
            }
            case HIDDEN -> {
                event.deathMessage(null);
                return;
            }
            case CUSTOM -> {
            }
        }

        // Respect the showDeathMessages gamerule and other plugins hiding the message
        if (event.deathMessage() == null)
            return;

        final DeathContext ctx = DeathContext.of(event);
        final Templates templates = find(ctx, cfg.messages, cfg.mobMessages);

        if (templates == null) {
            if (!cfg.fallbackToVanilla)
                event.deathMessage(null);
            return;
        }

        final int choice = ThreadLocalRandom.current().nextInt(templates.lines().size());

        if (!cfg.perPlayerLocale) {
            final DeathMessageEvent apiEvent = fire(ctx, null, templates.key(), ctx.format(templates.lines().get(choice), null));
            event.deathMessage(apiEvent.isCancelled() ? null : apiEvent.getMessage());
            return;
        }

        // Per-player locale: suppress the broadcast and send everyone their own translation
        event.deathMessage(null);
        for (Player viewer : Bukkit.getOnlinePlayers()) {
            final Templates localized = localized(ctx, viewer, cfg, templates);
            final String line = localized.lines().get(choice % localized.lines().size());
            final DeathMessageEvent apiEvent = fire(ctx, viewer, localized.key(), ctx.format(line, viewer));
            if (!apiEvent.isCancelled())
                viewer.sendMessage(apiEvent.getMessage());
        }

        Bukkit.getConsoleSender().sendMessage(ctx.format(templates.lines().get(choice), null));
    }

    private static Templates localized(DeathContext ctx, Player viewer, PluginConfig cfg, Templates fallback) {
        final Locale locale = viewer.locale();
        PluginConfig.LocaleMessages messages = cfg.locales.get(locale.toString().toLowerCase(Locale.ROOT));
        if (messages == null)
            messages = cfg.locales.get(locale.getLanguage().toLowerCase(Locale.ROOT));
        if (messages == null)
            return fallback;

        final Templates found = find(ctx, messages.messages, messages.mobMessages);
        // Only use the localized templates if they are as specific as the default ones
        return found != null && found.key().equals(fallback.key()) ? found : fallback;
    }

    /**
     * Find the most specific templates for a death: entity type, then player/mob, then cause, then default.
     */
    private static @Nullable Templates find(DeathContext ctx, Map<String, List<String>> messages, Map<String, List<String>> mobMessages) {
        final String killerType = ctx.killerType();

        if (ctx.killer() instanceof Player) {
            final Templates t = get(messages, "player", "player");
            if (t != null)
                return t;
        } else if (killerType != null) {
            final Templates t = get(mobMessages, killerType, "mob:" + killerType);
            if (t != null)
                return t;
        }

        final Templates byCause = get(messages, ctx.cause(), ctx.cause());
        if (byCause != null)
            return byCause;

        if (killerType != null && !(ctx.killer() instanceof Player)) {
            final Templates t = get(messages, "mob", "mob");
            if (t != null)
                return t;
        }

        return get(messages, "default", "default");
    }

    private static @Nullable Templates get(Map<String, List<String>> map, String lookup, String key) {
        final List<String> lines = map.get(lookup);
        return lines == null || lines.isEmpty() ? null : new Templates(key, lines);
    }

    private static DeathMessageEvent fire(DeathContext ctx, @Nullable Player viewer, String key, Component message) {
        final DeathMessageEvent event = new DeathMessageEvent(ctx.victim(), viewer, key, message);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
