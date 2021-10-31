package dev.kr9ly.kolonia.route

import android.os.Parcelable
import dev.kr9ly.kolonia.KoloniaEnvironment
import dev.kr9ly.kolonia.functions.Effect
import dev.kr9ly.kolonia.path.NodePath
import dev.kr9ly.kolonia.prelude.Lens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RouteController<Route : Parcelable>(
    koloniaEnvironment: KoloniaEnvironment<*>,
    initialRoute: Route
) {

    private val routePath = NodePath("route", null)

    @Suppress("UNCHECKED_CAST")
    private var routeHistory = koloniaEnvironment.stateStore.restoreList(routePath) as? List<Route>?
        ?: listOf(initialRoute)

    private val routeStateFlow = MutableStateFlow(routeHistory.lastOrNull() ?: initialRoute)

    fun latestRoute() = routeStateFlow.value

    fun <State> subscribeEffect(lens: Lens<State, Route?>): Effect<*, State> = { context, _ ->
        launch {
            routeStateFlow.collect { route ->
                context.dispatchState {
                    lens.copy(this, route)
                }
            }
        }
    }

    fun <State> applyTransactionEffect(vararg transactions: RouteTransaction<Route>): Effect<*, State> = { context, _ ->
        applyTransactions(transactions.toList())
    }

    suspend fun applyTransactions(transactions: List<RouteTransaction<Route>>) {
        val routeHistory = transactions.fold(routeHistory) { routeHistory, transaction ->
            transaction.apply(routeHistory)
        }
        routeHistory.lastOrNull()?.let { route ->
            routeStateFlow.emit(route)
        }
        this.routeHistory = routeHistory
    }
}