package dev.kr9ly.kolonia

import dev.kr9ly.kolonia.functions.SignalDelegate
import dev.kr9ly.kolonia.path.NodePath

interface KoloniaNode<DiProvider, Signal, Trigger, Props, State, View> : NodeParent<Signal> {

    fun dispatchTrigger(trigger: Trigger)

    class ForPreview<DiProvider, Signal, Trigger, Props, State, View> internal constructor() :
        KoloniaNode<DiProvider, Signal, Trigger, Props, State, View> {

        override fun dispatchTrigger(trigger: Trigger) {
        }

        override val path: NodePath
            get() = NodePath.Root

        override val signalDelegate: SignalDelegate<Signal>
            get() = {}

    }

    companion object {

        fun <DiProvider, Signal, Trigger, Props, State, View> forPreview(): KoloniaNode<DiProvider, Signal, Trigger, Props, State, View> =
            ForPreview()
    }
}