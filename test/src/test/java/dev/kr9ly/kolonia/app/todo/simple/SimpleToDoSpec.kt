package dev.kr9ly.kolonia.app.todo.simple

import app.cash.turbine.test
import dev.kr9ly.kolonia.*
import dev.kr9ly.kolonia.state.MockStateStore
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalCoroutinesApi
class SimpleToDoSpec : ShouldSpec({
    val testCoroutineDispatcher = TestCoroutineDispatcher()

    val environment = KoloniaEnvironment.forTest(
        DaggerKoloniaTestComponent.create(),
        MockStateStore(),
        testCoroutineDispatcher,
        testCoroutineDispatcher
    )

    val nodeRoot = NodeRoot<KoloniaTestSignal> {
    }

    context("test trigger") {
        context("before start") {
            val node = SimpleToDoModel.provider.provide(
                environment,
                nodeRoot,
                "id",
                "test",
                Unit,
            ) as KoloniaNodeImpl

            should("only emit initial view whenever call any trigger") {
                node.viewStateFlow().test {
                    awaitItem().apply {
                        todoList.value() shouldBe emptyList()
                    }

                    node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                    node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                    node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)

                    shouldThrow<TimeoutCancellationException> {
                        withTimeout(Duration.seconds(1)) {
                            awaitItem()
                        }
                    }
                }
            }
        }

        context("after start") {
            should("emit effects before launch") {
                val node = SimpleToDoModel.provider.provide(
                    environment,
                    nodeRoot,
                    "id",
                    "test",
                    Unit,
                ) as KoloniaNodeImpl

                node.viewStateFlow().test {
                    node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                    node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                    node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)

                    node.inCreate {
                        node.inStart {
                            awaitItem().apply {
                                todoList.value() shouldBe emptyList()
                            }
                            awaitItem().apply {
                                todoList.value().lastOrNull()?.body shouldBe "sample1"
                            }
                            awaitItem().apply {
                                todoList.value().lastOrNull()?.body shouldBe "sample2"
                            }
                            awaitItem().apply {
                                todoList.value().lastOrNull()?.body shouldBe "sample3"
                            }
                        }
                    }

                    cancelAndConsumeRemainingEvents()
                }
            }

            should("emit effects on the basis of trigger") {
                val node = SimpleToDoModel.provider.provide(
                    environment,
                    nodeRoot,
                    "id",
                    "test",
                    Unit,
                ) as KoloniaNodeImpl

                val viewStateFlow = node.viewStateFlow()

                node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)

                node.inCreate {
                    node.inStart {
                        viewStateFlow.test {
                            awaitItem().apply(::println)
                            awaitItem().apply(::println)
                            awaitItem().apply(::println)
                            awaitItem().apply(::println)
                            awaitItem().apply(::println)
                        }
                        viewStateFlow.test {
                            awaitItem().apply(::println)
                            node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                            awaitItem().apply {
                                todoList.value().lastOrNull()?.body shouldBe "sample4"
                            }
                            node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                            awaitItem().apply {
                                todoList.value().lastOrNull()?.body shouldBe "sample5"
                            }
                            node.dispatchTrigger(SimpleToDoTrigger.CreateToDo)
                            awaitItem().apply {
                                todoList.value().lastOrNull()?.body shouldBe "sample6"
                            }
                        }
                    }
                }
            }
        }
    }

    context("test events") {
        should("emit effects on shown and hidden") {
            val node = SimpleToDoModel.provider.provide(
                environment,
                nodeRoot,
                "id",
                "test",
                Unit,
            ) as KoloniaNodeImpl

            node.viewStateFlow().test {
                node.inCreate {
                    awaitItem().isShown shouldBe false
                    node.inStart {
                        awaitItem().isShown shouldBe true
                    }
                    awaitItem().isShown shouldBe false
                }
            }
        }
    }
})