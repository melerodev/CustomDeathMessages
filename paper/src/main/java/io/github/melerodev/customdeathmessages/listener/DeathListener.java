package io.github.melerodev.customdeathmessages.listener;

import io.github.melerodev.customdeathmessages.death.DeathMessageService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Replaces player death messages.
 */
public final class DeathListener implements Listener {
    private final DeathMessageService service;

    public DeathListener(DeathMessageService service) {
        this.service = service;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {
        service.handle(e);
    }
}
