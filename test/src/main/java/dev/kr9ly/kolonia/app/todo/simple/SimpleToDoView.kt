package dev.kr9ly.kolonia.app.todo.simple

import dev.kr9ly.kolonia.utils.lazy.LazyVal

interface SimpleToDoView {
    val todoList: LazyVal<List<ToDo>>
    val doneList: LazyVal<List<ToDo>>
    val isShown: Boolean
    val inputText: String

    class Mock(
        override val todoList: LazyVal<List<ToDo>>,
        override val doneList: LazyVal<List<ToDo>>,
        override val isShown: Boolean,
        override val inputText: String,
    ) : SimpleToDoView
}