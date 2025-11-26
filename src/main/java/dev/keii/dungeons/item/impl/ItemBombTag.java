package dev.keii.dungeons.item.impl;

import java.util.List;

import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.ItemRarity;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.trait.TraitBombTag;
import net.minestom.server.item.Material;

public class ItemBombTag extends Item {
    @Override
    public String key() {
        return "bombtag";
    }

    @Override
    public String name() {
        return "Bomb Tag";
    }

    @Override
    public List<ItemComponent> components() {
        return List.of(new TraitComponent(List.of(new TraitBombTag())));
    }

    @Override
    public Material material() {
        return Material.TNT;
    }

    @Override
    public ItemRarity rarity() {
        return ItemRarity.COMMON;
    }

}
