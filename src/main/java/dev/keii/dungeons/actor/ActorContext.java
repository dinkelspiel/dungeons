package dev.keii.dungeons.actor;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Instance;

public class ActorContext {
    private final Actor actor;

    public ActorContext(Actor actor) {
        this.actor = actor;
    }

    public Actor actor() {
        return actor;
    }

    public Entity entity() {
        return actor.getEntity();
    }

    public Instance instance() {
        return actor.getEntity().getInstance();
    }

    public Pos position() {
        return actor.getEntity().getPosition();
    }

    public <T extends ActorComponent> T get(Class<T> type) {
        return actor.getComponent(type);
    }
}
