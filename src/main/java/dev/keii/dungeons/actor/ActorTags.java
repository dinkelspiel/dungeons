package dev.keii.dungeons.actor;

import net.minestom.server.tag.Tag;

public final class ActorTags {
    public static final Tag<Boolean> IS_LEAPING = Tag.Boolean("dungeons:is_leaping");

    public static Tag<Long> cooldown(String key) {
        return Tag.Long("dungeons:cooldown_" + key);
    }
}
