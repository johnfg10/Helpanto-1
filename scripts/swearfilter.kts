//list of swear words thats filtered
val bannedWords = listOf(
    "fuck", "shit", "bastard", "cunt", "twat", "wanker"
)

fun isAllowed(message: String) : Boolean {
    for(bannedWord in bannedWords){
        if(message.contains(Regex(bannedWord))){
            return false
        }
    }

    return true
}
