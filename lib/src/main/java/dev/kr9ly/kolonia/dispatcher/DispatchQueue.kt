package dev.kr9ly.kolonia.dispatcher

import androidx.annotation.VisibleForTesting
import dev.kr9ly.kolonia.Drone
import kotlinx.coroutines.*
import java.util.concurrent.*

class DispatchQueue<Signal, Trigger, Props, State>(
    private val singleThreadDispatcher: CoroutineDispatcher
) {

    private var drone: Drone<Signal, Trigger, Props, State>? = null

    private val waitingQueue = ConcurrentLinkedQueue<Dispatch<Signal, Trigger, Props, State>>()

    @VisibleForTesting
    suspend fun start(drone: Drone<Signal, Trigger, Props, State>) {
        while (true) {
            val deferredDispatch = waitingQueue.poll() ?: break
            coroutineScope {
                deferredDispatch(this, drone)
            }
        }
        this.drone = drone
    }

    suspend fun startAndWait(drone: Drone<Signal, Trigger, Props, State>) {
        start(drone)
        try {
            awaitCancellation()
        } finally {
            this.drone = null
        }
    }

    suspend fun dispatch(dispatch: Dispatch<Signal, Trigger, Props, State>) {
        val drone = this.drone
        if (drone == null) {
            waitingQueue.add(dispatch)
        } else {
            coroutineScope {
                launch(singleThreadDispatcher) { dispatch(this, drone) }.join()
            }
        }
    }
}