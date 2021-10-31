package dev.kr9ly.kolonia

import dev.kr9ly.kolonia.functions.SignalDelegate
import dev.kr9ly.kolonia.path.NodePath

class NodeRoot<Signal>(
    override val path: NodePath = NodePath.Root,
    override val signalDelegate: SignalDelegate<Signal>
) : NodeParent<Signal>