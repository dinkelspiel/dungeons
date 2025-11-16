package dev.keii.dungeons.player;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Entity;

public class EntityContext {
    private Entity parent;

    @Getter
    private int health = 100;

    @Getter
    private int maxHealth = 100;

    public EntityContext(Entity parent) {
        this.parent = parent;
    }

    public Component getActionBar() {
        return Component.text(String.format("♥ %s/%s", health, maxHealth), NamedTextColor.RED);
    }
}
