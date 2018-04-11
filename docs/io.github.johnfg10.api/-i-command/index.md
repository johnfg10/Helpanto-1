[Api](../../index.md) / [io.github.johnfg10.api](../index.md) / [ICommand](./index.md)

# ICommand

`interface ICommand`

by implementing the methods in this class a script will be considered as a "command" and will be treated as such internally

### Functions

| Name | Summary |
|---|---|
| [execCommand](exec-command.md) | `abstract fun execCommand(event: MessageReceivedEvent, args: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>called when a "command" is called |
| [getCommandName](get-command-name.md) | `abstract fun getCommandName(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>the name used by the internal system to recognise this command |
