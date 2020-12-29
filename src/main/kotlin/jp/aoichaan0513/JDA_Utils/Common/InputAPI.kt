package jp.aoichaan0513.YudzukiBot.API.Commons

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.sharding.ShardManager
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

abstract class InputAPI(val shardManager: ShardManager, val isStart: Boolean = true, val timeOut: Long = 3, val timeUnit: TimeUnit = TimeUnit.MINUTES, var timeOutAction: Runnable = Runnable { }) : EventListener {

    val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    var scheduledFuture: ScheduledFuture<*>? = null

    private val instance = this

    var isAddedEventListener = false
        private set

    init {
        if (isStart)
            start()
    }

    open fun start() {
        if (scheduledExecutorService.isShutdown || timeOut < 1) return

        if (!isAddedEventListener) {
            shardManager.addEventListener(instance)
            isAddedEventListener = true
        }

        scheduledFuture?.cancel(true)
        scheduledFuture = scheduledExecutorService.schedule({
            shardManager.removeEventListener(instance)
            timeOutAction.run()
        }, timeOut, timeUnit)
    }

    open fun stop() {
        if (isAddedEventListener) {
            shardManager.removeEventListener(instance)
            isAddedEventListener = false
        }

        scheduledExecutorService.shutdown()
    }

    open fun stopNow() {
        if (isAddedEventListener) {
            shardManager.removeEventListener(instance)
            isAddedEventListener = false
        }

        scheduledExecutorService.shutdownNow()
    }

    abstract fun onMessageEvent(e: GenericMessageEvent)
    abstract fun onMessageReactionEvent(e: GenericMessageReactionEvent)

    override fun onEvent(e: GenericEvent) {
        if (e is GenericMessageReactionEvent)
            onMessageReactionEvent(e)
        else if (e is GenericMessageEvent)
            onMessageEvent(e)
        else if (e is ShutdownEvent)
            stop()
    }
}