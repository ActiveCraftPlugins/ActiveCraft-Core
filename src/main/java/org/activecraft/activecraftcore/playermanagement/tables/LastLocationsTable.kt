package org.activecraft.activecraftcore.playermanagement.tables

import org.activecraft.activecraftcore.playermanagement.Profilev2
import org.bukkit.Location
import org.bukkit.World
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object LastLocationsTable : Table("last_locations") {

    val id = integer("id").autoIncrement()
    val profileId = uuid("profile_id") references ProfilesTable.uuid
    val worldname = text("world")
    val locationId = varchar("location_id", 8) references LocationsTable.id
    val lastBeforeQuit = bool("last_before_quit")

    override val primaryKey = PrimaryKey(id, name = "last_locations_pk")

    fun saveLastLocation(profile: Profilev2, world: World, lastLocation: Location, lastBeforeQuit: Boolean) {
        fun writeLastLocation(updateBuilder: UpdateBuilder<Int>, oldLastLocationId: String?) {
            if (updateBuilder.type == StatementType.INSERT) {
                updateBuilder[profileId] = profile.uuid
                updateBuilder[worldname] = world.name
            }
            updateBuilder[locationId] = LocationsTable.saveLocation(lastLocation, oldLastLocationId)
            updateBuilder[LastLocationsTable.lastBeforeQuit] = lastBeforeQuit
        }

        transaction {
            val where = (worldname eq world.name) and (profileId eq profile.uuid)
            val oldLastLocationId: String? =
                select { where }.map { it[locationId] }
                    .firstOrNull()
            if (oldLastLocationId == null) {
                insert {
                    writeLastLocation(it, null)
                }
            } else {
                update({ where }) {
                    writeLastLocation(it, oldLastLocationId)
                }
            }
        }
    }

    fun getLastLocationsForProfile(profile: Profilev2) =
        transaction {
            select { profileId eq profile.uuid }
                .mapNotNull { LocationsTable.locationFromId(it[locationId]) }
                .associateBy { it.world }
        }

    fun getLastLocationBeforeQuitForProfile(profile: Profilev2) =
        transaction {
            select { (profileId eq profile.uuid) and (lastBeforeQuit eq true) }
                .map { LocationsTable.locationFromId(it[locationId]) }
                .firstOrNull()
        }

}