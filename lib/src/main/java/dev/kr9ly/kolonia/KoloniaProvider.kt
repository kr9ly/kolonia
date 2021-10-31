package dev.kr9ly.kolonia

import android.os.Parcelable
import dev.kr9ly.kolonia.app.Hive
import dev.kr9ly.kolonia.app.Mapper
import dev.kr9ly.kolonia.path.NodePath
import dev.kr9ly.kolonia.provider.InstanceProvider

fun interface KoloniaProvider<DiProvider, Signal, Trigger, Props, State, View> {

    fun provide(
        environment: KoloniaEnvironment<DiProvider>,
        parent: NodeParent<Signal>,
        nodeId: String,
        nodeKey: String,
        initialProps: Props
    ): KoloniaNode<DiProvider, Signal, Trigger, Props, State, View>

    object Factory {

        operator fun <DiProvider, Signal, Trigger, Props, State : Parcelable, View> invoke(
            hiveInstanceProvider: InstanceProvider<DiProvider, out Hive<Signal, Trigger, Props, State>>,
            mapperInstanceProvider: InstanceProvider<DiProvider, out Mapper<Props, State, View>>,
            initialState: State
        ) = KoloniaProvider { environment: KoloniaEnvironment<DiProvider>,
                              parent: NodeParent<Signal>,
                              nodeId: String,
                              nodeKey: String,
                              initialProps: Props ->
            KoloniaNodeImpl(
                environment,
                parent.signalDelegate,
                hiveInstanceProvider,
                mapperInstanceProvider,
                parent.path.child(nodeId).child(nodeKey),
                initialProps,
                initialState
            )
        }
    }
}