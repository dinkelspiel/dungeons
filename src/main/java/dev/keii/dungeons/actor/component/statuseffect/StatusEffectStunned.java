package dev.keii.dungeons.actor.component.statuseffect;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;

public class StatusEffectStunned implements StatusEffect {
    Pos stunnedPosition;

    @Override
    public boolean isStacking() {
        return false;
    }

    @Override
    public void onBuildTitle(StatusEffectContext ctx, Builder builder) {
        builder.append(Component.text("Stunned", NamedTextColor.YELLOW));
    }

    @Override
    public void onApply(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        if (stunnedPosition == null)
            stunnedPosition = ctx.entity().getPosition();

        livingEntity.setSneaking(true);

        // Default is 0.1f;
        livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0);
    }

    @Override
    public void onRemove(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.1);
        livingEntity.setSneaking(false);
    }

    @Override
    public void onTick(StatusEffectContext ctx) {
        if (stunnedPosition.y() > ctx.entity().getPosition().y()) {
            stunnedPosition = stunnedPosition.withY(ctx.entity().getPosition().y());
            ctx.entity().teleport(stunnedPosition);
        }
        ctx.entity().teleport(stunnedPosition);
    }
}
