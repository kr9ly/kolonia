package dev.kr9ly.kolonia.utils.equals

object EqualsUtil {

    inline fun <reified T : Any> equals(
        left: T,
        right: Any?,
        vararg getters: (T) -> Any?
    ): Boolean {
        if (this === right) return true
        if (right !is T) return false
        for (getter in getters) {
            if (getter(left) !== getter(right)) return false
        }
        return true
    }

    fun <T> hashCode(obj: T, vararg getters: (T) -> Any?): Int {
        var result = getters.firstOrNull()?.invoke(obj)?.hashCode() ?: 0
        for (getter in getters) {
            result = 31 * result + getter(obj).hashCode()
        }
        return result
    }
}