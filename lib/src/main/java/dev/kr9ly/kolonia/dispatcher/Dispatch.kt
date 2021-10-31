package dev.kr9ly.kolonia.dispatcher

import dev.kr9ly.kolonia.Drone
import kotlinx.coroutines.CoroutineScope

typealias Dispatch<Signal, Trigger, Props, State> = suspend CoroutineScope.(drone: Drone<Signal, Trigger, Props, State>) -> Unit