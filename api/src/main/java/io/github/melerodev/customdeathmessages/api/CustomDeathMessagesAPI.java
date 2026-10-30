package io.github.melerodev.customdeathmessages.api;

import org.jetbrains.annotations.ApiStatus;

/**
 * The CustomDeathMessagesAPI class is the main entry point for accessing the CustomDeathMessages API.
 */
public abstract class CustomDeathMessagesAPI {
    private static CustomDeathMessagesAPI INSTANCE;

    /**
     * Gets the instance of the CustomDeathMessagesAPI.
     *
     * @return the instance of CustomDeathMessagesAPI
     * @since 1.0.0
     */
    public static CustomDeathMessagesAPI getInstance() {
        if (INSTANCE == null)
            throw new RuntimeException("API was accessed before being initialized!");
        return INSTANCE;
    }

    /**
     * Sets the instance of the CustomDeathMessagesAPI.
     * This method is intended for internal use by the api provider only.
     *
     * @param api the instance of CustomDeathMessagesAPI to set
     * @since 1.0.0
     */
    @ApiStatus.Internal
    protected static void setInstance(CustomDeathMessagesAPI api) {
        INSTANCE = api;
    }
}
