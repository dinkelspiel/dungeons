package dev.keii.dungeons.items;

import net.minestom.server.item.Material;

public class ItemDagger implements Item {
    @Override
    public String name() {
        return "Dagger";
    }

    @Override
    public float damage() {
        return 20;
    }

    @Override
    public Material material() {
        return Material.IRON_SWORD;
    }
}
