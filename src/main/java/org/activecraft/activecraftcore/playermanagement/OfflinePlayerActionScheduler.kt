package org.activecraft.activecraftcore.playermanagement

import org.activecraft.activecraftcore.ActiveCraftCore
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object OfflinePlayerActionScheduler {

    private var initialized = false
    private val offlineQueue = HashMap<Profile, Queue<() -> Unit>>()

    @JvmStatic
    fun schedule(profile: Profile, action: Profile.() -> Unit) {
        offlineQueue.computeIfAbsent(profile) { LinkedList() }
        offlineQueue[profile]!!.offer { profile.action() }
    }

    @JvmStatic
    fun initialize() {
        if (initialized) return
        initialized = true
        val runnable: BukkitRunnable = object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (offlineQueue[Profile.of(player)] == null) continue
                    if (offlineQueue[Profile.of(player)]!!.size == 0) continue
                    execute(Profile.of(player))
                }
            }
        }
        runnable.runTaskTimer(ActiveCraftCore.INSTANCE, 0, 40)
    }

    @JvmStatic
    private fun execute(profile: Profile) {
        while (!offlineQueue[profile]!!.isEmpty() && offlineQueue[profile]!!.peek() != null) {
            offlineQueue[profile]!!.poll()!!()
        }
    }
}