package dev.kr9ly.kolonia.context

interface EffectContext<Signal, State> :
    EffectDispatcherContext<Signal, State>,
    StateDispatcherContext<State>,
    SignalDispatcherContext<Signal>