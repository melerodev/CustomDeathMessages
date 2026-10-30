package io.github.melerodev.customdeathmessages.hook.placeholderapi;

import io.github.melerodev.customdeathmessages.CustomDeathMessages;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * PlaceholderAPI expansion: {@code %customdeathmessages_deaths%}.
 */
public class PAPIExpansion extends PlaceholderExpansion {
    private final CustomDeathMessages plugin;

    public PAPIExpansion(CustomDeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getIdentifier() {
        return plugin.getPluginMeta().getName().replace(' ', '_').toLowerCase();
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getPluginMeta().getAuthors());
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // This needs to be true, or PlaceholderAPI will unregister the expansion during a plugin reload.
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer p, @NotNull String params) {
        if (p == null)
            return null;

        return switch (params) {
            case "deaths" -> String.valueOf(p.getStatistic(Statistic.DEATHS));
            default -> null;
        };
    }
}
