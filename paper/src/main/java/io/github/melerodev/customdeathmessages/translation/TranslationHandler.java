package io.github.melerodev.customdeathmessages.translation;

import io.github.melerodev.customdeathmessages.AbstractCustomDeathMessages;
import io.github.melerodev.customdeathmessages.Reloadable;
import io.github.melerodev.customdeathmessages.config.ConfigHandler;

/**
 * A wrapper handler class for handling the {@link Translation} lifecycle.
 */
public class TranslationHandler implements Reloadable {
    private final ConfigHandler configHandler;

    public TranslationHandler(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public void onEnable(AbstractCustomDeathMessages plugin) {
        Translation.initialize(plugin, configHandler.getConfig().language);
    }
}
