package de.silencio.activecraftcore.playermanagement;

import de.silencio.activecraftcore.ActiveCraftCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class PlayerQueue {

    private static boolean initialized = false;

    private static final HashMap<Profile, Queue<Action>> offlineQueue = new HashMap<>();

    public static void execute(Profile profile) {
        while (!offlineQueue.get(profile).isEmpty() && offlineQueue.get(profile).peek() != null) {
            offlineQueue.get(profile).poll().run();
        }
    }

    public static void add(Profile profile, Action action) {
        offlineQueue.computeIfAbsent(profile, k -> new LinkedList<>());
        offlineQueue.get(profile).offer(action);
    }

    public static void initialize() {
        if (initialized) return;
        initialized = true;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (offlineQueue.get(Profile.of(player)) == null) continue;
                    if (offlineQueue.get(Profile.of(player)).size() == 0) continue;
                    execute(Profile.of(player));
                }
            }
        };
        runnable.runTaskTimer(ActiveCraftCore.getPlugin(), 0, 40);
    }
}
