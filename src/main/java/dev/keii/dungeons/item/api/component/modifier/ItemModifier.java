package dev.keii.dungeons.item.api.component.modifier;

import dev.keii.dungeons.item.context.ItemUseContext;

public interface ItemModifier {
    String key();

    String name();

    default void onUse(ItemUseContext ctx) {
    }
}
