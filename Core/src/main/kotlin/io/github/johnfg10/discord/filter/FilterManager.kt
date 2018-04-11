package io.github.johnfg10.discord.filter

import io.github.johnfg10.api.IFilter
import io.github.johnfg10.discord.command.CommandManager
import org.slf4j.LoggerFactory
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import java.util.concurrent.ConcurrentHashMap

class FilterManager {

    @EventSubscriber
    fun onMessageEvent(msgEvent: MessageReceivedEvent) {
        msgEvent.message.content.contains(Regex("fuck"))
        filterMap.forEach { t: String, u: IFilter ->
            logger.info("$t filtering")
            if(!u.isAllowed(msgEvent.message.formattedContent)){
                msgEvent.message.delete()
            }
        }
    }

    companion object {
        val filterMap = ConcurrentHashMap<String, IFilter>()

        val logger = LoggerFactory.getLogger(FilterManager::class.java)

        fun addFilter(name: String, filter: IFilter){
            if(filterMap.contains(filter))
                return

            filterMap[name] = filter
        }

        fun updateFilter(name: String, filter: IFilter){
            if (!CommandManager.cmdMap.contains(name)){
                throw  IllegalArgumentException("the given filter was not contained within the filter map")
            }
            filterMap[name] = filter
        }

        fun removeFilter(name: String){
            if (!CommandManager.cmdMap.containsKey(name)){
                return
            }
            CommandManager.cmdMap.remove(name)
        }
    }
}