package dev.keii.dungeons.actor.component.statuseffect;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.actor.component.VisualComponent;
import dev.keii.dungeons.actor.component.visual.VisualState;
import dev.keii.dungeons.util.SoundUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.sound.SoundEvent;

public class StatusEffectBombTag implements StatusEffect {
    VisualComponent visualComponent;
    int radius;
    Actor source;

    public StatusEffectBombTag(int radius, Actor source) {
        this.radius = radius;
        this.source = source;
    }

    @Override
    public boolean isStacking() {
        return true;
    }

    @Override
    public void onBuildTitle(StatusEffectContext ctx, Builder builder) {
        builder.append(Component.text("Bomb", NamedTextColor.RED));
    }

    @Override
    public void onApply(StatusEffectContext ctx) {
        SoundUtil.playInRadius(ctx.instance(), ctx.position(), SoundEvent.ENTITY_TNT_PRIMED);

        visualComponent = new VisualComponent(new VisualState(Block.TNT, new Vec(radius * 2, 0.1, radius * 2)));
        ctx.actor().addComponent(visualComponent);
    }

    @Override
    public void onRemove(StatusEffectContext ctx) {
        SoundUtil.playInRadius(ctx.instance(), ctx.position(), SoundEvent.ENTITY_GENERIC_EXPLODE);

        ParticlePacket packet = new ParticlePacket(
                Particle.EXPLOSION,
                ctx.position(),
                new Vec(1, 1, 1),
                4.5f,
                5);

        ctx.instance().sendGroupedPacket(packet);

        for (Entity entity : ctx.instance().getEntities()) {
            if (entity.getPosition().distanceSquared(ctx.position()) > radius * radius)
                continue;

            if (!(entity instanceof ActorHolder actor))
                continue;

            // Don't damage the applier of the bomb
            if (actor.getActor().getId().equals(source.getId()))
                continue;

            actor.getActor().damage(ctx.actor(), 10.0);
        }

        ctx.actor().removeComponent(visualComponent);
    }
}
