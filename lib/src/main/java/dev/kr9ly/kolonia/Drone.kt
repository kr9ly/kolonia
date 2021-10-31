package dev.kr9ly.kolonia

import dev.kr9ly.kolonia.functions.OnEvent
import dev.kr9ly.kolonia.functions.OnSignal
import dev.kr9ly.kolonia.functions.OnTrigger

class Drone<Signal, Trigger, Props, State>(
    private val onSignal: OnSignal<Signal, Props, State>? = null,
    private val onShown: OnEvent<Signal, Props, State>? = null,
    private val onHidden: OnEvent<Signal, Props, State>? = null,
    private val onTrigger: OnTrigger<Signal, Trigger, Props, State>
) {

    fun dispatchTrigger(trigger: Trigger, props: Props, state: State) =
        onTrigger(trigger, props, state)

    fun dispatchSignal(signal: Signal, props: Props, state: State) =
        onSignal?.invoke(signal, props, state)

    fun dispatchOnShown(props: Props, state: State) =
        onShown?.invoke(props, state)

    fun dispatchOnHidden(props: Props, state: State) =
        onHidden?.invoke(props, state)
}