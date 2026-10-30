package io.github.melerodev.customdeathmessages.command;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import io.github.melerodev.customdeathmessages.CustomDeathMessages;
import io.github.melerodev.customdeathmessages.utility.Cfg;
import io.github.milkdrinkers.colorparser.paper.ColorParser;
import io.github.melerodev.customdeathmessages.translation.Translation;
import org.bukkit.command.CommandSender;

import static io.github.melerodev.customdeathmessages.command.CommandHandler.BASE_PERM;

/**
 * The /customdeathmessages command.
 */
final class DeathMessagesCommand extends Command {
    private final CustomDeathMessages plugin;

    DeathMessagesCommand(CustomDeathMessages plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandAPICommand command() {
        return new CommandAPICommand("customdeathmessages")
            .withHelp("CustomDeathMessages commands.", "CustomDeathMessages commands.")
            .withPermission(BASE_PERM)
            .withSubcommands(
                new CommandAPICommand("reload")
                    .withPermission(BASE_PERM + ".reload")
                    .executes(this::executorReload)
            )
            .executes(this::executorInfo);
    }

    private void executorInfo(CommandSender sender, CommandArguments args) {
        sender.sendMessage(ColorParser.of(Translation.of("commands.info"))
            .with("mode", Cfg.get().mode.name().toLowerCase())
            .with("templates", String.valueOf(Cfg.get().messages.size() + Cfg.get().mobMessages.size()))
            .with("locales", String.join(", ", Cfg.get().locales.keySet()))
            .build());
    }

    private void executorReload(CommandSender sender, CommandArguments args) {
        sender.sendMessage(Translation.as(plugin.reloadConfiguration() ? "commands.reload.success" : "commands.reload.failure"));
    }
}
