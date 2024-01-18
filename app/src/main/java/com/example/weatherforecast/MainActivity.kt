package com.example.weatherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherforecast.model.DefaultCity
import com.example.weatherforecast.view.navigation.WeatherNavigation
import com.example.weatherforecast.viewmodel.FavouriteViewModel
import com.example.weatherforecast.ui.theme.WeatherForecastTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherForecastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        WeatherApp()
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherApp(){
    val favouriteViewModel = hiltViewModel<FavouriteViewModel>()
    val list = favouriteViewModel.favList.collectAsState().value

    if (list.isNotEmpty()){
        DefaultCity.city = list[0].city
    }

    WeatherNavigation()
}