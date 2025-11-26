package dev.keii.dungeons.actor.component.statuseffect;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorContext;
import dev.keii.dungeons.actor.component.StatusEffectComponent;
import lombok.Getter;

public class StatusEffectContext extends ActorContext {
    @Getter
    private StatusEffectInstance instance;

    public StatusEffectContext(Actor actor, StatusEffectInstance instance) {
        super(actor);
        this.instance = instance;
    }

    public StatusEffectComponent statusEffectComponent() {
        return actor().getComponent(StatusEffectComponent.class);
    }
}
