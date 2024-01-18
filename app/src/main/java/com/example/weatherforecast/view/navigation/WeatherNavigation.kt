package com.example.weatherforecast.view.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.weatherforecast.view.WeatherScreens
import com.example.weatherforecast.view.about.AboutScreen
import com.example.weatherforecast.view.favourites.FavouritesScreen
import com.example.weatherforecast.view.main.MainScreen
import com.example.weatherforecast.viewmodel.MainViewModel
import com.example.weatherforecast.view.search.SearchScreen
import com.example.weatherforecast.view.settings.SettingsScreen
import com.example.weatherforecast.view.splash.WeatherSplashScreen

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name){
        composable(WeatherScreens.SplashScreen.name){
            WeatherSplashScreen(navController = navController)
        }

        val route = WeatherScreens.MainScreen.name
        composable("$route/{city}",
            arguments = listOf(
                navArgument(name = "city"){
                    type = NavType.StringType
                })){ navBack ->
                   navBack.arguments?.getString("city").let { city ->

                       val mainViewModel = hiltViewModel<MainViewModel>()
                       MainScreen(navController = navController, mainViewModel,
                       city = city)
                   }
        }

        composable(WeatherScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }

        composable(WeatherScreens.AboutScreen.name){
            AboutScreen(navController = navController)
        }
        composable(WeatherScreens.SettingsScreen.name){
            SettingsScreen(navController = navController)
        }
        composable(WeatherScreens.FavouriteScreen.name){
            FavouritesScreen(navController = navController)
        }
    }
}