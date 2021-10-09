package jp.aoichaan0513.JDA_Utils.Commons

import net.dv8tion.jda.api.hooks.EventListener

interface ComponentEventListener : EventListener {

    fun cancel()
}