package dev.keii.dungeons.player;

import java.util.Optional;

import dev.keii.dungeons.actor.Actor;
import dev.keii.dungeons.actor.ActorFactory;
import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.actor.component.ActorStatsComponent;
import dev.keii.dungeons.actor.component.DamageableComponent;
import dev.keii.dungeons.actor.component.HealthComponent;
import dev.keii.dungeons.actor.component.StatusEffectComponent;
import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemFactory;
import dev.keii.dungeons.item.context.ItemContext;
import dev.keii.dungeons.item.model.GameItemInstance;
import lombok.Getter;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
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
            actor.addComponent(new StatusEffectComponent(actor));
            actor.addComponent(new ActorStatsComponent(new Integer[] { 9, 10, 11, 12, 13, 18, 19, 20, 21, 22 }));
        });
    }

    @Override
    public void tick(long time) {
        actor.tick(time);

        this.sendActionBar(this.getActor().getActionBar());

        if (actor.isJustLanded()) {
            for (ItemStack itemStack : getInventory().getItemStacks()) {
                Optional<GameItemInstance> instance = ItemFactory.fromItemStack(itemStack);
                if (instance.isPresent()) {
                    Item item = instance.get().getItem();
                    item.components().stream().forEach(component -> {
                        component.onHolderLand(new ItemContext(item, actor));
                    });
                }
            }
        }

        super.tick(time);
    }
}
