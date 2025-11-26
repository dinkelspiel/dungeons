package dev.keii.dungeons.item.api.component.trait;

import dev.keii.dungeons.actor.ActorTags;
import dev.keii.dungeons.item.context.ItemContext;
import dev.keii.dungeons.item.context.ItemUseContext;
import dev.keii.dungeons.util.SoundUtil;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.sound.SoundEvent;

public class TraitMajesticLeap extends ActiveTrait {
    @Override
    public String key() {
        return "majesticleap";
    }

    @Override
    public String name() {
        return "Majestic Leap";
    }

    @Override
    public void onUse(ItemUseContext ctx) {
        if (!canActivate(ctx))
            return;

        activated(ctx);

        Vec direction = ctx.entity().getPosition().direction().mul(100);
        SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                SoundEvent.ENTITY_BREEZE_WIND_BURST);
        SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                SoundEvent.BLOCK_FROGSPAWN_HATCH);
        ctx.entity().setVelocity(direction.withY(20));

        ctx.entity().setTag(ActorTags.IS_LEAPING, true);
    }

    @Override
    public void onHolderLand(ItemContext ctx) {
        if (ctx.entity().getTag(ActorTags.IS_LEAPING) != null
                && ctx.entity().getTag(ActorTags.IS_LEAPING)) {
            ctx.entity().setTag(ActorTags.IS_LEAPING, false);
            SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                    SoundEvent.BLOCK_STONE_BREAK);
            SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                    SoundEvent.BLOCK_MUD_STEP);
        }
    }

    @Override
    public double cooldownSeconds() {
        return 0.0;
    }
}
