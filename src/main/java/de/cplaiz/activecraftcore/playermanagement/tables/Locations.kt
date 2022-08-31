package de.cplaiz.activecraftcore.playermanagement.tables

import de.cplaiz.activecraftcore.utils.CharPool
import de.cplaiz.activecraftcore.utils.generateRandomString
import org.bukkit.Bukkit
import org.bukkit.Location
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.StatementType
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction

object Locations : Table("locations") {

    val id = varchar("location_id", 8)
    val world = text("world")
    val x = double("x")
    val y = double("y")
    val z = double("z")
    val yaw = float("yaw")
    val pitch = float("pitch")

    override val primaryKey = PrimaryKey(id, name = "locations_pk")

    fun toLocation(row: ResultRow) =
        Location(Bukkit.getWorld(row[world]), row[x], row[y], row[z], row[yaw], row[pitch])

    fun locationIdInDatabase(locationId: String) = transaction { select { Locations.id eq locationId }.any() }

    fun saveLocation(location: Location, locationId: String? = null): String {
        var locId = locationId;
        fun writeLocation(updateBuilder: UpdateBuilder<Int>) {
            if (updateBuilder.type == StatementType.INSERT) {
                locId = generateRandomString(8, CharPool.DigitsAndLowercaseLetters)
                updateBuilder[id] = locId!!
            }
            updateBuilder[world] = location.world.name
            updateBuilder[x] = location.x
            updateBuilder[y] = location.y
            updateBuilder[z] = location.z
            updateBuilder[yaw] = location.yaw
            updateBuilder[pitch] = location.pitch
        }
        transaction {
            if (locationId != null && locationIdInDatabase(locationId)) {
                update({ Locations.id eq locationId }) {
                    locId = locationId;
                    writeLocation(it)
                }
            } else {
                insert {
                    writeLocation(it)
                }
            }
        }
        return locId!!;
    }

    fun deleteLocation(locationId: String) {
        transaction {
            deleteWhere { Locations.id eq locationId }
        }
    }
    
    fun locationFromId(id: String): Location? {
        val resultRow = transaction { select { Locations.id eq id }.firstOrNull() } ?: return null
        return toLocation(resultRow)
    }

}