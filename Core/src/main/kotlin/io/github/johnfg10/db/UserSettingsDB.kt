package io.github.johnfg10.db

import io.github.johnfg10.db.models.GuildSettings
import org.dizitart.no2.Nitrite
import org.dizitart.no2.NitriteCollection
import org.dizitart.no2.objects.Cursor
import org.dizitart.no2.objects.ObjectFilter
import org.dizitart.no2.objects.ObjectRepository
import org.dizitart.no2.objects.filters.ObjectFilters.eq
import java.io.File

class UserSettingsDB(dbUsername: String, dbPassword: String) {

    val nitrite: Nitrite

    val nitriteCollection: ObjectRepository<GuildSettings>

    init {
        nitrite = Nitrite.builder().compressed().filePath(File("./DatabaseConfig.db")).openOrCreate(dbUsername, dbPassword)
        nitriteCollection = nitrite.getRepository(GuildSettings::class.java)
    }

    fun insert(vararg guildSettings: GuildSettings){
        nitriteCollection.insert(guildSettings)
    }

    fun remove(objectFilter: ObjectFilter){
        nitriteCollection.remove(objectFilter)
    }

    fun find(objectFilter: ObjectFilter) : Cursor<GuildSettings> {
        return nitriteCollection.find(objectFilter)
    }

}