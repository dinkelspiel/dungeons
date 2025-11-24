package dev.keii.dungeons.item.api;

import java.util.List;

import dev.keii.dungeons.item.context.ItemAttackContext;
import dev.keii.dungeons.item.context.ItemBuildLoreContext;
import dev.keii.dungeons.item.context.ItemCraftContext;
import dev.keii.dungeons.item.context.ItemUseContext;
import net.kyori.adventure.text.Component;

public interface ItemComponent {
    default void onBuildLore(ItemBuildLoreContext ctx, List<Component> lore) {
    }

    default void onAttack(ItemAttackContext ctx) {
    }

    default void onUse(ItemUseContext ctx) {
    }

    default void onCraft(ItemCraftContext ctx) {
    }
}