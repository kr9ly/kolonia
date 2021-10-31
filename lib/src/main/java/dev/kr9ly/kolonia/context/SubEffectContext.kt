package dev.kr9ly.kolonia.context

interface SubEffectContext<Signal, RootState, State> :
    EffectDispatcherContext<Signal, RootState>,
    StateDispatcherContext<State>,
    SignalDispatcherContext<Signal>