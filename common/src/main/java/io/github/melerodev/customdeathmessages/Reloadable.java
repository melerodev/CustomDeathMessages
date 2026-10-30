package io.github.melerodev.customdeathmessages;

/**
 * Implemented in classes that should support being reloaded IE executing the methods during runtime after startup.
 */
public interface Reloadable {
    /**
     * On plugin load.
     */
    default void onLoad(AbstractCustomDeathMessages plugin) {
    }

    /**
     * On plugin enable.
     */
    default void onEnable(AbstractCustomDeathMessages plugin) {
    }

    /**
     * On plugin disable.
     */
    default void onDisable(AbstractCustomDeathMessages plugin) {
    }

}
