package io.github.johnfg10

import com.uchuhimo.konf.Config
import io.github.johnfg10.configs.ConfigProvider
import io.github.johnfg10.configs.models.DiscordConfig
import io.github.johnfg10.discord.HelpantoDiscord
import io.github.johnfg10.scripting.ScriptManager
import org.slf4j.LoggerFactory

//handles the overall bot instance such as dbs, configs, scripting and the DiscordConfig bot
class Helpanto {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)

        init {
            logger.info("Logger initizalized")
        }

        val config = ConfigProvider().getConfig()
        val helpantoDiscord = HelpantoDiscord(config[DiscordConfig.token])
        val scriptManager = ScriptManager("./scripts")
    }

}