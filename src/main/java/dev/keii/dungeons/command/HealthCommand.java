package dev.keii.dungeons.command;

import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.actor.component.HealthComponent;
import dev.keii.dungeons.util.MessageFormatter;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

public class HealthCommand extends Command {
    public HealthCommand() {
        super("health");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You executed the command");
        });

        var playerArgument = ArgumentType.Entity("player").onlyPlayers(true).singleEntity(true);
        var healthArgument = ArgumentType.Integer("health");

        addSyntax((sender, context) -> {
            final EntityFinder finder = context.get(playerArgument);
            final int health = context.get(healthArgument);

            Player player = finder.findFirstPlayer(sender);

            if (player == null) {
                sender.sendMessage(MessageFormatter.error("Player not found or not online."));
                return;
            }

            if (!(player instanceof ActorHolder actor)) {
                sender.sendMessage(MessageFormatter.error("Player is not an actor."));
                return;
            }
            HealthComponent healthComponent = actor.getActor().getComponent(HealthComponent.class);
            if (healthComponent == null) {
                sender.sendMessage(MessageFormatter.error("Player does not have a health component."));
                return;
            }
            healthComponent.setHealth(health);

            sender.sendMessage(MessageFormatter.success("Set player health to {}.", health));
        }, playerArgument, healthArgument);
    }
}
