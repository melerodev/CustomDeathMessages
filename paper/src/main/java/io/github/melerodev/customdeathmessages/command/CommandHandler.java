package io.github.melerodev.customdeathmessages.command;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIPaperConfig;
import io.github.melerodev.customdeathmessages.AbstractCustomDeathMessages;
import io.github.melerodev.customdeathmessages.CustomDeathMessages;
import io.github.melerodev.customdeathmessages.Reloadable;

/**
 * A class to handle registration of commands.
 */
public class CommandHandler implements Reloadable {
    public static final String BASE_PERM = "customdeathmessages.command";
    private final CustomDeathMessages plugin;

    /**
     * Instantiates the Command handler.
     *
     * @param plugin the plugin
     */
    public CommandHandler(CustomDeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onLoad(AbstractCustomDeathMessages plugin) {
        CommandAPI.onLoad(
            new CommandAPIPaperConfig(plugin)
                .silentLogs(true)
        );
    }

    @Override
    public void onEnable(AbstractCustomDeathMessages plugin) {
        if (!CommandAPI.isLoaded())
            return;

        CommandAPI.onEnable();

        // Register commands here
        new DeathMessagesCommand(this.plugin)
            .command()
            .withAliases("cdm", "deathmessages")
            .register();
    }

    @Override
    public void onDisable(AbstractCustomDeathMessages plugin) {
        if (!CommandAPI.isLoaded())
            return;

        CommandAPI.onDisable();
    }
}
