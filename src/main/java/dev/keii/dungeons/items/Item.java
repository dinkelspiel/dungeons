package dev.keii.dungeons.items;

import net.minestom.server.item.Material;

public interface Item {
    String name();

    float damage();

    Material material();
}
