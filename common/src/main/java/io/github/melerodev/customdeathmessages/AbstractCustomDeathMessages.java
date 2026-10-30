package io.github.melerodev.customdeathmessages;

import io.github.melerodev.customdeathmessages.config.ConfigHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCustomDeathMessages extends JavaPlugin {
    private static AbstractCustomDeathMessages instance;

    /**
     * Gets plugin instance.
     *
     * @return the plugin instance
     */
    public static AbstractCustomDeathMessages getInstance() {
        return AbstractCustomDeathMessages.instance;
    }

    AbstractCustomDeathMessages() {
        AbstractCustomDeathMessages.instance = this;
    }

    /**
     * Gets config handler.
     *
     * @return the config handler
     */
    public abstract @NotNull ConfigHandler getConfigHandler();
}
