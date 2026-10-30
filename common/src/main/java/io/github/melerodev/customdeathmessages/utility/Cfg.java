package io.github.melerodev.customdeathmessages.utility;

import io.github.melerodev.customdeathmessages.AbstractCustomDeathMessages;
import io.github.melerodev.customdeathmessages.config.ConfigHandler;
import io.github.melerodev.customdeathmessages.config.PluginConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Convenience class for accessing {@link ConfigHandler#getConfig}
 */
public final class Cfg {
    /**
     * Convenience method for {@link ConfigHandler#getConfig} to getConnection {@link PluginConfig}
     *
     * @return the config
     */
    @NotNull
    public static PluginConfig get() {
        return AbstractCustomDeathMessages.getInstance().getConfigHandler().getConfig();
    }
}
