package dev.kr9ly.kolonia.context

import dev.kr9ly.kolonia.functions.Effect
import dev.kr9ly.kolonia.prelude.Lens

class SubEffectContextImpl<Signal, RootState, State>(
    private val rootContext: EffectContext<Signal, RootState>,
    private val lens: Lens<RootState, State>
) : SubEffectContext<Signal, RootState, State> {

    override suspend fun dispatchEffect(effect: Effect<Signal, RootState>) {
        rootContext.dispatchEffect(effect)
    }

    override suspend fun dispatchSignal(signal: Signal) {
        rootContext.dispatchSignal(signal)
    }

    override suspend fun dispatchState(diff: State.() -> State) {
        rootContext.dispatchState {
            lens.copy(this, diff(lens.get(this)))
        }
    }
}