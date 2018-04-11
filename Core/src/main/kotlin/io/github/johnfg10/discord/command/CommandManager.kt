package io.github.johnfg10.discord.command

import io.github.johnfg10.api.ICommand
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class CommandManager(private val cmdPrefix: String = "!") {
    init {
        println("Command prefix is: $cmdPrefix")
    }

    @EventSubscriber
    fun onMessageEvent(msgEvent: MessageReceivedEvent){
        val msgSplit = msgEvent.message.content.split(" ").toMutableList()

        if (msgSplit.isEmpty()) return

        if (!msgSplit[0].startsWith(cmdPrefix)) return

        val replacedString = msgSplit.removeAt(0).replace(cmdPrefix, "")

        if (replacedString == "cmds"){
            cmdMap.forEach {
                t: String, u: ICommand -> msgEvent.channel.sendMessage("Command found: $t, $u")

            }
            return
        }

        if (cmdMap.containsKey(replacedString))
            cmdMap[replacedString]!!.execCommand(event = msgEvent, args = msgSplit)

    }

    companion object {
        val cmdMap = ConcurrentHashMap<String, ICommand>()

        init {
        }

        fun addCommand(command: ICommand){
            if (cmdMap.contains(command.getCommandName())){
                return
            }

            cmdMap[command.getCommandName()] = command
        }

        fun removeCommand(cmdName: String){
            if (!cmdMap.containsKey(cmdName)){
                return
            }
            cmdMap.remove(cmdName)
        }

        fun updateCommand(oldCommand: String, newCommand: ICommand){
            if (!cmdMap.contains(oldCommand)){
                throw  IllegalArgumentException("the given command was not contained within the command map")
            }

            cmdMap[oldCommand] = newCommand
        }
    }
}