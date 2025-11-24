package dev.keii.dungeons.util;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.sound.SoundEvent;

public class SoundUtil {
    public static void playInRadius(Instance instance, Pos pos, SoundEvent se) {
        if (se == null) {
            se = SoundEvent.ENTITY_GENERIC_HURT;
        }

        float volume = 1.0f;
        float pitch = 1.0f;

        double radius = 16.0;

        Sound sound = Sound.sound(se, Sound.Source.HOSTILE, volume, pitch);

        for (Player viewer : instance.getPlayers()) {
            if (viewer.getPosition().distanceSquared(pos) > radius * radius)
                continue;

            viewer.playSound(sound, pos);
        }
    }
}
