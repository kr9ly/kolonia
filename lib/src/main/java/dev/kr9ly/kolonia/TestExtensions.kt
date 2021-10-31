package dev.kr9ly.kolonia

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@VisibleForTesting
suspend inline fun KoloniaNodeImpl<*, *, *, *, *, *>.inCreate(crossinline callback: suspend CoroutineScope.() -> Unit) {
    coroutineScope {
        val createJob = launch {
            launchOnCreate()
        }
        callback(this)
        createJob.cancel()
    }
}

suspend inline fun KoloniaNodeImpl<*, *, *, *, *, *>.inStart(crossinline callback: suspend CoroutineScope.() -> Unit) {
    coroutineScope {
        val startJob = launch {
            launchOnStart()
        }
        callback(this)
        startJob.cancel()
    }
}