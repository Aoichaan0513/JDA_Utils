package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.JDA_Utils.Commons.ComponentEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent
import net.dv8tion.jda.api.events.interaction.GenericComponentInteractionCreateEvent
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.components.Button
import net.dv8tion.jda.api.interactions.components.ButtonStyle
import net.dv8tion.jda.api.sharding.ShardManager
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

inline fun <reified T : GenericComponentInteractionCreateEvent> JDA.onComponent(
    customId: String,
    crossinline consumer: ComponentEventListener.(T) -> Unit
) = listener<T> {
    if (it.componentId == customId)
        consumer(it)
}

inline fun <reified T : GenericComponentInteractionCreateEvent> ShardManager.onComponent(
    customId: String,
    crossinline consumer: ComponentEventListener.(T) -> Unit
) = listener<T> {
    if (it.componentId == customId)
        consumer(it)
}

inline fun JDA.onCommand(name: String, crossinline consumer: ComponentEventListener.(SlashCommandEvent) -> Unit) =
    listener<SlashCommandEvent> {
        if (it.name == name)
            consumer(it)
    }

inline fun ShardManager.onCommand(
    name: String,
    crossinline consumer: ComponentEventListener.(SlashCommandEvent) -> Unit
) = listener<SlashCommandEvent> {
    if (it.name == name)
        consumer(it)
}

inline fun JDA.onButton(id: String, crossinline consumer: ComponentEventListener.(ButtonClickEvent) -> Unit) =
    onComponent(id, consumer)

inline fun ShardManager.onButton(id: String, crossinline consumer: ComponentEventListener.(ButtonClickEvent) -> Unit) =
    onComponent(id, consumer)

inline fun JDA.onSelection(id: String, crossinline consumer: ComponentEventListener.(SelectionMenuEvent) -> Unit) =
    onComponent(id, consumer)

inline fun ShardManager.onSelection(
    id: String,
    crossinline consumer: ComponentEventListener.(SelectionMenuEvent) -> Unit
) =
    onComponent(id, consumer)


suspend inline fun JDA.button(
    style: ButtonStyle = ButtonStyle.SECONDARY,
    label: String? = null,
    emoji: Emoji? = null,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(ButtonClickEvent) -> Unit
): Button {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val button = Button.of(style, id, label, emoji)

    val task = onButton(id) {
        if (user == null || user == it.user)
            listener(it)

        if (!it.isAcknowledged)
            it.deferEdit().queue()
    }

    if (expiration > 0) {
        GlobalScope.launch {
            delay(expiration)
            removeEventListener(task)
        }
    }

    return button
}

suspend inline fun ShardManager.button(
    style: ButtonStyle = ButtonStyle.SECONDARY,
    label: String? = null,
    emoji: Emoji? = null,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(ButtonClickEvent) -> Unit
): Button {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val button = Button.of(style, id, label, emoji)

    val task = onButton(id) {
        if (user == null || user == it.user)
            listener(it)

        if (!it.isAcknowledged)
            it.deferEdit().queue()
    }

    if (expiration > 0) {
        GlobalScope.launch {
            delay(expiration)
            removeEventListener(task)
        }
    }

    return button
}