package dev.kr9ly.kolonia.utils.tostring

object ToStringUtil {

    inline fun <reified T : Any> toString(obj: T, vararg getters: (T) -> Any?): String {
        return "${obj.javaClass.simpleName}(${getters.map { it(obj) }.joinToString(", ")})"
    }
}