package io.github.melerodev.customdeathmessages.api.event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called before CustomDeathMessages sends a death message.
 * <p>
 * Translation plugins (or any other plugin) can listen to this event to modify or cancel the message.
 * When {@code per-player-locale} is enabled the event is called once per viewer, otherwise it is called once
 * with a {@code null} viewer for the broadcast message.
 *
 * @since 1.0.0
 */
public class DeathMessageEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player victim;
    private final @Nullable Player viewer;
    private final String cause;
    private Component message;
    private boolean cancelled;

    @ApiStatus.Internal
    public DeathMessageEvent(@NotNull Player victim, @Nullable Player viewer, @NotNull String cause, @NotNull Component message) {
        this.victim = victim;
        this.viewer = viewer;
        this.cause = cause;
        this.message = message;
    }

    /**
     * @return the player who died
     */
    public @NotNull Player getVictim() {
        return victim;
    }

    /**
     * @return the player who will receive this message, or {@code null} if it is broadcast to everyone
     */
    public @Nullable Player getViewer() {
        return viewer;
    }

    /**
     * @return the key used to pick the template, e.g. {@code fall}, {@code player} or {@code mob:zombie}
     */
    public @NotNull String getCause() {
        return cause;
    }

    /**
     * @return the death message
     */
    public @NotNull Component getMessage() {
        return message;
    }

    /**
     * @param message the new death message
     */
    public void setMessage(@NotNull Component message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}
