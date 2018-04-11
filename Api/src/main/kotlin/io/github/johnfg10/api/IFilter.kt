package io.github.johnfg10.api

import sx.blah.discord.handle.impl.obj.Message
import sx.blah.discord.handle.obj.IMessage

/**
 * Repusents a filter in the internal system
 */
interface IFilter {
    /**
     * is called on every message
     * @param message the message to filter
     * @return returns true if the message is permitted and false otherwise
     */
    fun isAllowed(message: String) : Boolean
}