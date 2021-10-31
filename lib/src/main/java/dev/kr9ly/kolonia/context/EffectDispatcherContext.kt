package dev.kr9ly.kolonia.context

import dev.kr9ly.kolonia.functions.Effect

interface EffectDispatcherContext<Signal, State> {

    suspend fun dispatchEffect(effect: Effect<Signal, State>)
}