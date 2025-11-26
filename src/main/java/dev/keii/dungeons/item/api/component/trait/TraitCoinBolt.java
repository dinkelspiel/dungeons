package dev.keii.dungeons.item.api.component.trait;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.item.context.ItemContext;
import dev.keii.dungeons.item.context.ItemUseContext;
import dev.keii.dungeons.npc.NpcCoinBolt;
import dev.keii.dungeons.util.Lightning;
import dev.keii.dungeons.util.MathUtil;
import dev.keii.dungeons.util.MessageFormatter;
import dev.keii.dungeons.util.SoundUtil;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.sound.SoundEvent;

public class TraitCoinBolt extends ActiveTrait {
    @Override
    public String key() {
        return "coin_bolt";
    }

    @Override
    public String name() {
        return "Coin Bolt";
    }

    @Override
    public double cooldownSeconds() {
        return 4.0;
    }

    @Override
    public void onUse(ItemUseContext ctx) {
        if (!canActivate(ctx))
            return;

        activated(ctx);

        NpcCoinBolt npc = ctx.actor().getData("trait_coin_bolt_npc", NpcCoinBolt.class);
        if (npc != null) {
            SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                    SoundEvent.BLOCK_NOTE_BLOCK_BASS);

            ctx.audience().sendMessage(MessageFormatter.error("You already have a Coin Bolt active."));
            return;
        }

        SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                SoundEvent.BLOCK_LANTERN_BREAK);
        SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                SoundEvent.BLOCK_LANTERN_PLACE);

        Vec randomOffset = MathUtil.randomVectorInRadius(1.0);
        npc = new NpcCoinBolt(ctx.position(),
                ctx.position().add(ctx.position().direction().normalize().mul(6)).withY(ctx.position().y() + 4)
                        .add(randomOffset));
        npc.spawn(ctx.position());
        ctx.actor().setData("trait_coin_bolt_npc", npc);
    }

    @Override
    public void onAttack(ItemContext ctx) {
        NpcCoinBolt npc = ctx.actor().getData("trait_coin_bolt_npc", NpcCoinBolt.class);
        if (npc == null) {
            ctx.audience().sendMessage(MessageFormatter.error("You must use Coin Bolt before attacking with it."));
            return;
        }
        if (MathUtil.isLookingAt(ctx.entity(), npc.getPosition(), 10)) {
            SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                    SoundEvent.BLOCK_BELL_USE);
            npc.getActor().remove();

            int radius = 16;
            for (Entity entity : ctx.instance().getEntities()) {
                if (!(entity instanceof ActorHolder actorEntity))
                    continue;

                if (entity.getPosition().distanceSquared(ctx.position()) > radius * radius)
                    continue;

                Actor actor = actorEntity.getActor();

                if (actor.getId().equals(ctx.actor().getId()))
                    continue;

                actor.damage(ctx.actor(), 15.0);

                Lightning.strike(actor.getEntity().getPosition(), false);
            }
            ctx.actor().removeData("trait_coin_bolt_npc");
        }
    }

}
