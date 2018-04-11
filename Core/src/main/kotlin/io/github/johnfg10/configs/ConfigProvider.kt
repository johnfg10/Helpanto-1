package io.github.johnfg10.configs

import com.uchuhimo.konf.Config
import io.github.johnfg10.configs.models.DatabaseConfig
import io.github.johnfg10.configs.models.DiscordConfig
import java.io.File

class ConfigProvider {
    fun getConfig() : Config {
        val config = File("./.config.conf")
        if (!config.exists()) {
            config.createNewFile()
            config.writeText(this::class.java.classLoader.getResource("defaultconfig.conf").readText())
        }
        return Config { addSpec(DatabaseConfig); addSpec(DiscordConfig) }
                .withSourceFrom.hocon.file(config)
                .withSourceFrom.env()
                .withSourceFrom.systemProperties()
    }
}