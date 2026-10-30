package io.github.melerodev.customdeathmessages;

import io.github.melerodev.customdeathmessages.api.CustomDeathMessagesAPI;

class CustomDeathMessagesAPIProvider extends CustomDeathMessagesAPI implements Reloadable {
    private final CustomDeathMessages plugin;

    CustomDeathMessagesAPIProvider(CustomDeathMessages plugin) {
        super();
        this.plugin = plugin;
        setInstance(this);
    }
}
