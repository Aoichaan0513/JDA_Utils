package jp.aoichaan0513.JDA_Utils

import jp.aoichaan0513.JDA_Utils.Commons.ComponentEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Emoji
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
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

inline fun JDA.onButton(
    id: String,
    crossinline consumer: ComponentEventListener.(ButtonInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun ShardManager.onButton(
    id: String,
    crossinline consumer: ComponentEventListener.(ButtonInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun JDA.onSelectMenu(
    id: String,
    crossinline consumer: ComponentEventListener.(SelectMenuInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun ShardManager.onSelectMenu(
    id: String,
    crossinline consumer: ComponentEventListener.(SelectMenuInteractionEvent) -> Unit
) = onComponent(id, consumer)


suspend inline fun JDA.button(
    style: ButtonStyle = ButtonStyle.SECONDARY,
    label: String? = null,
    emoji: Emoji? = null,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(ButtonInteractionEvent) -> Unit
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
    crossinline listener: ComponentEventListener.(ButtonInteractionEvent) -> Unit
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


suspend inline fun JDA.selectMenu(
    options: Collection<SelectOption> = emptyList(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(SelectMenuInteractionEvent) -> Unit
): SelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = SelectMenu.create(id).apply {
        addOptions(options)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onSelectMenu(id) {
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

    return selectMenu
}

suspend inline fun JDA.selectMenu(
    options: Array<SelectOption> = emptyArray(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(SelectMenuInteractionEvent) -> Unit
): SelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = SelectMenu.create(id).apply {
        addOptions(*options)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onSelectMenu(id) {
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

    return selectMenu
}

suspend inline fun ShardManager.selectMenu(
    options: Collection<SelectOption> = emptyList(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(SelectMenuInteractionEvent) -> Unit
): SelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = SelectMenu.create(id).apply {
        addOptions(options)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onSelectMenu(id) {
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

    return selectMenu
}

suspend inline fun ShardManager.selectMenu(
    options: Array<SelectOption> = emptyArray(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(SelectMenuInteractionEvent) -> Unit
): SelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = SelectMenu.create(id).apply {
        addOptions(*options)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onSelectMenu(id) {
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

    return selectMenu
}