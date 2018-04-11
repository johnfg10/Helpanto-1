import io.github.johnfg10.api.ICommand
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

fun execCommand(event: MessageReceivedEvent, args: List<String>) {
    event.channel.sendMessage("hey!")
}

fun getCommandName(): String {
    return "sayhey"
}
