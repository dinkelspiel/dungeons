package dev.keii.dungeons;

import dev.keii.dungeons.commands.GamemodeCommand;
import dev.keii.dungeons.commands.HealthCommand;
import dev.keii.dungeons.player.DungeonsPlayer;
import dev.keii.dungeons.utilities.ActionBar;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerLoadedEvent;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

public class Main {
    static void main(String[] args) {
        MinecraftServer minecraftServer = MinecraftServer.init();
        MinecraftServer.getCommandManager().register(new HealthCommand());
        MinecraftServer.getCommandManager().register(new GamemodeCommand());
        MinecraftServer.getConnectionManager().setPlayerProvider(DungeonsPlayer::new);

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));
        instanceContainer.setChunkSupplier(LightingChunk::new);
        instanceContainer.setTimeRate(0);
        instanceContainer.setTime(1000);

        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final DungeonsPlayer player = (DungeonsPlayer) event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));
        });

        globalEventHandler.addListener(PlayerLoadedEvent.class, event -> {
            final DungeonsPlayer player = (DungeonsPlayer) event.getPlayer();

            ActionBar.setPersistent(player, player.getContext().getActionBar());
        });

        globalEventHandler.addListener(PlayerBlockBreakEvent.class, event -> {
            event.setCancelled(true);
        });

        minecraftServer.start("0.0.0.0", 25565);
    }
}
