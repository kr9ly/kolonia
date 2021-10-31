package dev.kr9ly.kolonia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope

@Composable
fun <DiProvider, Signal, Trigger, Props, State, View> KoloniaComponent(
    environment: KoloniaEnvironment<DiProvider>,
    parent: NodeParent<Signal>,
    provider: KoloniaProvider<DiProvider, Signal, Trigger, Props, State, View>,
    componentId: String,
    componentKey: String,
    props: Props,
    viewForPreview: View? = null,
    body: @Composable (node: KoloniaNode<DiProvider, Signal, Trigger, Props, State, View>, view: View) -> Unit
) {
    if (environment.inPreview) {
        if (viewForPreview != null) {
            body(KoloniaNode.forPreview(), viewForPreview)
        }
    } else {
        val node = remember(componentKey) {
            provider.provide(
                environment,
                parent,
                componentId,
                componentKey,
                props
            )
        } as KoloniaNodeImpl

        val state = node.viewStateFlow().collectAsState(node.latestView())

        LaunchedEffect(componentKey) {
            node.launchOnCreate()
        }

        LaunchedEffect(props) {
            node.dispatchProps(props)
        }

        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(Unit) {
            lifecycleOwner.lifecycleScope.launchWhenStarted {
                node.launchOnStart()
            }
        }

        body(node, state.value)
    }
}