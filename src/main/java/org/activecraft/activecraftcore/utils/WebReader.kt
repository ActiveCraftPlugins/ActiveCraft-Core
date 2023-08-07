package org.activecraft.activecraftcore.utils

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.channels.Channels

object WebReader {
    var acVersionMap = mutableMapOf<String, Int>()
    var lastACVersionUpdate: Long = 0
    val aCVersionMap: Map<String, Int>
        get() {
            if (System.currentTimeMillis() - lastACVersionUpdate < 600000) return acVersionMap
            val map = mutableMapOf<String, Int>()
            val rawMap: Map<*, *> = try {
                readAsMap(URL("https://raw.githubusercontent.com/CPlaiz/ActiveCraft-Core/master/plugins.json"))
            } catch (e: IOException) {
                return acVersionMap
            }
            for (key in rawMap.keys) map[key.toString()] = rawMap[key.toString()] as Int
            lastACVersionUpdate = System.currentTimeMillis()
            return map.also { acVersionMap = it }
        }

    @Throws(IOException::class)
    fun readAsMap(url: URL): Map<*, *> {
        return ObjectMapper().readValue(url, Map::class.java)
    }

    @Throws(IOException::class)
    fun readAsList(url: URL): List<*> {
        return ObjectMapper().readValue(url, List::class.java)
    }

    @Throws(IOException::class)
    fun readAsString(url: URL): String {
        val conn = url.openConnection()
        val `in` = BufferedReader(
            InputStreamReader(
                conn.getInputStream()
            )
        )
        val buffer = StringBuffer()
        var inputLine: String?
        while (`in`.readLine().also { inputLine = it } != null) buffer.append(inputLine)
        `in`.close()
        return buffer.toString()
    }

    @Throws(IOException::class)
    fun downloadFile(url: URL, localFilename: String?) {
        val readableByteChannel = Channels.newChannel(url.openStream())
        val fileOutputStream = FileOutputStream(localFilename)
        fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
    }
}