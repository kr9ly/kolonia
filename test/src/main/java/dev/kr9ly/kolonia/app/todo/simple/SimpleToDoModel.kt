package dev.kr9ly.kolonia.app.todo.simple

import android.os.Parcelable
import dev.kr9ly.kolonia.DaggerInstanceProvider
import dev.kr9ly.kolonia.Drone
import dev.kr9ly.kolonia.KoloniaProvider
import dev.kr9ly.kolonia.app.Hive
import dev.kr9ly.kolonia.app.Mapper
import dev.kr9ly.kolonia.utils.lazy.LazyVal
import dev.kr9ly.kolonia.utils.tostring.ToStringUtil
import kotlinx.parcelize.Parcelize
import java.util.*
import javax.inject.Inject
import dev.kr9ly.kolonia.KoloniaTestSignal as Signal
import kotlin.Any as Props

object SimpleToDoModel {

    val provider = KoloniaProvider.Factory(
        DaggerInstanceProvider(SimpleToDoComponent::simpleToDoHive),
        DaggerInstanceProvider(SimpleToDoComponent::simpleToDoMapper),
        State()
    )

    @Parcelize
    data class State(
        val inputText: String = "",
        val todoList: List<ToDo> = emptyList(),
        val doneToDoIds: Set<UUID> = emptySet(),
        val isShown: Boolean = false
    ) : Parcelable

    class ToDoHive @Inject constructor() : Hive<Signal, SimpleToDoTrigger, Props, State> {

        override fun generate(): Drone<Signal, SimpleToDoTrigger, Props, State> =
            Drone(
                onShown = { _, _ ->
                    onShown()
                },
                onHidden = { _, _ ->
                    onHidden()
                }
            ) { trigger, _, _ ->
                when (trigger) {
                    is SimpleToDoTrigger.CreateToDo -> onCreateToDo()
                    is SimpleToDoTrigger.RemoveToDo -> onRemoveToDo(trigger.id)
                    is SimpleToDoTrigger.ToggleDone -> onToggleDone(trigger.id)
                    is SimpleToDoTrigger.ToggleUnDone -> onToggleUnDone(trigger.id)
                    is SimpleToDoTrigger.UpdateInputText -> onUpdateInputText(trigger.inputText)
                }
            }

        private fun onUpdateInputText(inputText: String) = effect { context, _ ->
            context.dispatchState {
                copy(
                    inputText = inputText
                )
            }
        }

        private fun onShown() = effect { context, _ ->
            context.dispatchState {
                copy(
                    isShown = true
                )
            }
        }

        private fun onHidden() = effect { context, _ ->
            context.dispatchState {
                copy(
                    isShown = false
                )
            }
        }

        private fun onCreateToDo() = effect { context, _ ->
            context.dispatchState {
                copy(
                    inputText = "",
                    todoList = todoList + ToDo(UUID.randomUUID(), inputText)
                )
            }
        }

        private fun onRemoveToDo(id: UUID) = effect { context, _ ->
            context.dispatchState {
                copy(
                    todoList = todoList.filter { id != it.id },
                    doneToDoIds = doneToDoIds - id
                )
            }
        }

        private fun onToggleDone(id: UUID) = effect { context, _ ->
            context.dispatchState {
                copy(
                    doneToDoIds = doneToDoIds + id
                )
            }
        }

        private fun onToggleUnDone(id: UUID) = effect { context, _ ->
            context.dispatchState {
                copy(
                    doneToDoIds = doneToDoIds - id
                )
            }
        }
    }

    class ToDoMapper @Inject constructor() : Mapper<Props, State, SimpleToDoView> {

        override fun generate(props: Props, state: State) = object : SimpleToDoView {

            override val todoList: LazyVal<List<ToDo>>
                get() = LazyVal(state.todoList, state.doneToDoIds) { todoList, doneToDoIds ->
                    todoList.filter { todo ->
                        !doneToDoIds.contains(todo.id)
                    }
                }

            override val doneList: LazyVal<List<ToDo>>
                get() = LazyVal(state.todoList, state.doneToDoIds) { todoList, doneToDoIds ->
                    todoList.filter { todo ->
                        doneToDoIds.contains(todo.id)
                    }
                }

            override val isShown: Boolean
                get() = state.isShown

            override val inputText: String
                get() = state.inputText

            override fun toString(): String =
                ToStringUtil.toString(
                    this,
                    SimpleToDoView::todoList,
                    SimpleToDoView::isShown
                )
        }
    }
}