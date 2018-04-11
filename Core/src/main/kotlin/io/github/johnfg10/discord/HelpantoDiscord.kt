package io.github.johnfg10.discord

import io.github.johnfg10.discord.command.CommandManager
import io.github.johnfg10.discord.filter.FilterManager
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.util.DiscordException
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent


//handles any DiscordConfig related functionality
class HelpantoDiscord(val discordToken: String) {
    val discordClient: IDiscordClient

    init {
        discordClient = createClient(discordToken, true) ?: throw IllegalStateException("IDiscordClient was null this is not allowed in this context")

        val dispatcher = discordClient.dispatcher
        dispatcher.registerListener(cmdManager)
        dispatcher.registerListener(filterManager)

    }

    @EventSubscriber
    fun test(msgEvent: MessageReceivedEvent){
        println(msgEvent.message.content + " testsy")
    }

    fun createClient(token: String, login: Boolean): IDiscordClient? { // Returns a new instance of the Discord client
        val clientBuilder = ClientBuilder() // Creates the ClientBuilder instance
        clientBuilder.withToken(token) // Adds the login info to the builder
        return try {
            if (login) {
                clientBuilder.login() // Creates the client instance and logs the client in
            } else {
                clientBuilder.build() // Creates the client instance but it doesn't log the client in yet, you would have to call client.login() yourself
            }
        } catch (e: DiscordException) { // This is thrown if there was a problem building the client
            e.printStackTrace()
            null
        }

    }

    companion object {
        val cmdManager: CommandManager = CommandManager()
        val filterManager: FilterManager = FilterManager()
    }

}