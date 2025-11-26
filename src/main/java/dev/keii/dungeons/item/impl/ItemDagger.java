package dev.keii.dungeons.item.impl;

import java.util.List;

import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.ItemRarity;
import dev.keii.dungeons.item.api.component.TraitComponent;
import dev.keii.dungeons.item.api.component.WeaponComponent;
import dev.keii.dungeons.item.api.component.trait.TraitMajesticLeap;
import dev.keii.dungeons.item.api.component.trait.TraitMeleeLiftsteal;
import dev.keii.dungeons.item.api.component.trait.TraitFireAspect;
import dev.keii.dungeons.item.api.component.trait.TraitSharpness;
import net.minestom.server.item.Material;

public class ItemDagger extends Item {
    @Override
    public String key() {
        return "dagger";
    }

    @Override
    public String name() {
        return "Dagger";
    }

    @Override
    public Material material() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemRarity rarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public List<ItemComponent> components() {
        return List.of(
                new WeaponComponent(10),
                new TraitComponent(List.of(new TraitSharpness(), new TraitFireAspect(), new TraitMajesticLeap(),
                        new TraitMeleeLiftsteal())));
    }
}
