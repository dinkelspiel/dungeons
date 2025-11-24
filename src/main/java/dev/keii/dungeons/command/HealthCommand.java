package dev.keii.dungeons.command;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class HealthCommand extends Command {
    public HealthCommand() {
        super("health");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You executed the command");
        });

        var playerArgument = ArgumentType.String("player");
        var healthArgument = ArgumentType.Integer("health");

        addSyntax((sender, context) -> {
            final String playerName = context.get(playerArgument);
            final int health = context.get(healthArgument);

            Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(playerName);
            assert player != null;
            player.setHealth(health);

            sender.sendMessage(Component.text("Set health of player to " + health));
        }, playerArgument, healthArgument);
    }
}
