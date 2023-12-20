package com.example.bersihkan.ui

import android.util.Log
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bersihkan.R
import com.example.bersihkan.ui.navigation.NavigationItem
import com.example.bersihkan.ui.navigation.Screen
import com.example.bersihkan.ui.screen.customer.delivery.DeliveryScreen
import com.example.bersihkan.ui.screen.customer.detailHistory.DetailScreen
import com.example.bersihkan.ui.screen.customer.editProfile.EditProfileScreen
import com.example.bersihkan.ui.screen.customer.history.HistoryScreen
import com.example.bersihkan.ui.screen.customer.home.HomeScreen
import com.example.bersihkan.ui.screen.customer.order.OrderScreen
import com.example.bersihkan.ui.screen.customer.profile.ProfileScreen
import com.example.bersihkan.ui.screen.customer.statistics.StatisticsScreen
import com.example.bersihkan.ui.screen.general.about.AboutScreen
import com.example.bersihkan.ui.screen.general.login.LoginScreen
import com.example.bersihkan.ui.screen.general.register.RegisterScreen
import com.example.bersihkan.ui.screen.general.welcomeScreen.WelcomePageOne
import com.example.bersihkan.ui.screen.general.welcomeScreen.WelcomePageTwo
import com.example.bersihkan.ui.theme.BersihKanTheme
import com.example.bersihkan.ui.theme.BlueLagoon
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bersihkan.data.di.Injection
import com.example.bersihkan.ui.screen.ViewModelFactory
import com.example.bersihkan.ui.screen.collector.delivery.DeliveryCollectorScreen
import com.example.bersihkan.ui.screen.collector.detailHistory.DetailCollectorScreen
import com.example.bersihkan.ui.screen.collector.editProfile.EditProfileCollectorScreen
import com.example.bersihkan.ui.screen.collector.history.HistoryCollectorScreen
import com.example.bersihkan.ui.screen.collector.home.HomeCollectorScreen
import com.example.bersihkan.ui.screen.collector.profile.ProfileCollectorScreen
import com.example.bersihkan.ui.screen.customer.search.SearchScreen
import com.example.bersihkan.utils.UserRole

