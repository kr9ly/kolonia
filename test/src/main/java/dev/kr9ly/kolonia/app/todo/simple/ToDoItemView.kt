package dev.kr9ly.kolonia.app.todo.simple

import java.util.*

data class ToDoItemView(
    private val todo: ToDo,
    private val doneToDoIds: Set<UUID>
) {
    val id: UUID get() = todo.id
    val body: String get() = todo.body
    val isDone: Boolean get() = doneToDoIds.contains(todo.id)
}