package dev.kr9ly.kolonia

import androidx.annotation.VisibleForTesting
import dev.kr9ly.kolonia.coroutines.KoloniaCoroutineDispatcher
import dev.kr9ly.kolonia.state.StateStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface KoloniaEnvironment<out DiProvider> {

    val diProvider: DiProvider

    val stateStore: StateStore

    val dispatcherForDispatchQueue: CoroutineDispatcher

    val dispatcherForIo: CoroutineDispatcher

    val inPreview: Boolean

    class ForPreview<out DiProvider> internal constructor() : KoloniaEnvironment<DiProvider> {

        override val diProvider: DiProvider
            get() = throw UnsupportedOperationException()

        override val stateStore: StateStore
            get() = throw UnsupportedOperationException()

        override val dispatcherForDispatchQueue: CoroutineDispatcher
            get() = throw UnsupportedOperationException()

        override val dispatcherForIo: CoroutineDispatcher
            get() = throw UnsupportedOperationException()

        override val inPreview: Boolean = true
    }

    class Impl<out DiProvider> internal constructor(
        override val diProvider: DiProvider,
        override val stateStore: StateStore,
        override val dispatcherForDispatchQueue: CoroutineDispatcher,
        override val dispatcherForIo: CoroutineDispatcher
    ) : KoloniaEnvironment<DiProvider> {

        override val inPreview: Boolean = false
    }

    companion object {

        fun <DiProvider> create(
            diProvider: DiProvider,
            stateStore: StateStore,
        ): KoloniaEnvironment<DiProvider> = Impl(
            diProvider,
            stateStore,
            KoloniaCoroutineDispatcher,
            Dispatchers.IO
        )

        fun <DiProvider> forPreview(): KoloniaEnvironment<DiProvider> = ForPreview()

        @VisibleForTesting
        fun <DiProvider> forTest(
            diProvider: DiProvider,
            stateStore: StateStore,
            dispatcherForDispatchQueue: CoroutineDispatcher,
            dispatcherForIo: CoroutineDispatcher
        ): KoloniaEnvironment<DiProvider> = Impl(
            diProvider,
            stateStore,
            dispatcherForDispatchQueue,
            dispatcherForIo
        )
    }
}