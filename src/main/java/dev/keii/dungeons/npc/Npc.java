package dev.keii.dungeons.npc;

import dev.keii.dungeons.Dungeons;
import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorHolder;
import lombok.Getter;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;

public abstract class Npc extends LivingEntity implements ActorHolder {
    @Getter
    protected Actor actor;

    public Npc(EntityType entityType) {
        super(entityType);
    }

    public abstract String key();

    public abstract String name();

    public void spawn(Pos pos) {
        this.setInstance(Dungeons.getInstance(), pos);
    }

    @Override
    public void tick(long time) {
        actor.tick(time);

        super.tick(time);
    }

    public void setCustomName() {
        this.set(DataComponents.CUSTOM_NAME, actor.getActionBar());
    }
}
