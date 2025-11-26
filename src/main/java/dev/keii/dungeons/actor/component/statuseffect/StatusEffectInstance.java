package dev.keii.dungeons.actor.component.statuseffect;

import dev.keii.dungeons.actor.Actor;
import lombok.Getter;
import lombok.Setter;

public final class StatusEffectInstance {
    private final StatusEffect effect;
    private final Actor actor;
    @Getter
    @Setter
    private Double remaining; // seconds

    public StatusEffectInstance(StatusEffect effect, Actor actor, Double durationSeconds) {
        this.effect = effect;
        this.actor = actor;
        this.remaining = durationSeconds;
    }

    public StatusEffect effect() {
        return effect;
    }

    public boolean isIndefinete() {
        return remaining == null;
    }

    public boolean isExpired() {
        return remaining <= 0;
    }

    public void tick() {
        if (remaining == null)
            return;

        // Minecraft tickrate
        remaining -= 1f / 20f;
    }

    public Actor actor() {
        return actor;
    }

    /**
     * A convenience method to get a StatusContext for the effect.
     */
    public StatusEffectContext ctx() {
        return new StatusEffectContext(actor, this);
    }
}
