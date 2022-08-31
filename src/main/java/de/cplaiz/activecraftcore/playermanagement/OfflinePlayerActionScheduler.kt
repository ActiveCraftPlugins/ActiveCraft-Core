package de.cplaiz.activecraftcore.playermanagement

import de.cplaiz.activecraftcore.ActiveCraftCore
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

object OfflinePlayerActionScheduler {

    private var initialized = false
    private val offlineQueue = HashMap<Profilev2, Queue<() -> Unit>>()

    @JvmStatic
    fun schedule(profile: Profilev2, action: Profilev2.() -> Unit) {
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
                    if (offlineQueue[Profilev2.of(player)] == null) continue
                    if (offlineQueue[Profilev2.of(player)]!!.size == 0) continue
                    execute(Profilev2.of(player)!!)
                }
            }
        }
        runnable.runTaskTimer(ActiveCraftCore.getInstance(), 0, 40)
    }

    @JvmStatic
    private fun execute(profile: Profilev2) {
        while (!offlineQueue[profile]!!.isEmpty() && offlineQueue[profile]!!.peek() != null) {
            offlineQueue[profile]!!.poll()!!()
        }
    }
}