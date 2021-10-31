package dev.kr9ly.kolonia

import android.os.Parcelable
import dev.kr9ly.kolonia.app.Hive
import dev.kr9ly.kolonia.app.Mapper
import dev.kr9ly.kolonia.context.EffectContextImpl
import dev.kr9ly.kolonia.dispatcher.Dispatch
import dev.kr9ly.kolonia.dispatcher.DispatchQueue
import dev.kr9ly.kolonia.dispatcher.TriggerQueue
import dev.kr9ly.kolonia.functions.Effect
import dev.kr9ly.kolonia.functions.SignalDelegate
import dev.kr9ly.kolonia.path.NodePath
import dev.kr9ly.kolonia.provider.InstanceProvider
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class KoloniaNodeImpl<DiProvider, Signal, Trigger, Props, State : Parcelable, View>(
    private val environment: KoloniaEnvironment<DiProvider>,
    private val parentSignalDelegate: SignalDelegate<Signal>,
    private val hiveInstanceProvider: InstanceProvider<DiProvider, out Hive<Signal, Trigger, Props, State>>,
    mapperInstanceProvider: InstanceProvider<DiProvider, out Mapper<Props, State, View>>,
    override val path: NodePath,
    initialProps: Props,
    initialState: State
) : KoloniaNode<DiProvider, Signal, Trigger, Props, State, View> {

    private val latestStatePath = path.child("_latestState")

    private val effectContext = EffectContextImpl(this)

    private val mapper = mapperInstanceProvider.provide(environment.diProvider)

    private val triggerQueue = TriggerQueue<Trigger>()

    private val dispatchQueue =
        DispatchQueue<Signal, Trigger, Props, State>(environment.dispatcherForDispatchQueue)

    private var latestProps = initialProps

    @Suppress("UNCHECKED_CAST")
    private var latestState: State =
        environment.stateStore.restore(latestStatePath) as? State ?: initialState

    private val leavingDispatchFlow =
        MutableSharedFlow<Dispatch<Signal, Trigger, Props, State>>(extraBufferCapacity = Int.MAX_VALUE)

    private val stateFlow = MutableStateFlow(mapper.generate(latestProps, latestState))

    override val signalDelegate: SignalDelegate<Signal>
        get() = { signal ->
            dispatchQueue.dispatch { drone ->
                val effect = drone.dispatchSignal(signal, latestProps, latestState)
                if (effect != null) {
                    dispatchEffect(effect)
                } else {
                    dispatchSignal(signal)
                }
            }
        }

    override fun dispatchTrigger(trigger: Trigger) {
        triggerQueue.dispatch(trigger)
    }

    fun latestView(): View = stateFlow.value

    fun viewStateFlow(): StateFlow<View> = stateFlow

    suspend fun launchOnCreate() {
        coroutineScope {
            launch(environment.dispatcherForDispatchQueue) {
                triggerQueue.collect { trigger ->
                    dispatchQueue.dispatch { drone ->
                        val effect = drone.dispatchTrigger(trigger, latestProps, latestState)
                        dispatchEffect(effect)
                    }
                }
            }

            launch(environment.dispatcherForDispatchQueue) {
                leavingDispatchFlow.collect { dispatch ->
                    dispatchQueue.dispatch(dispatch)
                }
            }

            launch(environment.dispatcherForIo) {
                val drone = hiveInstanceProvider.provide(environment.diProvider).generate()
                dispatchQueue.startAndWait(drone)
            }
        }
    }

    suspend fun launchOnStart() {
        coroutineScope {
            launch(environment.dispatcherForDispatchQueue) {
                try {
                    dispatchQueue.dispatch { drone ->
                        drone.dispatchOnShown(latestProps, latestState)?.let { dispatchEffect(it) }
                    }
                    awaitCancellation()
                } finally {
                    leavingDispatchFlow.tryEmit { drone ->
                        drone.dispatchOnHidden(latestProps, latestState)?.let { effect ->
                            coroutineScope {
                                effect(this, effectContext, latestState)
                            }
                        }
                    }
                }
            }
        }
    }

    suspend fun dispatchSignal(signal: Signal) {
        parentSignalDelegate(signal)
    }

    suspend fun dispatchProps(props: Props) {
        if (props == latestProps) {
            return
        }
        dispatchQueue.dispatch {
            latestProps = props
            stateFlow.emit(mapper.generate(latestProps, latestState))
        }
    }

    suspend fun dispatchEffect(effect: Effect<Signal, State>) {
        dispatchQueue.dispatch {
            effect(this, effectContext, latestState)
        }
    }

    suspend fun dispatchState(diff: (State) -> State) {
        dispatchQueue.dispatch {
            val state = diff(latestState)
            latestState = state
            environment.stateStore.save(latestStatePath, state)
            stateFlow.emit(mapper.generate(latestProps, state))
        }
    }
}