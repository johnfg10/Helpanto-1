package io.github.johnfg10.db.models

import org.dizitart.no2.IndexType
import org.dizitart.no2.objects.Id
import org.dizitart.no2.objects.Index
import org.dizitart.no2.objects.Indices
import java.io.Serializable

@Indices(Index("guildId", type = IndexType.Fulltext), Index("guildId", type = IndexType.Unique))
data class GuildSettings(
        @Id var id: String? = null,
        var guildId: String? = null
) : Serializable

fun guildSettings(block: GuildSettings.() -> Unit) : GuildSettings = GuildSettings().apply(block)