package dev.kr9ly.kolonia.app.todo.simple

import java.util.*

sealed class SimpleToDoTrigger {
    class UpdateInputText(val inputText: String): SimpleToDoTrigger()
    object CreateToDo : SimpleToDoTrigger()
    class RemoveToDo(val id: UUID) : SimpleToDoTrigger()
    class ToggleDone(val id: UUID) : SimpleToDoTrigger()
    class ToggleUnDone(val id: UUID) : SimpleToDoTrigger()
}