package io.github.melerodev.customdeathmessages.config;

import io.github.melerodev.customdeathmessages.config.exception.ConfigValidationException;
import io.github.melerodev.customdeathmessages.config.migration.Migration;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.interfaces.meta.Exclude;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class PluginConfig implements VersionedConfig {
    public static final String HEADER = """
        CustomDeathMessages configuration
        
        All messages support MiniMessage (https://docs.advntr.dev/minimessage/format),
        legacy color codes (&a, &l...), PlaceholderAPI (%placeholder%) and MiniPlaceholders.
        Translation keys work with the MiniMessage tag <lang:key>, e.g. <lang:death.attack.fall:'<victim>'>.
        
        Placeholders:
          <victim>        Victim name                  <victim_displayname> Victim display name
          <killer>        Killer name (translated)     <killer_type>        Killer entity type (translated)
          <weapon>        Killer's item, with hover    <cause>              Damage cause (e.g. fall)
          <world> <x> <y> <z>  Death location          <original>           The vanilla death message (translated)
        
        If several messages are listed for a key, one is picked at random.""";

    @Comment("Do not change this value!")
    public int configVersion = 1;

    @Override
    @Exclude
    public int configVersion() {
        return configVersion;
    }

    @Override
    @Exclude
    public @NotNull Map<Integer, Migration> migrations() {
        return Map.of();
    }

    @Override
    @Exclude
    public void validate() throws ConfigValidationException {
        if (messages == null || mobMessages == null || locales == null)
            throw new ConfigValidationException("`messages`, `mob-messages` and `locales` cannot be null");
    }

    public enum Mode {
        /**
         * Replace death messages with the configured templates.
         */
        CUSTOM,
        /**
         * Do not touch death messages.
         */
        VANILLA,
        /**
         * Hide every death message.
         */
        HIDDEN
    }

    @Comment("custom = use the messages below, vanilla = keep Minecraft's messages, hidden = no death messages")
    public Mode mode = Mode.CUSTOM;

    @Comment("""
        Send each player the message in their own client language using the `locales` section.
        When disabled, everyone receives the messages from `messages`/`mob-messages`.
        Translatable parts (<killer>, <killer_type>, <weapon>, <original> and <lang:...> tags) are always
        shown in each player's language, and are compatible with translation plugins that use Adventure's GlobalTranslator.""")
    public boolean perPlayerLocale = false;

    @Comment("Text used for <weapon> when the killer has nothing in their hand.")
    public String emptyHand = "<white>their fists";

    @Comment("Keep the vanilla message when no template matches (otherwise the death is silent).")
    public boolean fallbackToVanilla = true;

    @Comment("""
        Messages by cause. Special keys: `player` (killed by a player), `mob` (killed by any other entity)
        and `default` (anything without its own key). Other keys are damage causes:
        https://jd.papermc.io/paper/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html (lowercase)""")
    public Map<String, List<String>> messages = defaultMessages();

    @Comment("Messages by killer entity type (lowercase, e.g. zombie, creeper, warden). Takes priority over `mob`.")
    public Map<String, List<String>> mobMessages = defaultMobMessages();

    @Comment("""
        Per-language messages, used when `per-player-locale` is true. The key is the client locale (e.g. es_es, en_us)
        or only the language (e.g. es). Missing keys fall back to `messages`/`mob-messages`.""")
    public Map<String, LocaleMessages> locales = new LinkedHashMap<>(Map.of("es", LocaleMessages.spanish()));

    @ConfigSerializable
    public static class LocaleMessages {
        public Map<String, List<String>> messages = new LinkedHashMap<>();
        public Map<String, List<String>> mobMessages = new LinkedHashMap<>();

        static LocaleMessages spanish() {
            final LocaleMessages locale = new LocaleMessages();
            locale.messages.put("player", List.of("<red>☠ <white><victim> <gray>fue asesinado por <white><killer> <gray>usando <weapon>"));
            locale.messages.put("mob", List.of("<red>☠ <white><victim> <gray>fue asesinado por <white><killer>"));
            locale.messages.put("fall", List.of("<red>☠ <white><victim> <gray>olvidó que no sabe volar", "<red>☠ <white><victim> <gray>se cayó desde demasiado alto"));
            locale.messages.put("lava", List.of("<red>☠ <white><victim> <gray>intentó nadar en lava"));
            locale.messages.put("drowning", List.of("<red>☠ <white><victim> <gray>se ahogó"));
            locale.messages.put("void", List.of("<red>☠ <white><victim> <gray>cayó al vacío"));
            locale.messages.put("default", List.of("<red>☠ <white><victim> <gray>murió"));
            locale.mobMessages.put("creeper", List.of("<red>☠ <white><victim> <gray>abrazó a un <green>creeper"));
            return locale;
        }
    }

    private static Map<String, List<String>> defaultMessages() {
        final Map<String, List<String>> m = new LinkedHashMap<>();
        m.put("player", List.of(
            "<red>☠ <white><victim> <gray>was slain by <white><killer> <gray>using <weapon>",
            "<red>☠ <white><killer> <gray>sent <white><victim> <gray>back to spawn with <weapon>"
        ));
        m.put("mob", List.of("<red>☠ <white><victim> <gray>was killed by <white><killer>"));
        m.put("fall", List.of("<red>☠ <white><victim> <gray>forgot they can't fly", "<red>☠ <white><victim> <gray>hit the ground too hard"));
        m.put("lava", List.of("<red>☠ <white><victim> <gray>tried to swim in lava"));
        m.put("fire", List.of("<red>☠ <white><victim> <gray>went up in flames"));
        m.put("fire_tick", List.of("<red>☠ <white><victim> <gray>burned to death"));
        m.put("drowning", List.of("<red>☠ <white><victim> <gray>drowned"));
        m.put("suffocation", List.of("<red>☠ <white><victim> <gray>suffocated in a wall"));
        m.put("void", List.of("<red>☠ <white><victim> <gray>fell out of the world"));
        m.put("starvation", List.of("<red>☠ <white><victim> <gray>starved to death"));
        m.put("lightning", List.of("<red>☠ <white><victim> <gray>was struck by lightning"));
        m.put("block_explosion", List.of("<red>☠ <white><victim> <gray>blew up"));
        m.put("entity_explosion", List.of("<red>☠ <white><victim> <gray>was blown up by <white><killer>"));
        m.put("magic", List.of("<red>☠ <white><victim> <gray>was killed by magic"));
        m.put("wither", List.of("<red>☠ <white><victim> <gray>withered away"));
        m.put("freeze", List.of("<red>☠ <white><victim> <gray>froze to death"));
        m.put("contact", List.of("<red>☠ <white><victim> <gray>was pricked to death"));
        m.put("hot_floor", List.of("<red>☠ <white><victim> <gray>discovered the floor was lava"));
        m.put("fly_into_wall", List.of("<red>☠ <white><victim> <gray>experienced kinetic energy"));
        m.put("falling_block", List.of("<red>☠ <white><victim> <gray>was squashed by a falling block"));
        m.put("cramming", List.of("<red>☠ <white><victim> <gray>was squished too much"));
        m.put("world_border", List.of("<red>☠ <white><victim> <gray>left the confines of this world"));
        m.put("kill", List.of("<red>☠ <white><victim> <gray>was killed"));
        m.put("default", List.of("<red>☠ <white><victim> <gray>died"));
        return m;
    }

    private static Map<String, List<String>> defaultMobMessages() {
        final Map<String, List<String>> m = new LinkedHashMap<>();
        m.put("zombie", List.of("<red>☠ <white><victim> <gray>became a snack for a <dark_green>zombie"));
        m.put("skeleton", List.of("<red>☠ <white><victim> <gray>was sniped by a <white>skeleton"));
        m.put("creeper", List.of("<red>☠ <white><victim> <gray>hugged a <green>creeper"));
        m.put("spider", List.of("<red>☠ <white><victim> <gray>got tangled by a <dark_red>spider"));
        m.put("enderman", List.of("<red>☠ <white><victim> <gray>looked an <dark_purple>enderman <gray>in the eyes"));
        m.put("warden", List.of("<red>☠ <white><victim> <gray>was too loud near the <dark_aqua>warden"));
        return m;
    }

    @Comment("Update Checker Settings")
    public UpdateChecker updateChecker = new UpdateChecker();

    @ConfigSerializable
    public static class UpdateChecker {
        @Comment("Should the plugin check for plugin updates on startup?")
        public boolean enabled = true;

        @Comment("Send update notifications to the console?")
        public boolean console = true;

        @Comment("Send update notifications to opped players on join?")
        public boolean op = true;
    }

    @Comment("Language, specify the language file to use, for example `en_US` or `es_ES` which will load `/lang/<language>.json`")
    public String language = "en_US";
}
