package dev.kr9ly.kolonia.app.screens.child

import android.os.Parcelable
import dev.kr9ly.kolonia.DaggerInstanceProvider
import dev.kr9ly.kolonia.Drone
import dev.kr9ly.kolonia.KoloniaProvider
import dev.kr9ly.kolonia.app.Hive
import dev.kr9ly.kolonia.app.Mapper
import dev.kr9ly.kolonia.app.screens.ScreensAppComponent
import dev.kr9ly.kolonia.app.screens.ScreensAppRoute
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import dev.kr9ly.kolonia.app.screens.ScreensAppSignal as Signal
import dev.kr9ly.kolonia.app.screens.child.ChildScreenProps as Props
import dev.kr9ly.kolonia.app.screens.child.ChildScreenTrigger as Trigger
import dev.kr9ly.kolonia.app.screens.child.ChildScreenView as View

object ChildScreenModel {

    val provider = KoloniaProvider.Factory(
        DaggerInstanceProvider(ScreensAppComponent::screensAppChildScreenHive),
        DaggerInstanceProvider(ScreensAppComponent::screensAppChildScreenMapper),
        State()
    )

    @Parcelize
    class State : Parcelable

    class ChildScreenHive @Inject constructor() : Hive<Signal, Trigger, Props, State> {

        override fun generate(): Drone<Signal, Trigger, Props, State> =
            Drone { trigger, _, _ ->
                when (trigger) {
                    Trigger.BackScreen -> onBackScreen()
                    Trigger.BackToTop -> onBackToTop()
                    Trigger.OpenScreenA -> onOpenScreenA()
                    Trigger.OpenScreenB -> onOpenScreenB()
                }
            }

        private fun onBackScreen() = effect { context, _ ->
            context.dispatchSignal(Signal.BackScreen)
        }

        private fun onBackToTop() = effect { context, _ ->
            context.dispatchSignal(Signal.BackToRoot)
        }

        private fun onOpenScreenA() = effect { context, _ ->
            context.dispatchSignal(Signal.MoveToScreenA)
        }

        private fun onOpenScreenB() = effect { context, _ ->
            context.dispatchSignal(Signal.MoveToScreenB)
        }
    }

    class ChildScreenMapper @Inject constructor() : Mapper<Props, State, View> {

        override fun generate(
            props: Props,
            state: State
        ): View = object : View {

            override val route: ScreensAppRoute
                get() = props.route
        }
    }
}