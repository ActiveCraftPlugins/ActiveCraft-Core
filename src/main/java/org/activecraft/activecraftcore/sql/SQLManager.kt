package org.activecraft.activecraftcore.sql

import org.activecraft.activecraftcore.ActiveCraftCore
import org.activecraft.activecraftcore.exceptions.StartupException
import org.activecraft.activecraftcore.playermanagement.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class SQLManager {


    lateinit var database: Database

    fun init() {
        // TODO: das ganze mit mysql oder so noch networkübergreifend machen
        // connect to sqlite database
        val mainConfig = ActiveCraftCore.getInstance().mainConfig
        val dialect: DatabaseDialect
        try {
            dialect = DatabaseDialect.valueOf(mainConfig.databaseDialect.uppercase())
        } catch (e: IllegalArgumentException) {
            throw StartupException("Invalid database dialect. Check your \"config.yml\"")
        }

        try {
            database = when (dialect) {
                DatabaseDialect.SQLITE -> Database.connect(
                    url = "jdbc:sqlite:${ActiveCraftCore.getInstance().dataFolder}/${mainConfig.databaseLocalPath}",
                    driver = "org.sqlite.JDBC"
                )

                DatabaseDialect.MYSQL -> Database.connect(
                    url = "jdbc:mysql://${mainConfig.databaseHost}:${mainConfig.databasePort}/${mainConfig.databaseNetworkPath}",
                    driver = "com.mysql.cj.jdbc.Driver",
                    user = mainConfig.databaseUser,
                    password = mainConfig.databasePassword
                )
            }
        } catch (e: Exception) {
            throw StartupException(e.message)
        }

        transaction {
            try {
                SchemaUtils.create(
                    Effects,
                    Homes,
                    LastLocations,
                    Locations,
                    PreferredLanguages,
                    ProfilesTable,
                    Tags,
                    Warns
                )
            } catch (e: Exception) {
                throw StartupException(e.message)
            }
        }
    }

}