package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.ChatColor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object Profiles : Table("profiles") {

    //val profileId = integer("id").autoIncrement()
    val uuid = uuid("uuid")
    val name = text("name")
    val nickname = text("nickname")
    val prefix = text("prefix")
    val lastOnline = datetime("last_online").nullable()
    val colorNick = text("colornick");
    val timesJoined = integer("times_joined")
    val timesWarned = integer("times_warned")
    val timesMuted = integer("times_muted")
    val timesBanned = integer("times_banned")
    val timesIpBanned = integer("times_ip_banned")
    val playtime = integer("playtime") // in minutes
    val afk = bool("afk")
    val godmode = bool("godmode")
    val fly = bool("fly")
    val muted = bool("muted")
    val defaultmuted = bool("defaultmuted")
    val vanished = bool("vanished")
    val receiveLog = bool("receive_log")
    val bypassLockdown = bool("bypass_lockdown")
    val receiveSocialspy = bool("receive_socialspy")
    val editSign = bool("edit_sign")

    //private val extensionLoaders = mutableListOf<Profilev2s.(ResultRow) -> Unit>()

    override val primaryKey = PrimaryKey(uuid, name = "profiles_pk")

    fun toProfile(row: ResultRow): Profilev2 {
        return Profilev2(row[uuid]) {
            loadData(this, row)
        }
    }

    fun profileExists(uuid: UUID) = select { Profiles.uuid eq uuid }.any()

    /*fun registerExtensionLoader(loader: Profiles.(ResultRow) -> Unit) {
        extensionLoaders.add(loader)
    }*/

    fun writeToDatabase(profile: Profilev2) {
        transaction {
            if (profileExists(profile.uuid)) {
                update({ uuid eq profile.uuid }) {
                    saveProfile(profile, it)
                }
            } else {
                insert {
                    saveProfile(profile, it)
                }
            }
        }
    }

    fun loadAllProfiles() =
        transaction {
            selectAll().map { toProfile(it) }
        }


    fun loadFromDatabase(profile: Profilev2) {
        transaction {
            val resultRow = select { uuid eq profile.uuid }.firstOrNull()
                ?: throw IllegalStateException("Profilev2 with uuid ${profile.uuid} not found in database")
            loadData(profile, resultRow)
            /*extensionLoaders.forEach {
                it(this@Profiles, resultRow)
            }*/
        }
    }

    private fun loadData(profile: Profilev2, row: ResultRow) {
        profile.apply {
            name = row[Profiles.name]
            rawNickname = row[Profiles.nickname]
            lastOnline = row[Profiles.lastOnline]
            timesJoined = row[Profiles.timesJoined]
            timesWarned = row[Profiles.timesWarned]
            timesMuted = row[Profiles.timesMuted]
            timesBanned = row[Profiles.timesBanned]
            timesIpBanned = row[Profiles.timesIpBanned]
            playtime = row[Profiles.playtime]
            isAfk = row[afk]
            isGodmode = row[godmode]
            isFly = row[fly]
            isMuted = row[muted]
            isDefaultmuted = row[defaultmuted]
            isVanished = row[vanished]
            receiveLog = row[Profiles.receiveLog]
            bypassLockdown = row[Profiles.bypassLockdown]
            receiveSocialspy = row[Profiles.receiveSocialspy]
            editSign = row[Profiles.editSign]
            colorNick = ChatColor.valueOf(row[Profiles.colorNick])
        }
    }

    private fun saveProfile(profile: Profilev2, updateBuilder: UpdateBuilder<Int>) {
        saveData(profile, updateBuilder)
        saveManagerData(profile, updateBuilder)
    }

    private fun saveData(profile: Profilev2, updateBuilder: UpdateBuilder<Int>) {
        profile.apply {
            if (updateBuilder.type == StatementType.INSERT)
                updateBuilder[Profiles.uuid] = uuid
            updateBuilder[Profiles.name] = name
            updateBuilder[Profiles.nickname] = rawNickname
            updateBuilder[Profiles.lastOnline] = lastOnline
            updateBuilder[Profiles.timesJoined] = timesJoined
            updateBuilder[Profiles.timesWarned] = timesWarned
            updateBuilder[Profiles.timesMuted] = timesMuted
            updateBuilder[Profiles.timesBanned] = timesBanned
            updateBuilder[Profiles.timesIpBanned] = timesIpBanned
            updateBuilder[Profiles.playtime] = playtime
            updateBuilder[afk] = isAfk
            updateBuilder[godmode] = isGodmode
            updateBuilder[fly] = isFly
            updateBuilder[muted] = isMuted
            updateBuilder[defaultmuted] = isDefaultmuted
            updateBuilder[vanished] = isVanished
            updateBuilder[Profiles.receiveLog] = receiveLog
            updateBuilder[Profiles.bypassLockdown] = bypassLockdown
            updateBuilder[Profiles.receiveSocialspy] = receiveSocialspy
            updateBuilder[Profiles.editSign] = editSign
            updateBuilder[Profiles.colorNick] = colorNick.name

            updateBuilder[Profiles.prefix] = displayManager.prefix
        }
    }

    private fun saveManagerData(profile: Profilev2, updateBuilder: UpdateBuilder<Int>) {
        profile.apply {
            homeManager.writeToDatabase()
            warnManager.writeToDatabase()
            effectManager.writeToDatabase()
            displayManager.writeToDatabase()
            locationManager.writeToDatabase()
            languageManager.writeToDatabase()
        }
    }

}