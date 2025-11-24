package dev.keii.dungeons.item.context;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.item.api.Item;
import lombok.Getter;

public abstract class ItemContext {
    @Getter
    private Item item;

    @Getter
    Actor actor;

    public ItemContext(Item item, Actor actor) {
        this.item = item;
        this.actor = actor;
    }
}
