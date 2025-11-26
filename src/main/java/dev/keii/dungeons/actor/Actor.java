package dev.keii.dungeons.actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import dev.keii.dungeons.actor.component.ActorStatsComponent;
import dev.keii.dungeons.actor.component.DamageableComponent;
import dev.keii.dungeons.actor.component.HealthComponent;
import dev.keii.dungeons.actor.component.stats.ActorStats;
import dev.keii.dungeons.common.ComponentContainer;
import dev.keii.dungeons.common.DamageResult;
import dev.keii.dungeons.item.api.ItemTags;
import dev.keii.dungeons.item.dao.GameItemInstanceDao;
import dev.keii.dungeons.item.model.GameItemInstance;
import dev.keii.dungeons.npc.Npc;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.damage.Damage;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.time.TimeUnit;

public final class Actor implements ComponentContainer<ActorComponent> {
    private final UUID id;
    private final Entity entity;
    private final Map<Class<? extends ActorComponent>, ActorComponent> components = new HashMap<>();
    private final List<ActorComponent> componentList = new ArrayList<>();
    private boolean wasOnGroundLastTick = true;
    @Getter
    private boolean justLanded = false;
    private final Map<String, Object> data = new ConcurrentHashMap<>();

    public Actor(Entity entity) {
        this.id = entity.getUuid();
        this.entity = entity;
    }

    public UUID getId() {
        return id;
    }

    public Entity getEntity() {
        return entity;
    }

    public <T extends ActorComponent> T getComponent(Class<T> type) {
        ActorComponent comp = components.get(type);
        if (comp == null) {
            return null;
        }
        return type.cast(comp);
    }

    public void addComponent(ActorComponent component) {
        components.put(component.getClass(), component);
        componentList.add(component);
        component.onApply(new ActorContext(this));
    }

    /**
     * Add a component for a set amount of time
     */
    public void addTemporaryComponent(ActorComponent component, double seconds) {
        addComponent(component);

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            removeComponent(component);
            // I wanted more granular control than set seconds so i convert the seconds to
            // ticks to get some more precision
        }).delay((long) (seconds * 20), TimeUnit.SERVER_TICK).schedule();
    }

    /**
     * Remove the component by reference not by type
     */
    public <T extends ActorComponent> void removeComponent(T component) {
        component.onRemove(new ActorContext(this));
        components.remove(component.getClass(), component);
        componentList.remove(component);
    }

    public List<ActorComponent> components() {
        return Collections.unmodifiableList(componentList);
    }

    public Component getActionBar() {
        TextComponent.Builder builder = Component.text();

        for (ActorComponent component : componentList) {
            component.onBuildTitle(new ActorContext(this), builder);
        }

        return builder.build();
    }

    public void tick(long time) {
        for (ActorComponent component : components()) {
            component.onTick(new ActorContext(this), time);
        }

        justLanded = entity.isOnGround() && !wasOnGroundLastTick;

        wasOnGroundLastTick = entity.isOnGround();
    }

    @Override
    public boolean hasComponent(Class<? extends ActorComponent> type) {
        return getComponent(type) != null;
    }

    public DamageResult damage(Actor source, double amount) {
        ActorContext ctx = new ActorContext(this);
        ActorContext srcCtx = source == null ? null : new ActorContext(source);

        if (ctx.get(DamageableComponent.class) == null) {
            return DamageResult.NO_HEALTH;
        }

        // Apply to health if present
        HealthComponent health = getComponent(HealthComponent.class);
        if (health == null) {
            return DamageResult.NO_HEALTH;
        }
        if (health.getHealth() <= 0) {
            return DamageResult.CANCELLED;
        }

        float newHealth = health.getHealth() - (float) amount;
        health.setHealth(newHealth);

        // TODO: We need to add item types here since this system would allow ranged
        // weapons to get melee lifesteal
        ActorStatsComponent attackerStatsComponent = source.getComponent(ActorStatsComponent.class);
        if (attackerStatsComponent != null) {
            ActorStats attackerStats = attackerStatsComponent.calculate(source);
            double heal = amount * attackerStats.meleeLifesteal;
            source.heal(heal);
        }

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.damage(new Damage(DamageType.GENERIC, entity, srcCtx != null ? srcCtx.entity() : null,
                    srcCtx != null ? srcCtx.position() : null, 0));

            if (srcCtx != null) {
                livingEntity.takeKnockback(0.4f, Math.sin(srcCtx.entity().getPosition().yaw() * (Math.PI / 180)),
                        -Math.cos(srcCtx.entity().getPosition().yaw() * (Math.PI / 180)));
            }
        }

        // Notify components of the damage after it happened
        for (ActorComponent c : componentList) {
            c.onDamage(ctx, srcCtx, amount);
        }

        if (ctx.entity() instanceof Npc npc) {
            npc.setCustomName();
        }

        if (newHealth <= 0) {
            kill(source);
            return DamageResult.KILLED;
        }

        return DamageResult.DAMAGED;
    }

    public void heal(double amount) {
        HealthComponent health = getComponent(HealthComponent.class);
        if (health == null)
            return;

        health.setHealth(health.getHealth() + (float) amount);
        if (health.getHealth() > health.getMaxHealth()) {
            health.setHealth(health.getMaxHealth());
        }
    }

    public void kill(Actor killer) {
        ActorContext ctx = new ActorContext(this);
        ActorContext killerCtx = killer == null ? null : new ActorContext(killer);

        for (ActorComponent c : componentList) {
            c.onDeath(ctx, killerCtx);
        }

        DamageableComponent damageableComponent = getComponent(DamageableComponent.class);

        damageableComponent.playSoundDeath(ctx);

        if (ctx.entity() instanceof LivingEntity entity) {
            entity.kill();

            MinecraftServer.getSchedulerManager().buildTask(() -> {
                ctx.entity().remove();

                for (ActorComponent c : componentList) {
                    c.onRemove(ctx);
                }
                componentList.clear();
                components.clear();
            }).delay(15, TimeUnit.SERVER_TICK).schedule();
        } else {
            ctx.entity().remove();

            for (ActorComponent c : componentList) {
                c.onRemove(ctx);
            }
            componentList.clear();
            components.clear();
        }
    }

    public void remove() {
        ActorContext ctx = new ActorContext(this);
        entity.remove();

        for (ActorComponent c : componentList) {
            c.onRemove(ctx);
        }
        componentList.clear();
        components.clear();
    }

    public Optional<GameItemInstance> getItemInMainHand() {
        if (!(getEntity() instanceof LivingEntity entity)) {
            return Optional.empty();
        }

        ItemStack itemStack = entity.getItemInMainHand();
        return getItem(itemStack);
    }

    public Optional<GameItemInstance> getItem(ItemStack itemStack) {
        if (itemStack == null) {
            return Optional.empty();
        }
        String itemId = itemStack.getTag(ItemTags.ID);
        if (itemId == null) {
            return Optional.empty();
        }

        // Get the actual referenced item
        GameItemInstanceDao dao = new GameItemInstanceDao();
        GameItemInstance gameItemInstance = dao.findById(UUID.fromString(itemId));
        if (gameItemInstance == null) {
            return Optional.empty();
        }
        return Optional.of(gameItemInstance);
    }

    public <T> T getData(String key, Class<T> type) {
        Object value = data.get(key);
        return type.isInstance(value) ? type.cast(value) : null;
    }

    public void setData(String key, Object value) {
        data.put(key, value);
    }

    public void removeData(String key) {
        data.remove(key);
    }
}