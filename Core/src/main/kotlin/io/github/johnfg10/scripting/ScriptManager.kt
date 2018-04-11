package io.github.johnfg10.scripting

import io.github.johnfg10.api.ICommand
import io.github.johnfg10.api.IFilter
import io.github.johnfg10.discord.command.CommandManager
import io.github.johnfg10.discord.filter.FilterManager
import kotlinx.coroutines.experimental.launch
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import javax.script.Invocable


class ScriptManager(val scriptPath: String, private val isWatcherEnabled: Boolean = true) {

    data class ScriptStore(val scriptName: String, val scriptType: ScriptType)

    enum class ScriptType{
        ICommand,
        IFilter
    }

    val pathNames: MutableMap<String, ScriptStore> = mutableMapOf()
    val kotlinScriptFactory = KotlinJsr223JvmLocalScriptEngineFactory()

    init {
        File(scriptPath).mkdirs()

        logger.info("File watcher will be setup with the directory: $scriptPath")
        val file = File(scriptPath)

        file.walkTopDown().forEach {
            if(kotlinScriptFactory.extensions.contains(it.extension)){
                addScript(it.toPath())
            }
        }

        launch {
            val path = Paths.get(scriptPath)

            val watcher = path.fileSystem.newWatchService()
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE)

            while (isWatcherEnabled){
                val key = watcher.take()
                logger.info("watcher event found")
                key.pollEvents().forEach { it ->
                    val patha = it.context() as Path
                    when(it.kind().name()){
                        "ENTRY_CREATE" -> {
                            if(patha.toFile().extension == "kts")
                            addScript(path.resolve(patha.fileName))
                        }
                        "ENTRY_MODIFY" -> {
                            if(patha.toFile().extension == "kts")
                            updateScript(patha)
                        }
                        "ENTRY_DELETE" -> {
                            if(patha.toFile().extension == "kts")
                            removeScript(path.resolve(patha.fileName))
                        }
                    }
                }
                key.reset()
            }

/*            while (isWatcherEnabled){
                val key = watcher.take()
                logger.info("watcher event found")
                key.pollEvents().forEach { it ->
                    when(it.kind().name()){
                        "ENTRY_CREATE" -> {
                            logger.info(it.context().toString())
                            val patha = it.context() as Path
                            logger.info("creating new script: $patha")
                            val finalPath = scriptPath + File.separatorChar + patha.fileName
                            logger.info("final path: $finalPath")

                            val cmd = getCommand(finalPath)
                            if (cmd != null) {
                                CommandManager.addCommand(cmd)
                            } else
                                logger.error("$patha either does not exist, or is not of valid point or does not implement ICommand")
                        }
                        "ENTRY_MODIFY" -> {
                            val patha = (it.context() as Path)
                            val pathString = patha.toFile().absolutePath
                            if (cmdMap.containsKey(pathString)){
                                val name = cmdMap[pathString]!!
                                CommandManager.removeCommand(name)
                            }

                            val finalPath = scriptPath + File.separatorChar + patha.fileName

                            val cmd = getCommand(finalPath)
                            if (cmd != null) {
                                CommandManager.addCommand(cmd)
                            } else
                                logger.error("$patha either does not exist, or is not of valid point or does not implement ICommand")
                        }
                        "OVERFLOW" -> println("${it.context()} overflow")
                        "ENTRY_DELETE" -> {
                            logger.info("Removing script")
                            val patha = (it.context() as Path)
                            val pathString = patha.toFile().absolutePath
                            logger.info("Script path: $pathString")

                            cmdMap.forEach { t, u -> logger.info("$t : $u") }

                            if (cmdMap.containsKey(pathString)){
                                logger.info("script found in cmdMap")
                                val name = cmdMap[pathString]!!
                                CommandManager.removeCommand(name)
                            }
                        }
                    }
                }
                key.reset()
            }*/
        }
    }

    fun addScript(path: Path){
        val engine = kotlinScriptFactory.scriptEngine
        try {
            engine.eval(FileReader(path.toUri().toURL().file))
        }catch (e: FileNotFoundException){
            logger.debug("the file could not be found. ${e.message}")
        }

        val inv = engine as Invocable
        val scriptType = loadScript(inv)
        if (scriptType != null){
            when(scriptType){
                is ICommand -> {
                    pathNames[path.toFile().absolutePath] = ScriptStore(scriptType.getCommandName(), ScriptType.ICommand)
                    CommandManager.addCommand(scriptType)
                    logger.info("ICommand found")
                }
                is IFilter -> {
                    logger.info("IFilter found")
                    pathNames[path.toFile().absolutePath] = ScriptStore(path.toFile().nameWithoutExtension, ScriptType.IFilter)
                    FilterManager.addFilter(path.toFile().nameWithoutExtension, scriptType)
                }
            }
        }

        logger.info("the script could not be casted to a accepted interface")
    }

    fun updateScript(path: Path){
        if (pathNames.containsKey(path.toFile().absolutePath)){
            val pathName = pathNames[path.toFile().absolutePath]!!
            if(pathName.scriptType == ScriptType.ICommand) {
                val engine = kotlinScriptFactory.scriptEngine
                engine.eval(FileReader(path.toUri().toURL().file))
                val inv = engine as Invocable
                val scriptType = loadScript(inv)
                if (scriptType != null && scriptType == ICommand::class.java){
                    CommandManager.updateCommand(pathName.scriptName, scriptType as ICommand)
                }
            }else if(pathName.scriptType == ScriptType.IFilter){
                val engine = kotlinScriptFactory.scriptEngine
                engine.eval(FileReader(path.toUri().toURL().file))
                val inv = engine as Invocable
                val scriptType = loadScript(inv)
                if (scriptType != null && scriptType == IFilter::class.java){
                    FilterManager.addFilter(path.toFile().nameWithoutExtension, scriptType as IFilter)
                }

            }
        }else
            logger.debug("script reload was aborted as its not contained within pathNames map")
    }

    fun removeScript(path: Path){
        if (pathNames.containsKey(path.toFile().absolutePath)){
            val pathName = pathNames[path.toFile().absolutePath]!!
            //logger.debug("path found info: ${pathName.scriptName} : ${pathName.scriptType}")
            if(pathName.scriptType == ScriptType.ICommand)
                CommandManager.removeCommand(pathName.scriptName)
            else if (pathName.scriptType == ScriptType.IFilter)
                FilterManager.removeFilter(pathName.scriptName)

            pathNames.remove(path.toFile().absolutePath)
        }else
            logger.debug("script removal attempt aborted as it is not conatained withing the pathNames map")
    }

    fun loadScript(inv: Invocable) : Any?{
        try {
            val returnVal = inv.getInterface(ICommand::class.java)
            logger.info("command name: " + returnVal.getCommandName())
            return returnVal
        }catch (e: Exception){logger.debug(e.message)}

        try {
            val returnVal = inv.getInterface(IFilter::class.java)
            logger.info("command name: " + returnVal.isAllowed("tests"))
            return returnVal
        }catch (e: Exception){logger.debug(e.message)}

        return null
    }
    companion object {
        val logger = LoggerFactory.getLogger(ScriptManager::class.java)
    }
}
