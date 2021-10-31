package dev.kr9ly.kolonia.utils.lazy

import androidx.compose.runtime.Stable
import dev.kr9ly.kolonia.utils.equals.EqualsUtil
import dev.kr9ly.kolonia.utils.tostring.ToStringUtil

@Stable
interface LazyVal<T> {

    fun value(): T

    private class Mock<T>(
        private val value: T
    ) : LazyVal<T> {

        override fun value(): T = value

    }

    private class LazyVal1<T, V1>(
        private val value1: V1,
        private val builder: (V1) -> T
    ) : LazyVal<T> {

        override fun value(): T = builder(value1)

        override fun equals(other: Any?): Boolean =
            EqualsUtil.equals(
                this,
                other,
                LazyVal1<T, V1>::value1
            )

        override fun hashCode(): Int =
            EqualsUtil.hashCode(
                this,
                LazyVal1<T, V1>::value1
            )

        override fun toString(): String =
            ToStringUtil.toString(
                this,
                LazyVal1<T, V1>::value1
            )
    }

    private class LazyVal2<T, V1, V2>(
        private val value1: V1,
        private val value2: V2,
        private val builder: (V1, V2) -> T
    ) : LazyVal<T> {

        override fun value(): T = builder(value1, value2)

        override fun equals(other: Any?): Boolean =
            EqualsUtil.equals(
                this,
                other,
                LazyVal2<T, V1, V2>::value1,
                LazyVal2<T, V1, V2>::value2,
            )

        override fun hashCode(): Int =
            EqualsUtil.hashCode(
                this,
                LazyVal2<T, V1, V2>::value1,
                LazyVal2<T, V1, V2>::value2,
            )

        override fun toString(): String =
            ToStringUtil.toString(
                this,
                LazyVal2<T, V1, V2>::value1,
                LazyVal2<T, V1, V2>::value2,
            )
    }

    private class LazyVal3<T, V1, V2, V3>(
        private val value1: V1,
        private val value2: V2,
        private val value3: V3,
        private val builder: (V1, V2, V3) -> T
    ) : LazyVal<T> {

        override fun value(): T = builder(value1, value2, value3)

        override fun equals(other: Any?): Boolean =
            EqualsUtil.equals(
                this,
                other,
                LazyVal3<T, V1, V2, V3>::value1,
                LazyVal3<T, V1, V2, V3>::value2,
                LazyVal3<T, V1, V2, V3>::value3,
            )

        override fun hashCode(): Int =
            EqualsUtil.hashCode(
                this,
                LazyVal3<T, V1, V2, V3>::value1,
                LazyVal3<T, V1, V2, V3>::value2,
                LazyVal3<T, V1, V2, V3>::value3,
            )

        override fun toString(): String =
            ToStringUtil.toString(
                this,
                LazyVal3<T, V1, V2, V3>::value1,
                LazyVal3<T, V1, V2, V3>::value2,
                LazyVal3<T, V1, V2, V3>::value3,
            )
    }

    companion object {

        fun <T> mock(value: T): LazyVal<T> = Mock(value)

        operator fun <T, V1> invoke(value1: V1, builder: (V1) -> T): LazyVal<T> =
            LazyVal1(value1, builder)

        operator fun <T, V1, V2> invoke(
            value1: V1,
            value2: V2,
            builder: (V1, V2) -> T
        ): LazyVal<T> =
            LazyVal2(value1, value2, builder)

        operator fun <T, V1, V2, V3> invoke(
            value1: V1,
            value2: V2,
            value3: V3,
            builder: (V1, V2, V3) -> T
        ): LazyVal<T> =
            LazyVal3(value1, value2, value3, builder)
    }
}