@Composable
fun BersihKanApp(
    navController: NavHostController = rememberNavController(),
    viewModel: BaseViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    modifier: Modifier = Modifier
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    viewModel.getSession()
    val userModel = viewModel.userModel.collectAsState().value

    Scaffold(
        bottomBar = {
            if(currentRoute == Screen.Home.route || currentRoute == Screen.History.route || currentRoute == Screen.Profile.route || currentRoute == Screen.HomeCollector.route || currentRoute == Screen.HistoryCollector.route || currentRoute == Screen.ProfileCollector.route ){
                BottomBar(navController = navController, isCollector = userModel.role == UserRole.COLLECTOR)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = when(userModel.role){
                UserRole.USER -> Screen.Home.route
                UserRole.COLLECTOR -> Screen.HomeCollector.route
                else -> Screen.WelcomeScreen1.route
            },
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
                        navController.navigate(Screen.HomeCollector.route){
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
                        navController.navigate(Screen.HomeCollector.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToWelcomePage1 =  {
                        navController.navigate(Screen.WelcomeScreen1.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToStatistics = {
                        navController.navigate(Screen.Statistics.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToDetail = { id ->
                        navController.navigate(Screen.DetailHistory.createRoute(id))
                    },
                    navigateToDelivery = { id ->
                        navController.navigate(Screen.Delivery.createRoute(id))
                    },
                    navigateToOrder = { lat,lon ->
                        navController.navigate(Screen.Order.createRoute(lat, lon))
                    },
                    navigateToSearch = { location ->
                        navController.navigate(Screen.Search.createRoute(location))
                    }
                )
            }
            composable(Screen.Profile.route){
                ProfileScreen(
                    navigateToEditProfile = {
                        navController.navigate(Screen.EditProfile.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToAbout = {
                        navController.navigate(Screen.About.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToLogout = {
                        navController.navigate(Screen.WelcomeScreen1.route)
                    },
                )
            }
            composable(Screen.History.route){
                HistoryScreen(
                    navigateToDetail =  { orderId ->
                        navController.navigate(Screen.DetailHistory.createRoute(orderId))
                    }
                )
            }
            composable(
                route = Screen.DetailHistory.route,
                arguments = listOf(navArgument("orderId"){ type = NavType.IntType })
            ){
                val id = it.arguments?.getInt("orderId") ?: -1
                DetailScreen(
                    orderId = id,
                    navigateToBack = {
                        navController.navigateUp()
                    })
            }
            composable(Screen.EditProfile.route){
                EditProfileScreen(saveOnClick = {
                    navController.navigate(Screen.Profile.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                    navigateToBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(Screen.About.route){
                AboutScreen {
                    navController.navigateUp()
                }
            }
            composable(Screen.Statistics.route){
                StatisticsScreen(navigateToBack = {
                    navController.navigateUp()
                })
            }
            composable(
                route = Screen.Delivery.route,
                arguments = listOf(navArgument("orderId"){ type = NavType.IntType })
            ){
                val id = it.arguments?.getInt("orderId") ?: -1
                DeliveryScreen(
                    orderId = id,
                    navigateToBack = {
                        Log.d("BersihKanApp", "navigateToHome")
                        navController.navigateUp()
                    })
            }
            composable(
                route = Screen.Order.route,
                arguments = listOf(navArgument("lat"){ type = NavType.FloatType }, navArgument("lon"){ type = NavType.FloatType })
            ){
                val lat = it.arguments?.getFloat("lat") ?: -1f
                val lon = it.arguments?.getFloat("lon") ?: -1f
                OrderScreen(
                    lat = lat,
                    lon = lon,
                    navigateToDelivery = { id ->
                        navController.navigate(Screen.Delivery.createRoute(id))
                    },
                    navigateToBack = {
                        navController.navigateUp()
                    },
                    navigateToHome = {
                        navController.navigate(Screen.Home.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                    )
            }
            composable(
                route = Screen.Search.route,
                arguments = listOf(navArgument("location"){ type = NavType.StringType })
            ){
                val query = it.arguments?.getString("location") ?: ""
                SearchScreen(
                    query = query,
                    navigateToOrder = { lat, lon ->
                        navController.navigate(Screen.Order.createRoute(lat, lon))
                    }
                )
            }
            composable(Screen.HomeCollector.route){
                HomeCollectorScreen(
                    navigateToDetail = { id ->
                        navController.navigate(Screen.DetailHistoryCollector.createRoute(id))
                    },
                    navigateToDelivery = { id ->
                        navController.navigate(Screen.DeliveryCollector.createRoute(id))
                    }
                )
            }
            composable(Screen.HistoryCollector.route){
                HistoryCollectorScreen(
                    navigateToDetail = { id ->
                        navController.navigate(Screen.DetailHistoryCollector.createRoute(id))
                    }
                )
            }
            composable(
                route = Screen.DetailHistoryCollector.route,
                arguments = listOf(navArgument("orderId"){ type = NavType.IntType })
            ){
                val id = it.arguments?.getInt("orderId") ?: -1
                DetailCollectorScreen(
                    orderId = id,
                    navigateToBack = {
                        navController.navigateUp()
                    })
            }
            composable(Screen.ProfileCollector.route){
                ProfileCollectorScreen(
                    navigateToEditProfile = {
                        navController.navigate(Screen.EditProfileCollector.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToAbout = {
                        navController.navigate(Screen.About.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToLogout = {
                        navController.navigate(Screen.WelcomeScreen1.route)
                    }
                )
            }
            composable(Screen.EditProfileCollector.route){
                EditProfileCollectorScreen(
                    saveOnClick = {
                        navController.navigate(Screen.ProfileCollector.route){
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    navigateToBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(
                route = Screen.DeliveryCollector.route,
                arguments = listOf(navArgument("orderId"){ type = NavType.IntType })
            ){
                val id = it.arguments?.getInt("orderId") ?: -1
                DeliveryCollectorScreen(
                    orderId = id,
                    navigateToBack = {
                        navController.navigateUp()
                    })
            }
        }
    }

}

@Composable
fun BottomBar(
    navController: NavHostController,
    isCollector: Boolean = false,
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
                    screen = if(isCollector) Screen.HomeCollector else Screen.Home
                ),
                NavigationItem(
                    title = stringResource(R.string.menu_history),
                    icon = painterResource(id = R.drawable.ic_history),
                    screen = if(isCollector) Screen.HistoryCollector else Screen.History
                ),
                NavigationItem(
                    title = stringResource(R.string.menu_account),
                    icon = painterResource(id = R.drawable.ic_user_profile),
                    screen = if(isCollector) Screen.ProfileCollector else Screen.Profile
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