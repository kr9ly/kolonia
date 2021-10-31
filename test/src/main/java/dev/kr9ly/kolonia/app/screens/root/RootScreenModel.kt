package dev.kr9ly.kolonia.app.screens.root

import android.os.Parcelable
import dev.kr9ly.kolonia.DaggerInstanceProvider
import dev.kr9ly.kolonia.Drone
import dev.kr9ly.kolonia.KoloniaProvider
import dev.kr9ly.kolonia.app.Hive
import dev.kr9ly.kolonia.app.Mapper
import dev.kr9ly.kolonia.app.screens.ScreensAppComponent
import dev.kr9ly.kolonia.app.screens.ScreensAppRoute
import dev.kr9ly.kolonia.functions.doNothing
import dev.kr9ly.kolonia.prelude.Lens
import dev.kr9ly.kolonia.route.transaction.PopRoute
import dev.kr9ly.kolonia.route.transaction.PushRoute
import dev.kr9ly.kolonia.route.transaction.ResetRoute
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import dev.kr9ly.kolonia.app.screens.ScreensAppSignal as Signal
import dev.kr9ly.kolonia.app.screens.root.RootScreenProps as Props
import dev.kr9ly.kolonia.app.screens.root.RootScreenTrigger as Trigger
import dev.kr9ly.kolonia.app.screens.root.RootScreenView as View

object RootScreenModel {

    val provider = KoloniaProvider.Factory(
        DaggerInstanceProvider(ScreensAppComponent::screensAppRootScreenHive),
        DaggerInstanceProvider(ScreensAppComponent::screensAppRootScreenMapper),
        State()
    )

    private val stateRouteLens = Lens(State::route, State::copyWithRoute)

    @Parcelize
    data class State(
        val route: ScreensAppRoute? = null
    ) : Parcelable {

        fun copyWithRoute(route: ScreensAppRoute?) = copy(route = route)
    }

    class RootScreenHive @Inject constructor() : Hive<Signal, Trigger, Props, State> {

        override fun generate(): Drone<Signal, Trigger, Props, State> =
            Drone(
                onShown = { props, _ ->
                    props.routeController.subscribeEffect(stateRouteLens)
                },
                onSignal = { signal, props, _ ->
                    when (signal) {
                        Signal.BackToRoot -> props.routeController.applyTransactionEffect(
                            ResetRoute(ScreensAppRoute.RootScreen)
                        )
                        Signal.MoveToScreenA -> props.routeController.applyTransactionEffect(
                            PushRoute(ScreensAppRoute.ScreenA)
                        )
                        Signal.MoveToScreenB -> props.routeController.applyTransactionEffect(
                            PushRoute(ScreensAppRoute.ScreenB)
                        )
                        Signal.BackScreen -> props.routeController.applyTransactionEffect(PopRoute())
                    }
                }
            ) { trigger, props, state ->
                doNothing()
            }
    }

    class RootScreenMapper @Inject constructor() : Mapper<Props, State, View> {

        override fun generate(
            props: Props,
            state: State
        ): View = object : View {

            override val route: ScreensAppRoute
                get() = state.route ?: props.routeController.latestRoute()
        }
    }
}