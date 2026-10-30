package io.github.melerodev.customdeathmessages.hook.placeholderapi;

import io.github.melerodev.customdeathmessages.AbstractCustomDeathMessages;
import io.github.melerodev.customdeathmessages.CustomDeathMessages;
import io.github.melerodev.customdeathmessages.hook.AbstractHook;
import io.github.melerodev.customdeathmessages.hook.Hook;

/**
 * A hook to interface with <a href="https://wiki.placeholderapi.com/">PlaceholderAPI</a>.
 */
public class PAPIHook extends AbstractHook {
    private PAPIExpansion PAPIExpansion;

    /**
     * Instantiates a new PlaceholderAPI hook.
     *
     * @param plugin the plugin instance
     */
    public PAPIHook(CustomDeathMessages plugin) {
        super(plugin);
    }

    @Override
    public void onEnable(AbstractCustomDeathMessages plugin) {
        if (!isHookLoaded())
            return;

        PAPIExpansion = new PAPIExpansion(super.getPlugin());
        PAPIExpansion.register();
    }

    @Override
    public void onDisable(AbstractCustomDeathMessages plugin) {
        if (PAPIExpansion == null)
            return;

        PAPIExpansion.unregister();
        PAPIExpansion = null;
    }

    @Override
    public boolean isHookLoaded() {
        return isPluginPresent(Hook.PAPI.getPluginName()) && isPluginEnabled(Hook.PAPI.getPluginName());
    }
}
