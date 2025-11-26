package dev.keii.dungeons.item.impl;

import java.util.List;

import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.ItemRarity;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.trait.TraitTranscendentCooldown;
import net.minestom.server.item.Material;

public class ItemTranscendentCooldown extends Item {
    @Override
    public String key() {
        return "transcendentcooldown";
    }

    @Override
    public String name() {
        return "Transcendent Cooldown";
    }

    @Override
    public List<ItemComponent> components() {
        return List.of(new TraitComponent(List.of(new TraitTranscendentCooldown())));
    }

    @Override
    public Material material() {
        return Material.CLOCK;
    }

    @Override
    public ItemRarity rarity() {
        return ItemRarity.COMMON;
    }

}
