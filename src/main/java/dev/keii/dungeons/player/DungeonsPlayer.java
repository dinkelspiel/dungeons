package dev.keii.dungeons.player;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorFactory;
import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.actor.component.DamageableComponent;
import dev.keii.dungeons.actor.component.HealthComponent;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.minestom.server.sound.SoundEvent;

public class DungeonsPlayer extends Player implements ActorHolder {
    @Getter
    private Actor actor;

    public DungeonsPlayer(PlayerConnection playerConnection, GameProfile gameProfile) {
        super(playerConnection, gameProfile);

        actor = ActorFactory.create(this, (actor) -> {
            actor.addComponent(new HealthComponent(100, 100));
            actor.addComponent(
                    new DamageableComponent(SoundEvent.ENTITY_GENERIC_HURT, SoundEvent.ENTITY_GENERIC_DEATH));
        });
    }

    @Override
    public void tick(long time) {
        actor.tick(time);

        this.sendActionBar(this.getActor().getActionBar());

        super.tick(time);
    }
}
