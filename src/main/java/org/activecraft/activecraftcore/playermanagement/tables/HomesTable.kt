package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Home
import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object HomesTable : Table("homes") {

    var homeId = integer("id").autoIncrement()
    var name = varchar("name", 256)
    var ownerId = uuid("owner_id") references ProfilesTable.uuid
    var locationId = varchar("location_id", 8) references LocationsTable.id

    override val primaryKey = PrimaryKey(homeId, name = "homes_pk")

    fun toHome(row: ResultRow): Home? {
        return Home(name = row[name], location = LocationsTable.locationFromId(row[locationId]) ?: return null)
    }

    fun saveHome(profile: Profilev2, home: Home) {
        fun writeHome(updateBuilder: UpdateBuilder<Int>) {
            if (updateBuilder.type == StatementType.INSERT) {
                updateBuilder[HomesTable.ownerId] = profile.uuid
                updateBuilder[HomesTable.locationId] = LocationsTable.saveLocation(home.location)
            }
            updateBuilder[HomesTable.name] = home.name
        }

        transaction {
            if (homeExistsInDatabase(profile, home)) {
                update({ (ownerId eq profile.uuid) and (name eq home.name) }) {
                    writeHome(it)
                }
            } else {
                insert {
                    writeHome(it)
                }
            }
        }
    }

    fun deleteHome(profile: Profilev2, homeName: String) {
        transaction {
            deleteWhere { (ownerId eq profile.uuid) and (name eq homeName) }
        }
    }

    fun homeExistsInDatabase(profile: Profilev2, home: Home) = getHomesForProfile(profile).any { it.name == home.name }

    fun getHomesForProfile(profile: Profilev2) =
        transaction {
            select { ownerId eq profile.uuid }
                .mapNotNull { toHome(it) }
        }
}