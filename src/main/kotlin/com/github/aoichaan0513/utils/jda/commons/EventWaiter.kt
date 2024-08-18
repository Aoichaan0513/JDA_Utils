package com.github.aoichaan0513.utils.jda.commons

import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.sharding.ShardManager
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

abstract class EventWaiter(
    val shardManager: ShardManager,
    val isStart: Boolean = true,
    val timeOut: Long = 3,
    val timeUnit: TimeUnit = TimeUnit.MINUTES,
    var timeOutAction: Runnable = Runnable { }
) : ListenerAdapter() {

    val scheduledExecutorService: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    var scheduledFuture: ScheduledFuture<*>? = null

    var isAddedEventListener = false
        private set

    init {
        if (isStart)
            start()
    }

    open fun start() {
        if (scheduledExecutorService.isShutdown || timeOut < 1) return

        if (!isAddedEventListener) {
            shardManager.addEventListener(this)
            isAddedEventListener = true
        }

        scheduledFuture?.cancel(true)
        scheduledFuture = scheduledExecutorService.schedule({
            shardManager.removeEventListener(this)
            timeOutAction.run()
        }, timeOut, timeUnit)
    }

    open fun stop() {
        if (isAddedEventListener) {
            shardManager.removeEventListener(this)
            isAddedEventListener = false
        }

        scheduledExecutorService.shutdown()
    }

    open fun stopNow() {
        if (isAddedEventListener) {
            shardManager.removeEventListener(this)
            isAddedEventListener = false
        }

        scheduledExecutorService.shutdownNow()
    }

    override fun onShutdown(e: ShutdownEvent) {
        stop()
    }
}