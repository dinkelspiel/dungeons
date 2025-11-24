package dev.keii.dungeons.actor.component.statuseffect;

import net.minestom.server.entity.LivingEntity;

public class StatusEffectBurning implements StatusEffect {
    @Override
    public void onTick(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        livingEntity.setFireTicks(20);
    }

    @Override
    public void onRemove(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        livingEntity.setFireTicks(0);
    }

    @Override
    public boolean isStacking() {
        return false;
    }
}
