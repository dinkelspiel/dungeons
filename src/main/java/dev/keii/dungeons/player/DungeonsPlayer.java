package dev.keii.dungeons.player;

import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;

public class DungeonsPlayer extends Player {
    @Getter
    private EntityContext context;

    public DungeonsPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);

        context = new EntityContext(this);
    }
}
