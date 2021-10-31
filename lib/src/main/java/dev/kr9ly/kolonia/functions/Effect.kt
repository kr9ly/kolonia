package dev.kr9ly.kolonia.functions

import dev.kr9ly.kolonia.context.EffectContext
import kotlinx.coroutines.CoroutineScope

typealias Effect<Signal, State> = suspend CoroutineScope.(context: EffectContext<Signal, State>, state: State) -> Unit

fun <Signal, State> doNothing(): Effect<Signal, State> = { _, _ -> }

operator fun <Signal, State> Effect<Signal, State>.plus(right: Effect<Signal, State>): Effect<Signal, State> {
    val left = this
    return { context, state ->
        left(context, state)
        right(context, state)
    }
}