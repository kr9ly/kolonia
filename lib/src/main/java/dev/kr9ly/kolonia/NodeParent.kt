package dev.kr9ly.kolonia

import dev.kr9ly.kolonia.functions.SignalDelegate
import dev.kr9ly.kolonia.path.NodePath

interface NodeParent<Signal> {
    val path: NodePath
    val signalDelegate: SignalDelegate<Signal>

    companion object {

        fun <Signal> forPreview(): NodeParent<Signal> = object : NodeParent<Signal> {

            override val path: NodePath
                get() = NodePath.Root

            override val signalDelegate: SignalDelegate<Signal>
                get() = { }
        }
    }
}