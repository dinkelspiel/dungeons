package dev.keii.dungeons.actor;

import java.util.function.Consumer;

import net.minestom.server.entity.Entity;

public class ActorFactory {
    public static Actor create(Entity entity, Consumer<Actor> config) {
        Actor actor = new Actor(entity);
        config.accept(actor);

        ActorContext ctx = new ActorContext(actor);
        actor.components().forEach(c -> c.onSpawn(ctx));
        return actor;
    }
}