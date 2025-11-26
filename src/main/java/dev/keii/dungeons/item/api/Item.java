package dev.keii.dungeons.item.api;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.common.ComponentContainer;
import dev.keii.dungeons.item.context.ItemAttackContext;
import net.minestom.server.item.Material;

public abstract class Item implements ComponentContainer<ItemComponent> {
    public abstract String key();

    public abstract String name();

    public abstract Material material();

    public abstract ItemRarity rarity();

    @Override
    public <T extends ItemComponent> T getComponent(Class<T> type) {
        ItemComponent comp = components().stream()
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst()
                .orElse(null);
        if (comp == null) {
            return null;
        }
        return type.cast(comp);
    }

    @Override
    public boolean hasComponent(Class<? extends ItemComponent> type) {
        return getComponent(type) != null;
    }

    public void onAttackEntity(Actor actor, Actor target) {
        for (ItemComponent component : components()) {
            component.onAttackEntity(new ItemAttackContext(this, actor, target));
        }
    }
}
