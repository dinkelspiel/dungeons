package dev.keii.dungeons.npc;

import java.util.function.Supplier;

import dev.keii.dungeons.common.Registry;

public class NpcRegistry extends Registry<Npc> {
    public NpcRegistry() {
        register(NpcZombie::new);
    }

    @Override
    public void register(Supplier<Npc> factory) {
        Npc npc = factory.get();
        register(npc.key(), factory);
    }
}
