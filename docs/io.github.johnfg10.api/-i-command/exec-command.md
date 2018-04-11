[Api](../../index.md) / [io.github.johnfg10.api](../index.md) / [ICommand](index.md) / [execCommand](./exec-command.md)

# execCommand

`abstract fun execCommand(event: MessageReceivedEvent, args: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

called when a "command" is called

### Parameters

`event` - the event of a matching message

`args` - a list of args excluding the matching argument and prefix