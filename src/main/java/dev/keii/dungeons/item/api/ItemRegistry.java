package dev.keii.dungeons.item.api;

import java.util.function.Supplier;

import dev.keii.dungeons.common.Registry;
import dev.keii.dungeons.item.impl.ItemDagger;

public class ItemRegistry extends Registry<Item> {
    public ItemRegistry() {
        register(ItemDagger::new);
    }

    @Override
    public void register(Supplier<Item> factory) {
        Item item = factory.get();
        register(item.key(), factory);
    }
}
