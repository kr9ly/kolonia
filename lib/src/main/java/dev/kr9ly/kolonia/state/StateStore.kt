package dev.kr9ly.kolonia.state

import android.os.Parcelable
import dev.kr9ly.kolonia.path.NodePath

interface StateStore {

    fun save(path: NodePath, value: Parcelable)

    fun saveList(path: NodePath, valueList: List<Parcelable>)

    fun restore(path: NodePath): Parcelable?

    fun restoreList(path: NodePath): List<Parcelable>?
}