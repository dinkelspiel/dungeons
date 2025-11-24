package dev.keii.dungeons.command;

import dev.keii.dungeons.Dungeons;
import dev.keii.dungeons.npc.Npc;
import dev.keii.dungeons.util.MessageFormatter;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.location.RelativeVec;

public class NpcCommand extends Command {
    public NpcCommand() {
        super("npc");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You executed the command");
        });

        var npcArgument = ArgumentType.Word("npc");
        npcArgument.setSuggestionCallback((sender, context, suggestion) -> {
            for (String key : Dungeons.getNpcRegistry().getKeys()) {
                suggestion.addEntry(new SuggestionEntry(key));
            }
        });
        var positionArgument = ArgumentType.RelativeBlockPosition("position");

        addSyntax((sender, context) -> {
            final String npcQuery = context.get(npcArgument);
            final RelativeVec vector = context.get(positionArgument);

            if (!(sender instanceof Player player)) {
                sender.sendMessage(MessageFormatter.error("/npc summon must be run as player."));
                return;
            }

            final Pos pos = vector.from(player.getPosition()).asPos();

            Npc npc = Dungeons.getNpcRegistry().get(npcQuery);
            if (npc == null) {
                sender.sendMessage(MessageFormatter.error("Npc '{}' not found in registry.", npcQuery));
                return;
            }

            npc.spawn(pos);
            sender.sendMessage(MessageFormatter.success("Spawned NPC '{}'.", npc.key()));
        }, ArgumentType.Literal("summon"), npcArgument, positionArgument);
    }
}
