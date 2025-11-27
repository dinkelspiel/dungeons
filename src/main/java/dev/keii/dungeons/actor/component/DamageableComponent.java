package dev.keii.dungeons.actor.component;

import java.util.Optional;

import dev.keii.dungeons.actor.ActorComponent;
import dev.keii.dungeons.actor.ActorContext;
import dev.keii.dungeons.common.DamageType;
import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemStatResolver;
import dev.keii.dungeons.item.api.ItemStats;
import dev.keii.dungeons.item.model.GameItemInstance;
import dev.keii.dungeons.util.SoundUtil;
import lombok.Getter;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.sound.SoundEvent;

public class DamageableComponent implements ActorComponent {
    long last = System.currentTimeMillis();

    @Getter
    protected SoundEvent damageSound;

    @Getter
    protected SoundEvent deathSound;

    public void playSoundDamaged(ActorContext ctx) {
        if (damageSound == null)
            return;
        SoundUtil.playInRadius(ctx.instance(), ctx.position(), damageSound);
    }

    public void playSoundDeath(ActorContext ctx) {
        if (deathSound == null)
            return;
        SoundUtil.playInRadius(ctx.instance(), ctx.position(), deathSound);
    }

    public DamageableComponent(SoundEvent damageSound, SoundEvent deathSound) {
        this.damageSound = damageSound;
        this.deathSound = deathSound;
    }

    @Override
    public void onInteract(ActorContext ctx, ActorContext attacker) {
        if (System.currentTimeMillis() - last < 450) {
            return;
        }

        double damage = 0;

        if (attacker.entity() instanceof LivingEntity _) {
            // Find the id for the held item
            Optional<GameItemInstance> gameItemInstance = attacker.actor().getItemInMainHand();
            if (gameItemInstance.isEmpty()) {
                return;
            }
            Item item = gameItemInstance.get().getItem();

            // Send the attack to the item
            item.onAttackEntity(attacker.actor(), ctx.actor());

            // Calculate the damage
            ItemStatResolver statResolver = new ItemStatResolver();
            ItemStats stats = statResolver.resolve(item, ctx.actor(), attacker.actor());
            damage = stats.getDamage();
        }

        ctx.actor().damage(attacker.actor(), damage, DamageType.MELEE);

        last = System.currentTimeMillis();
    }
}
