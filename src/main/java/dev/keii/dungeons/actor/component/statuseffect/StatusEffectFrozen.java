package dev.keii.dungeons.actor.component.statuseffect;

import dev.keii.dungeons.actor.component.VisualComponent;
import dev.keii.dungeons.actor.component.visual.VisualState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.block.Block;

public class StatusEffectFrozen implements StatusEffect {
    VisualComponent visualComponent;
    Pos frozenPosition;

    @Override
    public boolean isStacking() {
        return false;
    }

    @Override
    public void onBuildTitle(StatusEffectContext ctx, Builder builder) {
        builder.append(Component.text("Frozen", NamedTextColor.AQUA));
    }

    @Override
    public void onApply(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        if (frozenPosition == null)
            frozenPosition = ctx.entity().getPosition();

        visualComponent = new VisualComponent(new VisualState(Block.ICE, new Vec(1, 1, 1)));
        ctx.actor().addComponent(visualComponent);

        // Default is 0.1f;
        livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0);
    }

    @Override
    public void onRemove(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        livingEntity.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.1);
        ctx.actor().removeComponent(visualComponent);
    }

    @Override
    public void onTick(StatusEffectContext ctx) {
        if (frozenPosition.y() > ctx.entity().getPosition().y()) {
            frozenPosition = frozenPosition.withY(ctx.entity().getPosition().y());
            ctx.entity().teleport(frozenPosition);
        }
        ctx.entity().teleport(frozenPosition);
    }
}
