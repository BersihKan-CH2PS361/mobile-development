package com.example.bersihkan.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bersihkan.R
import com.example.bersihkan.ui.navigation.NavigationItem
import com.example.bersihkan.ui.navigation.Screen
import com.example.bersihkan.ui.screen.customer.history.HistoryScreen
import com.example.bersihkan.ui.screen.customer.home.HomeScreen
import com.example.bersihkan.ui.screen.customer.profile.ProfileScreen
import com.example.bersihkan.ui.screen.general.login.LoginScreen
import com.example.bersihkan.ui.screen.general.register.RegisterScreen
import com.example.bersihkan.ui.screen.general.welcomeScreen.WelcomePageOne
import com.example.bersihkan.ui.screen.general.welcomeScreen.WelcomePageTwo
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon

@Composable
fun BersihKanApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if(currentRoute != Screen.WelcomeScreen1.route && currentRoute != Screen.WelcomeScreen2.route && currentRoute != Screen.Register.route && currentRoute != Screen.Login.route){
                BottomBar(navController = navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Screen.WelcomeScreen1.route){
                WelcomePageOne(
                    navigateToNextScreen = {
                        navController.navigate(Screen.WelcomeScreen2.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.WelcomeScreen2.route){
                WelcomePageTwo(
                    navigateToLogin = {
                        navController.navigate(Screen.Login.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToRegister = {
                        navController.navigate(Screen.Register.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Register.route){
                RegisterScreen(
                    navigateToLogin = {
                        navController.navigate(Screen.Login.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToWelcomeScreen2 = {
                        navController.navigate(Screen.WelcomeScreen2.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Login.route){
                LoginScreen(
                    navigateToHomeCustomer = {
                        navController.navigate(Screen.Home.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToHomeCollector = {

                    },
                    navigateToRegister = {
                        navController.navigate(Screen.Register.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Home.route){
                HomeScreen(
                    navigateToHomeCustomer = {
                        navController.navigate(Screen.Home.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToHomeCollector = {

                    },
                    navigateToWelcomePage1 =  {
                        navController.navigate(Screen.WelcomeScreen1.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Profile.route){
                ProfileScreen()
            }
            composable(Screen.History.route){
                HistoryScreen()
            }
        }
    }

}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp, // Adjust the elevation to control the shadow intensity
        color = Color.White // Set the background color of the surface
    ){
        NavigationBar(
            containerColor = Color.White,
            modifier = modifier
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val navigationItems = listOf(
                NavigationItem(
                    title = stringResource(R.string.menu_home),
                    icon = painterResource(id = R.drawable.ic_home),
                    screen = Screen.Home
                ),
                NavigationItem(
                    title = stringResource(R.string.menu_history),
                    icon = painterResource(id = R.drawable.ic_history),
                    screen = Screen.History
                ),
                NavigationItem(
                    title = stringResource(R.string.menu_account),
                    icon = painterResource(id = R.drawable.ic_user_profile),
                    screen = Screen.Profile
                ),
            )
            navigationItems.map { item ->
                NavigationBarItem(
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            painter = item.icon,
                            contentDescription = item.title,
                            tint = BlueLagoon,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }
    }

}

@Preview
@Composable
fun BersihKanAppPreview() {
    BersihKanTheme {
        BersihKanApp()
    }
}