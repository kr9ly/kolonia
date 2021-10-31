package dev.kr9ly.kolonia.functions

typealias OnTrigger<Signal, Trigger, Props, State> = (trigger: Trigger, props: Props, state: State) -> Effect<Signal, State>