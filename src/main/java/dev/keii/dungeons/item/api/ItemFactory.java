package dev.keii.dungeons.item.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.keii.dungeons.Dungeons;
import dev.keii.dungeons.item.dao.GameItemInstanceDao;
import dev.keii.dungeons.item.model.GameItemInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.ItemStack;

public final class ItemFactory {
    GameItemInstanceDao dao;

    public ItemFactory() {
        this.dao = new GameItemInstanceDao();
    }

    public Optional<GameItemInstance> createInstance(String definitionKey) {
        Item item = Dungeons.getItemRegistry().get(definitionKey.toLowerCase());

        if (item == null) {
            return Optional.empty();
        }

        GameItemInstance instance = new GameItemInstance(definitionKey.toLowerCase());
        instance = dao.save(instance);

        return Optional.of(instance);
    }

    public ItemStack createItemStack(GameItemInstance instance) {
        Item item = instance.getItem();

        List<Component> lore = new ArrayList<>();

        for (ItemComponent component : item.components()) {
            component.onBuildLore(null, lore);
        }

        ItemStack itemStack = ItemStack.builder(item.material())
                .customName(Component.text(item.name()).decoration(TextDecoration.ITALIC, false))
                .lore(lore)
                .set(ItemTags.ID, instance.getId().toString())
                .build();

        return itemStack;
    }

}
