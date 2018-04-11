package io.github.johnfg10.api

import sx.blah.discord.handle.impl.obj.Message
import sx.blah.discord.handle.obj.IMessage

interface IFilter {
    fun isAllowed(message: String) : Boolean
}