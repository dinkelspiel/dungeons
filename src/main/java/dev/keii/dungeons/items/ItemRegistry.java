package dev.keii.dungeons.items;

import java.util.function.Supplier;

public class ItemRegistry {
    public ItemRegistry() {
        register("dagger", ItemDagger::new);
    }

    public void register(String key, Supplier<ItemDagger> factory) {

    }
}
