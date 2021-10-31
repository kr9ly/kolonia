package dev.kr9ly.kolonia.context

interface SignalDispatcherContext<Signal> {

    suspend fun dispatchSignal(signal: Signal)
}