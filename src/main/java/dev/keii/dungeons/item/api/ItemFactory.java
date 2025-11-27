package dev.keii.dungeons.item.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.keii.dungeons.Dungeons;
import dev.keii.dungeons.item.dao.GameItemInstanceDao;
import dev.keii.dungeons.item.model.GameItemInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;

public final class ItemFactory {
    public static Optional<GameItemInstance> createInstance(String definitionKey) {
        GameItemInstanceDao dao = new GameItemInstanceDao();
        Item item = Dungeons.getItemRegistry().get(definitionKey.toLowerCase());

        if (item == null) {
            return Optional.empty();
        }

        GameItemInstance instance = new GameItemInstance(definitionKey.toLowerCase());
        instance = dao.save(instance);

        return Optional.of(instance);
    }

    /**
     * Shorthand to get itemstack as GameItemInstance
     * 
     * @param itemStack
     * @return
     */
    public static Optional<GameItemInstance> fromItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }
        String itemId = itemStack.getTag(ItemTags.ID);
        if (itemId == null) {
            return Optional.empty();
        }

        // Get the actual referenced item
        GameItemInstanceDao dao = new GameItemInstanceDao();
        GameItemInstance gameItemInstance = dao.findById(UUID.fromString(itemId));
        if (gameItemInstance == null) {
            return Optional.empty();
        }
        return Optional.of(gameItemInstance);
    }

    public static ItemStack createItemStack(GameItemInstance instance) {
        Item item = instance.getItem();

        List<Component> lore = new ArrayList<>();

        for (ItemComponent component : item.components()) {
            component.onBuildLore(null, lore);
            lore.add(Component.text(""));
        }

        ItemStack itemStack = ItemStack.builder(item.material())
                .customName(Component.text(item.name()).decoration(TextDecoration.ITALIC, false))
                .lore(lore)
                .set(ItemTags.ID, instance.getId().toString())
                .build();

        return itemStack;
    }

}
