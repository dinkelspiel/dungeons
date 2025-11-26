package dev.keii.dungeons.item.api;

import java.util.function.Supplier;

import dev.keii.dungeons.common.Registry;
import dev.keii.dungeons.item.impl.ItemArcticBlast;
import dev.keii.dungeons.item.impl.ItemBombTag;
import dev.keii.dungeons.item.impl.ItemCoinBolt;
import dev.keii.dungeons.item.impl.ItemDagger;
import dev.keii.dungeons.item.impl.ItemThwomp;
import dev.keii.dungeons.item.impl.ItemTranscendentCooldown;

public class ItemRegistry extends Registry<Item> {
    public ItemRegistry() {
        register(ItemDagger::new);
        register(ItemTranscendentCooldown::new);
        register(ItemArcticBlast::new);
        register(ItemBombTag::new);
        register(ItemCoinBolt::new);
        register(ItemThwomp::new);
    }

    @Override
    public void register(Supplier<Item> factory) {
        Item item = factory.get();
        register(item.key(), factory);
    }
}
