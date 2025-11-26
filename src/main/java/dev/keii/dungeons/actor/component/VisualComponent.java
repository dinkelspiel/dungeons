package dev.keii.dungeons.actor.component;

import dev.keii.dungeons.actor.ActorComponent;
import dev.keii.dungeons.actor.ActorContext;
import dev.keii.dungeons.actor.component.visual.VisualState;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;

public class VisualComponent implements ActorComponent {
    public Entity entity;
    boolean instantiated = false;
    VisualState state;

    public VisualComponent(VisualState state) {
        entity = new Entity(EntityType.BLOCK_DISPLAY);

        BlockDisplayMeta meta = (BlockDisplayMeta) entity.getEntityMeta();

        meta.setBlockState(state.getBlock());
        meta.setHasNoGravity(true);
        meta.setScale(state.getScale());

        this.state = state;
    }

    @Override
    public void onTick(ActorContext ctx, long time) {
        if (!instantiated) {
            entity.setInstance(ctx.instance());
            instantiated = true;
        }

        Vec offset = new Vec(state.getScale().x() * 0.5, 0, -state.getScale().z() * 0.5);
        entity.teleport(ctx.position().withDirection(new Vec(90, 0, 0)).sub(offset));
        entity.setVelocity(ctx.entity().getVelocity());
    }

    @Override
    public void onRemove(ActorContext ctx) {
        entity.remove();
    }

}
