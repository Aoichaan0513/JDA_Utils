package com.github.aoichaan0513.utils.jda.commons

import net.dv8tion.jda.api.hooks.EventListener

interface ComponentEventListener : EventListener {

    fun cancel()
}