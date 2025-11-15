package com.allsoundspro.app.navigation

sealed class Route(val route: String) {
    object Home : Route("home")
    object Player : Route("player")
}
