package dev.kr9ly.kolonia.coroutines

import io.kotest.core.spec.style.ShouldSpec
import kotlinx.coroutines.*
import java.util.concurrent.Executors

class SingleDispatcherSpec : ShouldSpec({
    val singleThreadDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    should("complete nested calls") {
        dispatch(singleThreadDispatcher) {
            println("start 1st block")
            dispatch(singleThreadDispatcher) {
                println("start 2nd block")
                dispatch(singleThreadDispatcher) {
                    println("start 3rd block")
                    println("finish 3rd block")
                }
                println("finish 2nd block")
            }
            println("finish 1st block")
        }
    }
}) {

    companion object {
        suspend fun dispatch(dispatcher: CoroutineDispatcher, call: suspend CoroutineScope.() -> Unit) {
            coroutineScope { launch(dispatcher) { call() }.join() }
        }
    }
}