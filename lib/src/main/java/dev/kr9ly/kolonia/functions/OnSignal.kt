package dev.kr9ly.kolonia.functions

typealias OnSignal<Signal, Props, State> = (signal: Signal, props: Props, state: State) -> Effect<Signal, State>?