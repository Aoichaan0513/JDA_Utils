package com.github.aoichaan0513.utils.jda

import com.github.aoichaan0513.utils.jda.commons.ComponentEventListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu.SelectTarget
import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
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

inline fun JDA.onButton(
    id: String,
    crossinline consumer: ComponentEventListener.(ButtonInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun ShardManager.onButton(
    id: String,
    crossinline consumer: ComponentEventListener.(ButtonInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun JDA.onStringSelectMenu(
    id: String,
    crossinline consumer: ComponentEventListener.(StringSelectInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun ShardManager.onStringSelectMenu(
    id: String,
    crossinline consumer: ComponentEventListener.(StringSelectInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun JDA.onEntitySelectMenu(
    id: String,
    crossinline consumer: ComponentEventListener.(EntitySelectInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun ShardManager.onEntitySelectMenu(
    id: String,
    crossinline consumer: ComponentEventListener.(EntitySelectInteractionEvent) -> Unit
) = onComponent(id, consumer)

inline fun JDA.onModal(
    id: String,
    crossinline consumer: ComponentEventListener.(ModalInteractionEvent) -> Unit
) = listener<ModalInteractionEvent> {
    if (it.modalId == id)
        consumer(it)
}

inline fun ShardManager.onModal(
    id: String,
    crossinline consumer: ComponentEventListener.(ModalInteractionEvent) -> Unit
) = listener<ModalInteractionEvent> {
    if (it.modalId == id)
        consumer(it)
}

@OptIn(DelicateCoroutinesApi::class)
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

@OptIn(DelicateCoroutinesApi::class)
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


@OptIn(DelicateCoroutinesApi::class)
suspend inline fun JDA.stringSelectMenu(
    options: Collection<SelectOption> = emptyList(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(StringSelectInteractionEvent) -> Unit
): StringSelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = StringSelectMenu.create(id).apply {
        addOptions(options)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onStringSelectMenu(id) {
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

suspend inline fun JDA.stringSelectMenu(
    options: Array<SelectOption> = emptyArray(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(StringSelectInteractionEvent) -> Unit
) = stringSelectMenu(options.toList(), placeholder, minValue, maxValue, expiration, user, listener)

@OptIn(DelicateCoroutinesApi::class)
suspend inline fun ShardManager.stringSelectMenu(
    options: Collection<SelectOption> = emptyList(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(StringSelectInteractionEvent) -> Unit
): StringSelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = StringSelectMenu.create(id).apply {
        addOptions(options)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onStringSelectMenu(id) {
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

suspend inline fun ShardManager.stringSelectMenu(
    options: Array<SelectOption> = emptyArray(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(StringSelectInteractionEvent) -> Unit
) = stringSelectMenu(options.toList(), placeholder, minValue, maxValue, expiration, user, listener)


@OptIn(DelicateCoroutinesApi::class)
suspend inline fun JDA.entitySelectMenu(
    targets: Collection<SelectTarget> = emptyList(),
    types: Collection<ChannelType> = emptyList(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(EntitySelectInteractionEvent) -> Unit
): EntitySelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = EntitySelectMenu.create(id, targets).apply {
        setChannelTypes(types)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onEntitySelectMenu(id) {
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

suspend inline fun JDA.entitySelectMenu(
    targets: Array<SelectTarget> = emptyArray(),
    types: Array<ChannelType> = emptyArray(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(EntitySelectInteractionEvent) -> Unit
) = entitySelectMenu(targets.toList(), types.toList(), placeholder, minValue, maxValue, expiration, user, listener)

@OptIn(DelicateCoroutinesApi::class)
suspend inline fun ShardManager.entitySelectMenu(
    targets: Collection<SelectTarget> = emptyList(),
    types: Collection<ChannelType> = emptyList(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(EntitySelectInteractionEvent) -> Unit
): EntitySelectMenu {
    val id = ThreadLocalRandom.current().nextLong().toString()
    val selectMenu = EntitySelectMenu.create(id, targets).apply {
        setChannelTypes(types)
        this.placeholder = placeholder
        setRequiredRange(minValue, maxValue)
    }.build()

    val task = onEntitySelectMenu(id) {
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

suspend inline fun ShardManager.entitySelectMenu(
    targets: Array<SelectTarget> = emptyArray(),
    types: Array<ChannelType> = emptyArray(),
    placeholder: String? = null,
    minValue: Int = 1,
    maxValue: Int = 1,
    expiration: Long = TimeUnit.MINUTES.toMillis(15),
    user: User? = null,
    crossinline listener: ComponentEventListener.(EntitySelectInteractionEvent) -> Unit
) = entitySelectMenu(targets.toList(), types.toList(), placeholder, minValue, maxValue, expiration, user, listener)