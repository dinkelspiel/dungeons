package dev.keii.dungeons.npc;

import java.util.Optional;

import dev.keii.dungeons.Dungeons;
import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.context.ItemContext;
import dev.keii.dungeons.item.model.GameItemInstance;
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

        if (actor.isJustLanded()) {
            Optional<GameItemInstance> instance = actor.getItemInMainHand();
            if (instance.isPresent()) {
                Item item = instance.get().getItem();
                item.components().stream().forEach(component -> {
                    component.onHolderLand(new ItemContext(item, actor));
                });
            }
        }

        super.tick(time);
    }

    public void setCustomName() {
        this.set(DataComponents.CUSTOM_NAME, actor.getActionBar());
    }
}
