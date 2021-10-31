package dev.kr9ly.kolonia.coroutines

import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

internal val KoloniaCoroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()