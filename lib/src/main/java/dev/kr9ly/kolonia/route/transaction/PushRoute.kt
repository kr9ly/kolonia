package dev.kr9ly.kolonia.route.transaction

import dev.kr9ly.kolonia.route.RouteTransaction

class PushRoute<Route>(private val route: Route) : RouteTransaction<Route> {

    override fun apply(routeHistory: List<Route>): List<Route> =
        routeHistory + route
}