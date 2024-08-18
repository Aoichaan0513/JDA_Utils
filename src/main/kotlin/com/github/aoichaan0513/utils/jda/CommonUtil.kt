package com.github.aoichaan0513.utils.jda

import com.github.aoichaan0513.utils.jda.commons.ComponentEventListener
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.sharding.ShardManager

inline fun <reified T : GenericEvent> JDA.listener(
    crossinline consumer: ComponentEventListener.(T) -> Unit
) = object : ComponentEventListener {

    override fun onEvent(event: GenericEvent) {
        if (event is T)
            consumer(event)
    }

    override fun cancel() {
        return removeEventListener(this)
    }
}.also { addEventListener(it) }

inline fun <reified T : GenericEvent> ShardManager.listener(
    crossinline consumer: ComponentEventListener.(T) -> Unit
) = object : ComponentEventListener {

    override fun onEvent(event: GenericEvent) {
        if (event is T)
            consumer(event)
    }

    override fun cancel() {
        return removeEventListener(this)
    }
}.also { addEventListener(it) }