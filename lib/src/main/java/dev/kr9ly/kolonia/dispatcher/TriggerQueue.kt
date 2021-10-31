package dev.kr9ly.kolonia.dispatcher

import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

class TriggerQueue<Trigger> {

    private val triggerFlow = MutableSharedFlow<Trigger>(extraBufferCapacity = Int.MAX_VALUE)

    private val waitingQueue = ConcurrentLinkedQueue<Trigger>()

    private var started = false

    fun dispatch(trigger: Trigger) {
        if (!started) {
            waitingQueue.add(trigger)
        } else {
            triggerFlow.tryEmit(trigger)
        }
    }

    suspend fun collect(callback: suspend (Trigger) -> Unit) {
        coroutineScope {
            launch {
                started = true
                triggerFlow.collect { trigger ->
                    callback(trigger)
                }
            }

            while (true) {
                val trigger = waitingQueue.poll() ?: break
                triggerFlow.emit(trigger)
            }

            try {
                awaitCancellation()
            } finally {
                started = false
            }
        }
    }
}