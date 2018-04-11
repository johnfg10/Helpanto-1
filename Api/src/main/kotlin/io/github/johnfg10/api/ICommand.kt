package io.github.johnfg10.api

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

/**
 * by implementing the methods in this class a script will be considered as a "command" and will be treated as such internally
 */
interface ICommand{
    /**
     * called when a "command" is called
     *
     * @param event the event of a matching message
     * @param args a list of args excluding the matching argument and prefix
     */
    fun execCommand(event: MessageReceivedEvent, args: List<String>)

    /**
     * the name used by the internal system to recognise this command
     * @return the name of the command which is matched to call @see execCommand
     */
    fun getCommandName() : String
}