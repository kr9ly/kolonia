package dev.kr9ly.kolonia.app

import dev.kr9ly.kolonia.Drone
import dev.kr9ly.kolonia.functions.Effect

interface Hive<Signal, Trigger, Props, State> {

    fun generate(): Drone<Signal, Trigger, Props, State>

    fun effect(effect: Effect<Signal, State>) = effect
}