package org.activecraft.activecraftcore.utils

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import java.io.IOException
import java.net.URL
import java.util.*

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
class UpdateChecker(private val plugin: Plugin, private val resourceId: Int) {
    fun getVersion(consumer: (String) -> Unit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            try {
                URL("https://api.spigotmc.org/legacy/update.php?resource=$resourceId").openStream()
                    .use { inputStream ->
                        Scanner(inputStream).use { scanner ->
                            if (scanner.hasNext()) {
                                consumer(scanner.next())
                            }
                        }
                    }
            } catch (exception: IOException) {
                plugin.logger.info("Unable to check for updates: " + exception.message)
            }
        })
    }
}