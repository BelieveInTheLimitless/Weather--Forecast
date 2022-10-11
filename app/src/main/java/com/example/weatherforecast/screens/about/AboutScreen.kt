package com.example.weatherforecast.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.weatherforecast.widgets.WeatherAppBar

@Composable
fun AboutScreen(navController: NavController){
   Scaffold(topBar = {
       WeatherAppBar(
           title = "About",
           icon = Icons.Default.ArrowBack,
           isMainScreen = false,
           navController = navController){
           navController.popBackStack()
       }
   }) {
       Surface(modifier = Modifier
           .fillMaxWidth()
           .fillMaxHeight()) {
           Column(horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center) {
               Text(text = "Weather App v.1.0",
                   style = MaterialTheme.typography.subtitle1,
                       fontWeight = FontWeight.Bold
               )
               Text(text = "Weather API by: https://openweathermap.org",
                   style = MaterialTheme.typography.subtitle1,
                   fontWeight = FontWeight.Light)
           }
       }
   }
}