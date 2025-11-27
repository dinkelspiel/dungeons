package dev.keii.dungeons.command;

import java.util.Optional;

import dev.keii.dungeons.Dungeons;
import dev.keii.dungeons.item.api.ItemFactory;
import dev.keii.dungeons.item.api.ItemTags;
import dev.keii.dungeons.item.model.GameItemInstance;
import dev.keii.dungeons.util.MessageFormatter;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

public class ItemCommand extends Command {
    public ItemCommand() {
        super("item");

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("You executed the command");
        });

        var itemArgument = ArgumentType.Word("item");
        itemArgument.setSuggestionCallback((sender, context, suggestion) -> {
            for (String key : Dungeons.getItemRegistry().getKeys()) {
                suggestion.addEntry(new SuggestionEntry(key));
            }
        });

        addSyntax((sender, context) -> {
            final String itemQuery = context.get(itemArgument);

            Optional<GameItemInstance> instance = ItemFactory.createInstance(itemQuery);

            if (instance.isEmpty()) {
                sender.sendMessage(MessageFormatter.error("Item '{}' not found in registry.", itemQuery));
                return;
            }

            if (sender instanceof Player player) {
                ItemStack itemStack = ItemFactory.createItemStack(instance.get());

                player.getInventory().addItemStack(itemStack);
            }

            sender.sendMessage(MessageFormatter.success("Gave you item '{}'.", instance.get().getKey()));
        }, ArgumentType.Literal("give"), itemArgument);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(MessageFormatter.error("Not a player."));
                return;
            }

            ItemStack item = player.getItemInMainHand();

            if (item == null) {
                sender.sendMessage(MessageFormatter.error("No item in hand."));
                return;
            }

            String id = item.getTag(ItemTags.ID);

            if (id == null) {
                sender.sendMessage(MessageFormatter.error("Item doesn't have an id tag."));
                return;
            }

            sender.sendMessage(MessageFormatter.success("Item ID: '{}'.", id));
        }, ArgumentType.Literal("inspect"));
    }
}
