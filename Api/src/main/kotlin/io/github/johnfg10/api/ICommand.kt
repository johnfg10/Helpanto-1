package io.github.johnfg10.api

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

interface ICommand{
    fun execCommand(event: MessageReceivedEvent, args: List<String>)

    fun getCommandName() : String
}