package io.github.melerodev.customdeathmessages.utility;


import io.github.melerodev.customdeathmessages.AbstractCustomDeathMessages;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

/**
 * A class that provides shorthand access to {@link AbstractCustomDeathMessages#getComponentLogger}.
 */
public class Logger {
    /**
     * Get component logger. Shorthand for:
     *
     * @return the component logger {@link AbstractCustomDeathMessages#getComponentLogger}.
     */
    @NotNull
    public static ComponentLogger get() {
        return AbstractCustomDeathMessages.getInstance().getComponentLogger();
    }
}
