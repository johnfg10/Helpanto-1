import java.io.File
import java.io.FileReader

//list of swear words thats filtered
var bannedWords = listOf(
    "fuck", "shit", "bastard", "cunt", "twat", "wanker"
)

var totalSpace: Long = 0

fun isAllowed(message: String) : Boolean {
    val mainFile = File("${File("").absoluteFile}/scripts/swearword.txt")

    if(mainFile.exists() && totalSpace != mainFile.totalSpace){
        val texts = mainFile.readLines()
        bannedWords = texts
    }

    for(bannedWord in bannedWords){
        if(message.contains(bannedWord)){
            return false
        }
    }

    return true
}
