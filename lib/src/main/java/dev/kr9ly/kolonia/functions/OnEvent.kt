package dev.kr9ly.kolonia.functions

typealias OnEvent<Signal, Props, State> = (props: Props, state: State) -> Effect<Signal, State>