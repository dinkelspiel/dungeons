package dev.keii.dungeons.item.context;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.item.api.Item;
import net.kyori.adventure.audience.Audience;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;

public class ItemContext {
    private Item item;

    private Actor actor;

    public ItemContext(Item item, Actor actor) {
        this.item = item;
        this.actor = actor;
    }

    public Item item() {
        return item;
    }

    public Actor actor() {
        return actor;
    }

    public Entity entity() {
        return actor.getEntity();
    }

    public Audience audience() {
        return (Audience) actor.getEntity();
    }

    public Instance instance() {
        return entity().getInstance();
    }

    public Pos position() {
        return entity().getPosition();
    }
}
