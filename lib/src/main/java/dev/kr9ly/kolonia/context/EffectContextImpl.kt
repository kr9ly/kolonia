package dev.kr9ly.kolonia.context

import android.os.Parcelable
import dev.kr9ly.kolonia.KoloniaNodeImpl
import dev.kr9ly.kolonia.functions.Effect

class EffectContextImpl<Signal, State : Parcelable>(
    private val node: KoloniaNodeImpl<*, Signal, *, *, State, *>
) : EffectContext<Signal, State> {

    override suspend fun dispatchEffect(effect: Effect<Signal, State>) {
        node.dispatchEffect(effect)
    }

    override suspend fun dispatchSignal(signal: Signal) {
        node.dispatchSignal(signal)
    }

    override suspend fun dispatchState(diff: State.() -> State) {
        node.dispatchState(diff)
    }
}