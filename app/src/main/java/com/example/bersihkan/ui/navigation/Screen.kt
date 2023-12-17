package com.example.bersihkan.ui.navigation

sealed class Screen(val route: String){
    object WelcomeScreen1: Screen("welcome screen 1")
    object WelcomeScreen2: Screen("welcome screen 2")
    object Register: Screen("register")
    object Login: Screen("login")
    object Home: Screen("home")
    object History: Screen("history")
    object DetailHistory: Screen("detailHistory/{orderId}"){
        fun createRoute(orderId: Int) = "detailHistory/$orderId"
    }
    object Profile: Screen("profile")
    object EditProfile: Screen("editProfile")
    object Order: Screen("order")
    object Delivery: Screen("delivery")
    object Statistics: Screen("statistics")
    object About: Screen("about")
}
