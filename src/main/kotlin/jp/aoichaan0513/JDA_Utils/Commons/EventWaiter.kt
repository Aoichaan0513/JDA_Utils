package jp.aoichaan0513.JDA_Utils.Commons

import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.sharding.ShardManager
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

abstract class EventWaiter(
    val shardManager: ShardManager,
    val isStart: Boolean = true,
    val timeOut: Long = 3,
    val timeUnit: TimeUnit = TimeUnit.MINUTES,
    var timeOutAction: Runnable = Runnable { }
) : ListenerAdapter() {

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

    override fun onShutdown(e: ShutdownEvent) {
        stop()
    }
}