package dev.keii.dungeons.actor.component;

import java.util.UUID;

import dev.keii.dungeons.actor.ActorComponent;
import dev.keii.dungeons.actor.ActorContext;
import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemStatResolver;
import dev.keii.dungeons.item.api.ItemStats;
import dev.keii.dungeons.item.api.ItemTags;
import dev.keii.dungeons.item.dao.GameItemInstanceDao;
import dev.keii.dungeons.item.model.GameItemInstance;
import dev.keii.dungeons.npc.Npc;
import dev.keii.dungeons.util.SoundUtil;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.utils.time.TimeUnit;

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

        HealthComponent healthComponent = ctx.actor().getComponent(HealthComponent.class);

        double damage = 0;
        if (attacker.entity() instanceof LivingEntity entity) {
            // Find the id for the held item
            ItemStack itemStack = entity.getItemInMainHand();
            if (itemStack == null) {
                return;
            }
            String itemId = itemStack.getTag(ItemTags.ID);

            // Get the actual referenced item
            GameItemInstanceDao dao = new GameItemInstanceDao();
            GameItemInstance gameItemInstance = dao.findById(UUID.fromString(itemId));
            if (gameItemInstance == null) {
                return;
            }
            Item item = gameItemInstance.getItem();

            // Send the attack to the item
            item.onAttack(attacker.actor(), ctx.actor());

            // Calculate the damage
            ItemStatResolver statResolver = new ItemStatResolver();
            ItemStats stats = statResolver.resolve(item, ctx.actor(), attacker.actor());
            damage = stats.getDamage();
        }

        if (healthComponent != null) {
            healthComponent.setHealth(healthComponent.getHealth() - (float) damage);
        }

        if (ctx.entity() instanceof LivingEntity entity) {
            entity.damage(new Damage(DamageType.GENERIC, entity, attacker.entity(), attacker.position(), 0));

            entity.takeKnockback(0.4f, Math.sin(attacker.entity().getPosition().yaw() * (Math.PI / 180)),
                    -Math.cos(attacker.entity().getPosition().yaw() * (Math.PI / 180)));

            if (healthComponent.getHealth() <= 0) {
                entity.kill();
            }
        }

        if (ctx.entity() instanceof Npc npc) {
            npc.setCustomName();
        }

        if (healthComponent.getHealth() <= 0) {
            playSoundDeath(ctx);

            MinecraftServer.getSchedulerManager().buildTask(() -> {
                ctx.entity().remove();
            }).delay(15, TimeUnit.SERVER_TICK).schedule();
        } else {
            playSoundDamaged(ctx);
        }

        last = System.currentTimeMillis();
    }
}
