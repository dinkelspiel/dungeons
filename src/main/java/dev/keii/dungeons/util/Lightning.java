package dev.keii.dungeons.util;

import dev.keii.dungeons.npc.NpcLightning;
import net.minestom.server.coordinate.Pos;

public class Lightning {
    // TODO: fix this still making a sound if playSound is false
    // https://github.com/Minestom/Minestom/discussions/2983
    public static void strike(Pos pos, boolean playSound) {
        NpcLightning npc = new NpcLightning(playSound);
        npc.spawn(pos);
    }
}
