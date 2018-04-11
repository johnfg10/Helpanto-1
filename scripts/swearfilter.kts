import java.io.File
import java.io.FileReader

//list of swear words thats filtered
var bannedWords = listOf(
    "fuck", "shit", "bastard", "cunt", "twat", "wanker"
)

fun isAllowed(message: String) : Boolean {
    val file = File("");
    println(file.absoluteFile)
    if(File("${file.absoluteFile}/scripts/swearword.txt").exists()){
        val texts = File("${file.absoluteFile}/scripts/swearword.txt").readLines()
        bannedWords = texts
    }

    for(bannedWord in bannedWords){
        if(message.contains(bannedWord)){
            return false
        }
    }

    return true
}
