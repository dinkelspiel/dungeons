package dev.keii.dungeons.item.impl;

import java.util.List;

import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.ItemRarity;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.trait.TraitThwomp;
import net.minestom.server.item.Material;

public class ItemThwomp extends Item {
    @Override
    public String key() {
        return "thwomp";
    }

    @Override
    public String name() {
        return "Thwomp";
    }

    @Override
    public List<ItemComponent> components() {
        return List.of(new TraitComponent(List.of(new TraitThwomp())));
    }

    @Override
    public Material material() {
        return Material.ANVIL;
    }

    @Override
    public ItemRarity rarity() {
        return ItemRarity.COMMON;
    }

}
