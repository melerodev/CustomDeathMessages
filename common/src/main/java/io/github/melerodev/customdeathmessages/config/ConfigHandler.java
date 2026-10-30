package io.github.melerodev.customdeathmessages.config;

import io.github.melerodev.customdeathmessages.AbstractCustomDeathMessages;
import io.github.melerodev.customdeathmessages.Reloadable;
import io.github.melerodev.customdeathmessages.config.loading.ConfigLoader;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * A class that generates/loads {@literal &} provides access to a configuration file.
 */
public class ConfigHandler implements Reloadable {
    private final AbstractCustomDeathMessages plugin;
    private final Path configDir;
    private final Logger logger;

    private PluginConfig cfg;

    /**
     * Instantiates a new Config handler.
     *
     * @param plugin the plugin instance
     */
    public ConfigHandler(AbstractCustomDeathMessages plugin) {
        this.plugin = plugin;
        this.configDir = plugin.getDataFolder().toPath();
        this.logger = plugin.getComponentLogger();
    }

    public ConfigHandler(AbstractCustomDeathMessages plugin, Path configDir, Logger logger) {
        this.plugin = plugin;
        this.configDir = configDir;
        this.logger = logger;
    }

    @Override
    public void onLoad(AbstractCustomDeathMessages plugin) {
        cfg = load();
    }

    /**
     * Reloads the config from disk. If the new file is invalid the previously loaded config is kept.
     *
     * @return whether the config was reloaded successfully
     */
    public boolean reload() {
        final PluginConfig newCfg = load();
        if (newCfg == null)
            return false;

        cfg = newCfg;
        return true;
    }

    private PluginConfig load() {
        return new ConfigLoader()
            .withLogger(logger)
            .withDirectory()
            .withPath(configDir.resolve("config.yml"))
            .withHeader(PluginConfig.HEADER)
            .build(PluginConfig.class);
    }

    /**
     * Gets main config object.
     *
     * @return the config object
     */
    public PluginConfig getConfig() {
        return cfg;
    }
}
