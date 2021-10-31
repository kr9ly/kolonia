package dev.kr9ly.kolonia.route

interface RouteTransaction<Route> {

    fun apply(routeHistory: List<Route>): List<Route>
}