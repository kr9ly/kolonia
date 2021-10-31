package dev.kr9ly.kolonia.route.transaction

import dev.kr9ly.kolonia.route.RouteTransaction

class SwitchRoute<Route>(private val route: Route) : RouteTransaction<Route> {

    override fun apply(routeHistory: List<Route>): List<Route> =
        routeHistory.dropLast(1) + route
}