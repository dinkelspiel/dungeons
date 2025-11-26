package dev.keii.dungeons.npc;

import dev.keii.dungeons.actor.ActorFactory;
import dev.keii.dungeons.util.MathUtil;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.BlockDisplayMeta;
import net.minestom.server.instance.block.Block;

public class NpcCoinBolt extends Npc {
    long durationUntilAtEnd = 5;
    long aliveTicks = 0;
    Pos startPos, endPos;

    public NpcCoinBolt(Pos startPos, Pos endPos) {
        super(EntityType.BLOCK_DISPLAY);

        this.actor = ActorFactory.create(this, (a) -> {
        });

        BlockDisplayMeta meta = (BlockDisplayMeta) getEntityMeta();

        meta.setBlockState(Block.GOLD_BLOCK);
        meta.setHasNoGravity(true);
        meta.setScale(new Vec(0.3, 0.3, 0.3));
        setNoGravity(true);

        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public String key() {
        return "coin_bolt";
    }

    @Override
    public String name() {
        return "Coin Bolt";
    }

    @Override
    public void tick(long time) {
        aliveTicks++;
        double t = (double) aliveTicks / (double) durationUntilAtEnd;
        if (t >= 1.0) {
            t = 1;
        }

        double y = -4 * (t * t) + 4 * t;
        y *= 2;

        Pos pos = new Pos(
                MathUtil.lerp(startPos.x(), endPos.x(), t),
                MathUtil.lerp(startPos.y(), endPos.y(), t),
                MathUtil.lerp(startPos.z(), endPos.z(), t)).add(0, y, 0);
        if (this.getInstance() != null) {
            setVelocity(pos.sub(getPosition()).asVec().mul(3));
            teleport(pos);
        }
    }

}
