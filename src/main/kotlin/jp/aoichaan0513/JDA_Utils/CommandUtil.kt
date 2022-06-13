package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.JDA_Utils.Commons.ComponentEventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.sharding.ShardManager

val OptionMapping.asInteger: Int
    get() {
        check(type == OptionType.INTEGER) { "Cannot convert option of type $type to integer" }
        return asString.toInt()
    }


inline fun JDA.onCommand(
    name: String,
    crossinline consumer: ComponentEventListener.(SlashCommandInteractionEvent) -> Unit
) = listener<SlashCommandInteractionEvent> {
    if (it.name == name)
        consumer(it)
}

inline fun ShardManager.onCommand(
    name: String,
    crossinline consumer: ComponentEventListener.(SlashCommandInteractionEvent) -> Unit
) = listener<SlashCommandInteractionEvent> {
    if (it.name == name)
        consumer(it)
}

inline fun JDA.onAutoComplete(
    name: String,
    crossinline consumer: ComponentEventListener.(CommandAutoCompleteInteractionEvent) -> Unit
) = listener<CommandAutoCompleteInteractionEvent> {
    if (it.name == name)
        consumer(it)
}

inline fun ShardManager.onAutoComplete(
    name: String,
    crossinline consumer: ComponentEventListener.(CommandAutoCompleteInteractionEvent) -> Unit
) = listener<CommandAutoCompleteInteractionEvent> {
    if (it.name == name)
        consumer(it)
}


inline fun JDA.onMessageContext(
    name: String,
    crossinline consumer: ComponentEventListener.(MessageContextInteractionEvent) -> Unit
) = listener<MessageContextInteractionEvent> {
    if (it.name == name)
        consumer(it)
}

inline fun ShardManager.onMessageContext(
    name: String,
    crossinline consumer: ComponentEventListener.(MessageContextInteractionEvent) -> Unit
) = listener<MessageContextInteractionEvent> {
    if (it.name == name)
        consumer(it)
}

inline fun JDA.onUserContext(
    name: String,
    crossinline consumer: ComponentEventListener.(UserContextInteractionEvent) -> Unit
) = listener<UserContextInteractionEvent> {
    if (it.name == name)
        consumer(it)
}

inline fun ShardManager.onUserContext(
    name: String,
    crossinline consumer: ComponentEventListener.(UserContextInteractionEvent) -> Unit
) = listener<UserContextInteractionEvent> {
    if (it.name == name)
        consumer(it)
}