package dev.keii.dungeons.item.impl;

import java.util.List;

import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.ItemRarity;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.trait.TraitCoinBolt;
import net.minestom.server.item.Material;

public class ItemCoinBolt extends Item {
    @Override
    public String key() {
        return "coin_bolt";
    }

    @Override
    public String name() {
        return "Coin Bolt";
    }

    @Override
    public List<ItemComponent> components() {
        return List.of(new TraitComponent(List.of(new TraitCoinBolt())));
    }

    @Override
    public Material material() {
        return Material.GOLD_BLOCK;
    }

    @Override
    public ItemRarity rarity() {
        return ItemRarity.COMMON;
    }

}
