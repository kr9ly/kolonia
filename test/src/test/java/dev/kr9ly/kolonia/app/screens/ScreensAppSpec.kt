package dev.kr9ly.kolonia.app.screens

import app.cash.turbine.test
import dev.kr9ly.kolonia.*
import dev.kr9ly.kolonia.app.screens.child.ChildScreenModel
import dev.kr9ly.kolonia.app.screens.child.ChildScreenProps
import dev.kr9ly.kolonia.app.screens.child.ChildScreenTrigger
import dev.kr9ly.kolonia.app.screens.root.RootScreenModel
import dev.kr9ly.kolonia.app.screens.root.RootScreenProps
import dev.kr9ly.kolonia.route.RouteController
import dev.kr9ly.kolonia.state.MockStateStore
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class ScreensAppSpec : ShouldSpec({
    val testCoroutineDispatcher = TestCoroutineDispatcher()

    val environment = KoloniaEnvironment.forTest(
        DaggerKoloniaTestComponent.create(),
        MockStateStore(),
        testCoroutineDispatcher,
        testCoroutineDispatcher
    )

    val nodeRoot = NodeRoot<ScreensAppSignal> {
    }

    val routeController = RouteController<ScreensAppRoute>(environment, ScreensAppRoute.RootScreen)

    context("test signal") {
        val rootScreenNode = RootScreenModel.provider.provide(
            environment,
            nodeRoot,
            "id",
            "test",
            RootScreenProps(routeController),
        ) as KoloniaNodeImpl

        val childScreenNode = ChildScreenModel.provider.provide(
            environment,
            rootScreenNode,
            "id",
            "test",
            ChildScreenProps(rootScreenNode.latestView().route)
        ) as KoloniaNodeImpl

        val rootStateFlow = rootScreenNode.viewStateFlow()

        val stateJob = launch(testCoroutineDispatcher) {
            rootStateFlow.collect { view ->
                childScreenNode.dispatchProps(ChildScreenProps(view.route))
            }
        }

        should("update route by trigger") {
            rootScreenNode.inCreate {
                rootScreenNode.inStart {
                    childScreenNode.inCreate {
                        childScreenNode.inStart {
                            childScreenNode.viewStateFlow().test {
                                awaitItem().route shouldBe ScreensAppRoute.RootScreen
                                childScreenNode.dispatchTrigger(ChildScreenTrigger.OpenScreenA)
                                awaitItem().route shouldBe ScreensAppRoute.ScreenA
                                childScreenNode.dispatchTrigger(ChildScreenTrigger.OpenScreenB)
                                awaitItem().route shouldBe ScreensAppRoute.ScreenB
                                childScreenNode.dispatchTrigger(ChildScreenTrigger.BackScreen)
                                awaitItem().route shouldBe ScreensAppRoute.ScreenA
                                childScreenNode.dispatchTrigger(ChildScreenTrigger.OpenScreenB)
                                awaitItem().route shouldBe ScreensAppRoute.ScreenB
                                childScreenNode.dispatchTrigger(ChildScreenTrigger.BackToTop)
                                awaitItem().route shouldBe ScreensAppRoute.RootScreen
                            }
                        }
                    }
                }
            }
        }

        stateJob.cancel()
    }
})