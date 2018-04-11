package io.github.johnfg10.configs.models

import com.uchuhimo.konf.ConfigSpec

object DiscordConfig : ConfigSpec("discord") {
    val token by required<String>()
}