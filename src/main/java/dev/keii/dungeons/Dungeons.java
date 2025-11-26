package dev.keii.dungeons;

import java.util.Optional;

import dev.keii.dungeons.actor.ActorContext;
import dev.keii.dungeons.actor.ActorHolder;
import dev.keii.dungeons.command.GamemodeCommand;
import dev.keii.dungeons.command.HealthCommand;
import dev.keii.dungeons.command.ItemCommand;
import dev.keii.dungeons.command.NpcCommand;
import dev.keii.dungeons.item.api.Item;
import dev.keii.dungeons.item.api.ItemComponent;
import dev.keii.dungeons.item.api.ItemRegistry;
import dev.keii.dungeons.item.context.ItemContext;
import dev.keii.dungeons.item.context.ItemUseContext;
import dev.keii.dungeons.item.model.GameItemInstance;
import dev.keii.dungeons.npc.NpcRegistry;
import dev.keii.dungeons.player.DungeonsPlayer;
import lombok.Getter;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class Dungeons {
    @Getter
    private static NpcRegistry npcRegistry;

    @Getter
    private static ItemRegistry itemRegistry;

    @Getter
    private static InstanceContainer instance;

    static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.getCommandManager().register(new HealthCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());
        MinecraftServer.getCommandManager().register(new NpcCommand());
        MinecraftServer.getCommandManager().register(new ItemCommand());
        MinecraftServer.getConnectionManager().setPlayerProvider(DungeonsPlayer::new);

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        instance = instanceManager.createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instance.setChunkSupplier(LightingChunk::new);
        instance.setTimeRate(0);
        instance.setTime(1000);

        // Instantiate registries
        npcRegistry = new NpcRegistry();
        itemRegistry = new ItemRegistry();

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final DungeonsPlayer player = (DungeonsPlayer) event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            event.setSpawningInstance(instance);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        // globalEventHandler.addListener(PlayerLoadedEvent.class, event -> {
        // final DungeonsPlayer player = (DungeonsPlayer) event.getPlayer();

        // ActionBar.setPersistent(player, player.getActor().getActionBar());
        // });

        globalEventHandler.addListener(EntityAttackEvent.class, event -> {
            Entity _attacker = event.getEntity();
            Entity _victim = event.getTarget();

            if (_victim instanceof ActorHolder victim && _attacker instanceof ActorHolder player) {
                victim.getActor().components().forEach((component) -> {
                    component.onInteract(new ActorContext(victim.getActor()), new ActorContext(player.getActor()));
                });
            }
        });

        globalEventHandler.addListener(PlayerUseItemEvent.class, event -> {
            Entity user = event.getEntity();

            if (user instanceof ActorHolder player) {
                Optional<GameItemInstance> gameItemInstance = player.getActor().getItemInMainHand();
                if (gameItemInstance.isEmpty())
                    return;

                Item item = gameItemInstance.get().getItem();
                for (ItemComponent component : item.components()) {
                    component.onUse(new ItemUseContext(item, player.getActor()));
                }
            }
        });

        globalEventHandler.addListener(PlayerHandAnimationEvent.class, event -> {
            if (event.getHand() == PlayerHand.MAIN) {
                Entity user = event.getEntity();

                if (user instanceof ActorHolder player) {
                    Optional<GameItemInstance> gameItemInstance = player.getActor().getItemInMainHand();
                    if (gameItemInstance.isEmpty())
                        return;

                    Item item = gameItemInstance.get().getItem();
                    for (ItemComponent component : item.components()) {
                        component.onAttack(new ItemContext(item, player.getActor()));
                    }
                }
            }
        });

        globalEventHandler.addListener(PlayerBlockPlaceEvent.class, event -> {
            Entity user = event.getEntity();

            if (user instanceof ActorHolder player) {
                if (player.getActor().getItemInMainHand().isPresent()) {
                    event.setCancelled(true);
                }
            }
        });

        minecraftServer.start("0.0.0.0", 25565);
    }
}
