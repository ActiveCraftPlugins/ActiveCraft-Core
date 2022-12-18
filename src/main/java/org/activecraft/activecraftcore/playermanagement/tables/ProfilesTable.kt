package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.ChatColor
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

object ProfilesTable : Table("profiles") {

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

    fun profileExists(uuid: UUID) = select { ProfilesTable.uuid eq uuid }.any()

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
            name = row[ProfilesTable.name]
            rawNickname = row[ProfilesTable.nickname]
            lastOnline = row[ProfilesTable.lastOnline]
            timesJoined = row[ProfilesTable.timesJoined]
            timesWarned = row[ProfilesTable.timesWarned]
            timesMuted = row[ProfilesTable.timesMuted]
            timesBanned = row[ProfilesTable.timesBanned]
            timesIpBanned = row[ProfilesTable.timesIpBanned]
            playtime = row[ProfilesTable.playtime]
            isAfk = row[afk]
            isGodmode = row[godmode]
            isFly = row[fly]
            isMuted = row[muted]
            isDefaultmuted = row[defaultmuted]
            isVanished = row[vanished]
            receiveLog = row[ProfilesTable.receiveLog]
            bypassLockdown = row[ProfilesTable.bypassLockdown]
            receiveSocialspy = row[ProfilesTable.receiveSocialspy]
            editSign = row[ProfilesTable.editSign]
            colorNick = ChatColor.valueOf(row[ProfilesTable.colorNick])
        }
    }

    private fun saveProfile(profile: Profilev2, updateBuilder: UpdateBuilder<Int>) {
        saveData(profile, updateBuilder)
        saveManagerData(profile, updateBuilder)
    }

    private fun saveData(profile: Profilev2, updateBuilder: UpdateBuilder<Int>) {
        profile.apply {
            if (updateBuilder.type == StatementType.INSERT)
                updateBuilder[ProfilesTable.uuid] = uuid
            updateBuilder[ProfilesTable.name] = name
            updateBuilder[ProfilesTable.nickname] = rawNickname
            updateBuilder[ProfilesTable.lastOnline] = lastOnline
            updateBuilder[ProfilesTable.timesJoined] = timesJoined
            updateBuilder[ProfilesTable.timesWarned] = timesWarned
            updateBuilder[ProfilesTable.timesMuted] = timesMuted
            updateBuilder[ProfilesTable.timesBanned] = timesBanned
            updateBuilder[ProfilesTable.timesIpBanned] = timesIpBanned
            updateBuilder[ProfilesTable.playtime] = playtime
            updateBuilder[afk] = isAfk
            updateBuilder[godmode] = isGodmode
            updateBuilder[fly] = isFly
            updateBuilder[muted] = isMuted
            updateBuilder[defaultmuted] = isDefaultmuted
            updateBuilder[vanished] = isVanished
            updateBuilder[ProfilesTable.receiveLog] = receiveLog
            updateBuilder[ProfilesTable.bypassLockdown] = bypassLockdown
            updateBuilder[ProfilesTable.receiveSocialspy] = receiveSocialspy
            updateBuilder[ProfilesTable.editSign] = editSign
            updateBuilder[ProfilesTable.colorNick] = colorNick.name

            updateBuilder[ProfilesTable.prefix] = displayManager.prefix
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