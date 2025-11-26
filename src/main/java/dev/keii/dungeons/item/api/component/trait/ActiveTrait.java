package dev.keii.dungeons.item.api.component.trait;

import java.text.DecimalFormat;

import dev.keii.dungeons.actor.component.ActorStatsComponent;
import dev.keii.dungeons.actor.component.stats.ActorStats;
import dev.keii.dungeons.item.context.ItemContext;
import dev.keii.dungeons.util.MessageFormatter;
import dev.keii.dungeons.util.SoundUtil;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;

public abstract class ActiveTrait implements ItemTrait {
    public abstract double cooldownSeconds();

    public Tag<Long> cooldownTag() {
        return Tag.Long("dungeons:cooldown_" + key());
    }

    public boolean canActivate(ItemContext ctx) {
        long now = System.currentTimeMillis();
        Long expires = ctx.entity().getTag(cooldownTag());

        if (expires != null && expires > now) {
            // still on cooldown
            SoundUtil.playInRadius(ctx.entity().getInstance(), ctx.entity().getPosition(),
                    SoundEvent.BLOCK_NOTE_BLOCK_BASS);
            DecimalFormat df = new DecimalFormat("#.#");
            ctx.audience().sendMessage(
                    MessageFormatter.error("You can use this active in {}s",
                            df.format((float) (expires - now) / 1000f)));
            return false;
        }
        return true;
    }

    public void activated(ItemContext ctx) {
        // resolve cooldown reduction from actor inventory
        ActorStatsComponent stats = ctx.actor().getComponent(ActorStatsComponent.class);
        ActorStats actorStats = stats.calculate(ctx.actor());

        double cooldownSeconds = cooldownSeconds() * (1.0 - actorStats.cooldownReduction);

        long now = System.currentTimeMillis();
        long expiryTimestamp = now + (long) (cooldownSeconds * 1000.0);
        ctx.entity().setTag(cooldownTag(), expiryTimestamp);
    }
}
