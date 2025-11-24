package dev.keii.dungeons.actor;

public interface ActorComponent {
    default void onSpawn(ActorContext ctx) {
    }

    default void onTick(ActorContext ctx, long time) {
    }

    default void onDamage(ActorContext ctx, ActorContext source, double amount) {
    }

    default void onDeath(ActorContext ctx, ActorContext killer) {
    }

    default void onInteract(ActorContext ctx, ActorContext player) {
    }
}