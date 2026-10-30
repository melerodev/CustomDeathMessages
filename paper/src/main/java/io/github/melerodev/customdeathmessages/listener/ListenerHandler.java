package io.github.melerodev.customdeathmessages.listener;

import io.github.melerodev.customdeathmessages.AbstractCustomDeathMessages;
import io.github.melerodev.customdeathmessages.CustomDeathMessages;
import io.github.melerodev.customdeathmessages.Reloadable;
import io.github.melerodev.customdeathmessages.death.DeathMessageService;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to handle registration of event listeners.
 */
public class ListenerHandler implements Reloadable {
    private final CustomDeathMessages plugin;
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Instantiates a the Listener handler.
     *
     * @param plugin the plugin instance
     */
    public ListenerHandler(CustomDeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onEnable(AbstractCustomDeathMessages plugin) {
        listeners.clear(); // Clear the list to avoid duplicate listeners when reloading the plugin
        listeners.add(new DeathListener(new DeathMessageService()));

        // Register listeners here
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public void onDisable(AbstractCustomDeathMessages plugin) {
        listeners.forEach(HandlerList::unregisterAll);
        listeners.clear();
    }
}
