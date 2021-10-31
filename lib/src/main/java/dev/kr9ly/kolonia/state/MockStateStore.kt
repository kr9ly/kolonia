package dev.kr9ly.kolonia.state

import android.os.Parcelable
import dev.kr9ly.kolonia.path.NodePath

class MockStateStore : StateStore {

    private val storeMap = mutableMapOf<NodePath, Any>()

    override fun save(path: NodePath, value: Parcelable) {
        storeMap[path] = value
    }

    override fun saveList(path: NodePath, valueList: List<Parcelable>) {
        storeMap[path] = valueList
    }

    override fun restore(path: NodePath): Parcelable? =
        storeMap[path] as? Parcelable?

    @Suppress("UNCHECKED_CAST")
    override fun restoreList(path: NodePath): List<Parcelable>? =
        storeMap[path] as? List<Parcelable>?
}