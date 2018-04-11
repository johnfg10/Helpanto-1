package io.github.johnfg10.configs.models

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import java.io.File

object DatabaseConfig : ConfigSpec("DatabaseConfig") {
    val username by optional("root")
    val password by optional("password")
}