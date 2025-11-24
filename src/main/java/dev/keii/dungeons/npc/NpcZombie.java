package dev.keii.dungeons.npc;

import dev.keii.dungeons.actor.ActorFactory;
import dev.keii.dungeons.actor.component.DamageableComponent;
import dev.keii.dungeons.actor.component.HealthComponent;
import dev.keii.dungeons.actor.component.StatusEffectComponent;
import net.minestom.server.entity.EntityType;
import net.minestom.server.sound.SoundEvent;

public class NpcZombie extends Npc {
    public NpcZombie() {
        super(EntityType.ZOMBIE);

        this.actor = ActorFactory.create(this, (a) -> {
            a.addComponent(new HealthComponent(100, 100));
            a.addComponent(new DamageableComponent(SoundEvent.ENTITY_ZOMBIE_HURT, SoundEvent.ENTITY_ZOMBIE_DEATH));
            a.addComponent(new StatusEffectComponent(a));
        });

        setCustomName();
    }

    @Override
    public String key() {
        return "zombie";
    }

    @Override
    public String name() {
        return "Zombie";
    }
}
