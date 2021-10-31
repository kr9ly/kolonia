package dev.kr9ly.kolonia.app.todo.simple

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.kr9ly.kolonia.*
import dev.kr9ly.kolonia.utils.lazy.LazyVal
import java.util.*

private val mockView = SimpleToDoView.Mock(
    isShown = true,
    inputText = "brush teeth",
    todoList = LazyVal.mock(
        listOf(
            ToDo(UUID.randomUUID(), "eat breakfast"),
            ToDo(UUID.randomUUID(), "go to work"),
        )
    ),
    doneList = LazyVal.mock(
        listOf(
            ToDo(UUID.randomUUID(), "wake up"),
            ToDo(UUID.randomUUID(), "wash my face"),
        )
    )
)

@Preview(showBackground = true)
@Composable
fun SimpleToDoScreen(
    environment: KoloniaEnvironment<KoloniaTestComponent> = KoloniaEnvironment.forPreview(),
    parent: NodeParent<KoloniaTestSignal> = NodeParent.forPreview(),
    viewForPreview: SimpleToDoView = mockView
) {
    KoloniaComponent(
        environment = environment,
        parent = parent,
        provider = SimpleToDoModel.provider,
        componentId = "root",
        componentKey = "todo",
        props = Unit,
        viewForPreview = viewForPreview
    ) { node, view ->
        Scaffold(
            topBar = { TopAppBar(title = { Text("SimpleToDoScreen") }) },
            content = {
                Column {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            label = { Text("input todo") },
                            value = view.inputText,
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                            onValueChange = { value ->
                                node.dispatchTrigger(
                                    SimpleToDoTrigger.UpdateInputText(value)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { node.dispatchTrigger(SimpleToDoTrigger.CreateToDo) }
                        ) {
                            Text("Add")
                        }
                    }
                    key(view.todoList, view.doneList) {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                Text("ToDo List")
                            }
                            view.todoList.value().let { todoList ->
                                itemsIndexed(todoList, { _, v -> v.id }) { index, toDo ->
                                    Row(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            toDo.body,
                                            fontSize = 18.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedButton(onClick = {
                                            node.dispatchTrigger(
                                                SimpleToDoTrigger.ToggleDone(toDo.id)
                                            )
                                        }) {
                                            Text("Done")
                                        }
                                    }
                                    if (index + 1 < todoList.size) {
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            }
                            item {
                                Text("Done List")
                            }
                            view.doneList.value().let { todoList ->
                                itemsIndexed(todoList, { _, v -> v.id }) { index, toDo ->
                                    Row(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            toDo.body,
                                            fontSize = 18.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        OutlinedButton(onClick = {
                                            node.dispatchTrigger(
                                                SimpleToDoTrigger.ToggleDone(toDo.id)
                                            )
                                        }) {
                                            Text(
                                                text = "UnDone",
                                                style = LocalTextStyle.current.copy(color = Color.Gray)
                                            )
                                        }
                                    }
                                    if (index + 1 < todoList.size) {
                                        Divider(color = Color.LightGray, thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}