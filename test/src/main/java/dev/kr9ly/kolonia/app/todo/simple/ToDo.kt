package dev.kr9ly.kolonia.app.todo.simple

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class ToDo(val id: UUID, val body: String) : Parcelable