package dev.kr9ly.kolonia.functions

import dev.kr9ly.kolonia.context.SubEffectContext
import kotlinx.coroutines.CoroutineScope

typealias SubEffect<Signal, RootState, State> = suspend CoroutineScope.(context: SubEffectContext<Signal, RootState, State>, state: State) -> Unit