package com.example.bersihkan.ui.navigation

sealed class Screen(val route: String){
    object WelcomeScreen1: Screen("welcome screen 1")
    object WelcomeScreen2: Screen("welcome screen 2")
    object Register: Screen("register")
    object Login: Screen("login")
    object Home: Screen("home")
    object History: Screen("history")
    object Profile: Screen("profile")
}
