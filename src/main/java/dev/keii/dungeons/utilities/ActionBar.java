package dev.keii.dungeons.utilities;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

public class ActionBar {
    private static final Map<UUID, Task> persistentActionBars = new ConcurrentHashMap<>();

    public static void setPersistent(Player player, Component message) {
        UUID uuid = player.getUuid();

        Task old = persistentActionBars.remove(uuid);
        if (old != null) {
            old.cancel();
        }

        player.sendActionBar(message);

        Task task = MinecraftServer.getSchedulerManager()
                .buildTask(() -> {
                    if (!player.isOnline()) {
                        Task t = persistentActionBars.remove(uuid);
                        if (t != null)
                            t.cancel();
                        return;
                    }
                    player.sendActionBar(message);
                })
                .delay(TaskSchedule.seconds(1))
                .repeat(TaskSchedule.seconds(1))
                .schedule();

        persistentActionBars.put(uuid, task);
    }
}
