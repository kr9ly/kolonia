package dev.kr9ly.kolonia.route.transaction

import dev.kr9ly.kolonia.route.RouteTransaction

class PopRoute<Route> : RouteTransaction<Route> {

    override fun apply(routeHistory: List<Route>): List<Route> =
        routeHistory.dropLast(1)
}