package dev.keii.dungeons.actor.component;

import dev.keii.dungeons.actor.ActorComponent;
import dev.keii.dungeons.actor.ActorContext;
import dev.keii.dungeons.actor.component.visual.VisualTweenState;
import dev.keii.dungeons.util.MathUtil;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.block.Block;

public class VisualTweenComponent implements ActorComponent {
    public Entity entity;
    BlockDisplayMeta meta;
    boolean instantiated = false;
    long startTime;
    VisualTweenState state;
    double durationSeconds;

    public VisualTweenComponent(VisualTweenState state, double durationSeconds) {
        entity = new Entity(EntityType.BLOCK_DISPLAY);

        meta = (BlockDisplayMeta) entity.getEntityMeta();

        meta.setBlockState(Block.ICE);
        meta.setHasNoGravity(true);
        meta.setScale(state.fromScale);
        meta.setTranslation(new Vec(-0.5, 0, -0.5));

        this.state = state;

        startTime = System.currentTimeMillis();
        this.durationSeconds = durationSeconds;
    }

    @Override
    public void onTick(ActorContext ctx, long time) {
        if (!instantiated) {
            entity.setInstance(ctx.instance());
            instantiated = true;
        }

        double t = (System.currentTimeMillis() - startTime) / (durationSeconds * 1000.0);
        Vec scale = new Vec(
                MathUtil.lerp(state.fromScale.x(), state.toScale.x(), t),
                MathUtil.lerp(state.fromScale.y(), state.toScale.y(), t),
                MathUtil.lerp(state.fromScale.z(), state.toScale.z(), t));
        meta.setScale(scale);
        Vec offset = new Vec(scale.x() * 0.5, 0, -scale.z() * 0.5);
        entity.teleport(ctx.position().withDirection(new Vec(90, 0, 0)).sub(offset));
    }

    @Override
    public void onRemove(ActorContext ctx) {
        entity.remove();
    }

}
