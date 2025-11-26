package dev.keii.dungeons.item.impl;

import java.util.List;

import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.ItemRarity;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.trait.TraitArcticBlast;
import net.minestom.server.item.Material;

public class ItemArcticBlast extends Item {
    @Override
    public String key() {
        return "arcticblast";
    }

    @Override
    public String name() {
        return "Arctic Blast";
    }

    @Override
    public List<ItemComponent> components() {
        return List.of(new TraitComponent(List.of(new TraitArcticBlast())));
    }

    @Override
    public Material material() {
        return Material.ICE;
    }

    @Override
    public ItemRarity rarity() {
        return ItemRarity.COMMON;
    }

}
