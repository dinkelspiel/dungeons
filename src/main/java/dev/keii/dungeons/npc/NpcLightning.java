package dev.keii.dungeons.npc;

import dev.keii.dungeons.actor.ActorFactory;
import net.minestom.server.entity.EntityType;

public class NpcLightning extends Npc {
    int aliveTicks = 0;

    public NpcLightning(boolean playSound) {
        super(EntityType.LIGHTNING_BOLT);

        this.actor = ActorFactory.create(this, (a) -> {
        });

        if (!playSound) {
            setSilent(true);
        }
    }

    @Override
    public String key() {
        return "lightning";
    }

    @Override
    public String name() {
        return "Lightning";
    }

    @Override
    public void tick(long time) {
        aliveTicks++;
        if (aliveTicks > 10) {
            this.actor.remove();
        }
    }
}
