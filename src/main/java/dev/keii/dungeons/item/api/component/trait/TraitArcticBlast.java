package dev.keii.dungeons.item.api.component.trait;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.actor.component.StatusEffectComponent;
import dev.keii.dungeons.actor.component.VisualTweenComponent;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffectFrozen;
import dev.keii.dungeons.actor.component.visual.VisualTweenState;
import dev.keii.dungeons.item.context.ItemUseContext;
import dev.keii.dungeons.util.SoundUtil;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.sound.SoundEvent;

public class TraitArcticBlast extends ActiveTrait {
    @Override
    public String key() {
        return "arcticblast";
    }

    @Override
    public String name() {
        return "Arctic Blast";
    }

    @Override
    public void onUse(ItemUseContext ctx) {
        if (!canActivate(ctx))
            return;

        activated(ctx);

        double radius = 8.0;

        SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                SoundEvent.ENTITY_STRAY_DEATH);

        double tweenDuration = 0.2;
        ctx.actor().addTemporaryComponent(
                new VisualTweenComponent(
                        new VisualTweenState(new Vec(1, 0.1, 1), new Vec(radius * 2, 0.1, radius * 2)), tweenDuration),
                tweenDuration);

        for (Entity entity : ctx.instance().getEntities()) {
            if (!(entity instanceof ActorHolder actorEntity))
                continue;

            if (entity.getPosition().distanceSquared(ctx.position()) > radius * radius)
                continue;

            Actor actor = actorEntity.getActor();

            if (actor.getId().equals(ctx.actor().getId()))
                continue;

            StatusEffectComponent effects = actor.getComponent(StatusEffectComponent.class);

            if (effects == null)
                continue;

            effects.add(new StatusEffectFrozen(), 8);
        }
    }

    @Override
    public double cooldownSeconds() {
        return 0.0;
    }
}
