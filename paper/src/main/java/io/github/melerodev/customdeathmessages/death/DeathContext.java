package io.github.melerodev.customdeathmessages.death;

import io.github.melerodev.customdeathmessages.utility.Cfg;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * Everything known about a death, used to pick and fill a template.
 *
 * @param victim   the player who died
 * @param killer   the entity responsible for the death, if any
 * @param cause    the lowercase damage cause
 * @param original the vanilla death message
 */
public record DeathContext(@NotNull Player victim, @Nullable Entity killer, @NotNull String cause, @Nullable Component original) {

    /**
     * Build the context from a death event.
     *
     * @param event the event
     * @return the context
     */
    public static @NotNull DeathContext of(@NotNull PlayerDeathEvent event) {
        final Player victim = event.getPlayer();
        final DamageSource source = event.getDamageSource();

        Entity killer = victim.getKiller();
        if (killer == null)
            killer = source.getCausingEntity();
        if (killer == null)
            killer = source.getDirectEntity();
        if (killer != null && killer.equals(victim))
            killer = null;

        final EntityDamageEvent lastDamage = victim.getLastDamageCause();
        final String cause = lastDamage == null ? "default" : lastDamage.getCause().name().toLowerCase(Locale.ROOT);

        return new DeathContext(victim, killer, cause, event.deathMessage());
    }

    /**
     * @return the lowercase entity type of the killer, or null if there is no killer
     */
    public @Nullable String killerType() {
        return killer == null ? null : killer.getType().name().toLowerCase(Locale.ROOT);
    }

    /**
     * Fill a template with this death's placeholders.
     *
     * @param template the template
     * @param viewer   the player who will see it, used for MiniPlaceholders
     * @return the parsed message
     */
    public @NotNull Component format(@NotNull String template, @Nullable Player viewer) {
        final Location loc = victim.getLocation();

        final var parser = ColorParser.of(template)
            .legacy()
            .with("victim", victim.getName())
            .with("player", victim.getName())
            .with("victim_displayname", victim.displayName())
            .with("killer", killerName())
            .with("killer_type", killer == null ? Component.empty() : Component.translatable(killer.getType().translationKey()))
            .with("weapon", weapon())
            .with("cause", cause)
            .with("world", victim.getWorld().getName())
            .with("x", String.valueOf(loc.getBlockX()))
            .with("y", String.valueOf(loc.getBlockY()))
            .with("z", String.valueOf(loc.getBlockZ()))
            .with("original", original == null ? Component.empty() : original)
            .papi(victim);

        if (viewer != null)
            parser.mini(viewer);

        return parser.build();
    }

    private Component killerName() {
        if (killer == null)
            return Component.empty();

        if (killer instanceof Player player)
            return player.displayName();

        return killer.name(); // Custom name, or a translatable entity name rendered in each client's language
    }

    private Component weapon() {
        if (killer instanceof LivingEntity living) {
            final EntityEquipment equipment = living.getEquipment();
            if (equipment != null) {
                final ItemStack item = equipment.getItemInMainHand();
                if (!item.getType().isAir())
                    return item.displayName(); // Translatable item name with hover tooltip
            }
        }

        return ColorParser.of(Cfg.get().emptyHand).build();
    }
}
