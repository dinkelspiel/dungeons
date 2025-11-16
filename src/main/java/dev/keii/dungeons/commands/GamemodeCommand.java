package dev.keii.dungeons.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class GamemodeCommand extends Command {
    public GamemodeCommand() {
        super("gamemode");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You executed the command");
        });

        var playerArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        var gameModeArgument = ArgumentType.Enum("gameMode", GameMode.class);

        addSyntax((sender, context) -> {
            final EntityFinder finder = context.get(playerArgument);
            final GameMode gameMode = context.get(gameModeArgument);

            Player target = finder.findFirstPlayer(sender);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found or not online."));
                return;
            }
            target.setGameMode(gameMode);

            sender.sendMessage(Component.text("Set " + target.getUsername() + " to " + gameMode));
        }, gameModeArgument, playerArgument);

        addSyntax((sender, context) -> {
            final GameMode gameMode = context.get(gameModeArgument);

            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can run this without a target."));
                return;
            }

            player.setGameMode(gameMode);
            sender.sendMessage(Component.text("Set your gamemode to " + gameMode));
        }, gameModeArgument);
    }
}
