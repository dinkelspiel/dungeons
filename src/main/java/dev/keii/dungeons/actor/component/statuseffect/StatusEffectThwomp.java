package dev.keii.dungeons.actor.component.statuseffect;

import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.actor.component.StatusEffectComponent;
import dev.keii.dungeons.actor.component.VisualComponent;
import dev.keii.dungeons.actor.component.visual.VisualState;
import dev.keii.dungeons.util.SoundUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.utils.time.TimeUnit;

public class StatusEffectThwomp implements StatusEffect {
    VisualComponent visualComponent;

    @Override
    public boolean isStacking() {
        return false;
    }

    @Override
    public void onApply(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        visualComponent = new VisualComponent(new VisualState(Block.ANVIL, new Vec(2, 2, 2)));
        ctx.actor().addComponent(visualComponent);

        livingEntity.getAttribute(Attribute.GRAVITY).setBaseValue(0.04);
        ctx.entity().setVelocity(ctx.entity().getVelocity().mul(0.1).withY(15));

        // I delay the gravity increase so the thwomp has a bit of airtime
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            // TODO: make this into a generic component so two status effects don't
            // overwrite eachother, maybe make it a part of ActorStats
            livingEntity.getAttribute(Attribute.GRAVITY).setBaseValue(0.32);
        }).delay(10, TimeUnit.SERVER_TICK).schedule();
    }

    @Override
    public void onRemove(StatusEffectContext ctx) {
        if (!(ctx.entity() instanceof LivingEntity livingEntity))
            return;

        livingEntity.getAttribute(Attribute.GRAVITY).setBaseValue(0.08);
    }

    @Override
    public void onTick(StatusEffectContext ctx) {
        if (ctx.actor().isJustLanded()) {
            ctx.actor().removeComponent(visualComponent);
            ctx.statusEffectComponent().remove(ctx.getInstance());
            SoundUtil.playInRadius(ctx.instance(), ctx.position(), SoundEvent.BLOCK_ANVIL_LAND);
            ParticlePacket packet = new ParticlePacket(
                    Particle.EXPLOSION,
                    ctx.position(),
                    new Vec(1, 0, 1),
                    3.5f,
                    5);

            ctx.instance().sendGroupedPacket(packet);

            double radius = 8;
            for (Entity entity : ctx.instance().getEntities()) {
                if (entity.getPosition().distanceSquared(ctx.position()) > radius * radius)
                    continue;

                if (!(entity instanceof ActorHolder actor))
                    continue;

                // Don't damage the player
                if (actor.getActor().getId().equals(ctx.actor().getId()))
                    continue;

                actor.getActor().damage(ctx.actor(), 10.0);
                StatusEffectComponent statusEffectComponent = actor.getActor()
                        .getComponent(StatusEffectComponent.class);
                if (statusEffectComponent != null) {
                    statusEffectComponent.add(new StatusEffectStunned(), 3);
                }
            }
        }
    }

    @Override
    public void onBuildTitle(StatusEffectContext ctx, Builder builder) {
        builder.append(Component.text("Thwomp", NamedTextColor.DARK_RED));
    }
}
