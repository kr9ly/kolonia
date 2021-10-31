package dev.kr9ly.kolonia.path

class NodePath(private val key: String, private val parent: NodePath?) {

    fun fullPath(): String = when (parent) {
        null -> key
        else -> "${parent.fullPath()}/key"
    }

    fun child(key: String) = NodePath(key, this)

    companion object {
        val Root = NodePath("", null)
    }
}