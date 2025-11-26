package dev.keii.dungeons.actor.component.statuseffect;

import dev.keii.dungeons.actor.Actor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.LivingEntity;

public class StatusEffectBurning implements StatusEffect {
    int ticksSinceFireTick = 0;
    int ticksBetweenFireTick = 10;
    Actor source;

    public StatusEffectBurning(Actor source) {
        this.source = source;
    }

    @Override
    public void onTick(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        if (ticksSinceFireTick >= ticksBetweenFireTick) {
            ctx.actor().damage(source, 2);
            ticksSinceFireTick = 0;
        }
        ticksSinceFireTick++;

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

    @Override
    public void onBuildTitle(StatusEffectContext ctx, Builder builder) {
        builder.append(Component.text("Burning", NamedTextColor.RED));
    }
}
