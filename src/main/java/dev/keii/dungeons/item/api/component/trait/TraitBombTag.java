package dev.keii.dungeons.item.api.component.trait;

import dev.keii.dungeons.actor.component.StatusEffectComponent;
import dev.keii.dungeons.actor.component.statuseffect.StatusEffectBombTag;
import dev.keii.dungeons.item.context.ItemAttackContext;
import dev.keii.dungeons.item.context.ItemUseContext;

public class TraitBombTag extends ActiveTrait {
    @Override
    public String key() {
        return "bombtag";
    }

    @Override
    public String name() {
        return "Bomb Tag";
    }

    @Override
    public void onAttackEntity(ItemAttackContext ctx) {
        if (!canActivate(ctx))
            return;

        activated(ctx);

        ctx.getVictim().getComponent(StatusEffectComponent.class)
                .add(new StatusEffectBombTag(5, ctx.actor()), 5);
    }

    @Override
    public void onUse(ItemUseContext ctx) {
        if (!canActivate(ctx))
            return;

        activated(ctx);

        ctx.actor().getComponent(StatusEffectComponent.class)
                .add(new StatusEffectBombTag(5, ctx.actor()), 5);
    }

    @Override
    public double cooldownSeconds() {
        return 0.0;
    }
}
