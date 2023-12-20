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
    object Order: Screen("order/{lat}/{lon}"){
        fun createRoute(lat: Float, lon: Float) = "order/$lat/$lon"
    }
    object Delivery: Screen("delivery/{orderId}"){
        fun createRoute(orderId: Int) = "delivery/$orderId"
    }
    object Statistics: Screen("statistics")
    object About: Screen("about")
    object HomeCollector: Screen("home-collector")
    object HistoryCollector: Screen("history-collector")
    object DetailHistoryCollector: Screen("detailHistory-collector/{orderId}"){
        fun createRoute(orderId: Int) = "detailHistory-collector/$orderId"
    }
    object ProfileCollector: Screen("profile-collector")
    object EditProfileCollector: Screen("editProfile-collector")
    object DeliveryCollector: Screen("delivery-collector/{orderId}"){
        fun createRoute(orderId: Int) = "delivery-collector/$orderId"
    }
    object Search: Screen("search/{location}"){
        fun createRoute(location: String) = "search/$location"

    }
}
