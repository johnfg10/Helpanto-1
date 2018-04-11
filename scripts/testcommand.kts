import io.github.johnfg10.api.ICommand
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

fun execCommand(event: MessageReceivedEvent, args: List<String>) {
    event.message.reply("Hey!")
}

fun getCommandName(): String {
    return "test"
}
