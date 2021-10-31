package dev.kr9ly.kolonia.context

interface StateDispatcherContext<State> {

    suspend fun dispatchState(diff: State.() -> State)
}