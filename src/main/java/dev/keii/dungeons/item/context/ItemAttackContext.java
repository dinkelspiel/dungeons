package dev.keii.dungeons.item.context;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.item.api.Item;
import lombok.Getter;

public final class ItemAttackContext extends ItemContext {
    @Getter
    private Actor victim;

    public ItemAttackContext(Item item, Actor actor, Actor victim) {
        super(item, actor);
        this.victim = victim;
    }
}
