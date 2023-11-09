package com.example.weatherforecast.screens.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.data.DataOrException
import com.example.weatherforecast.model.Weather
import com.example.weatherforecast.model.WeatherItem
import com.example.weatherforecast.navigation.WeatherScreens
import com.example.weatherforecast.screens.settings.SettingsViewModel
import com.example.weatherforecast.utils.formatDate
import com.example.weatherforecast.utils.formatDecimals
import com.example.weatherforecast.widgets.*

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    city: String?
){
    Log.d("TAG", "MainScreen: $city")

    val unitFromDB = settingsViewModel.unitList.collectAsState().value
    var unit by remember{
        mutableStateOf("metric")
    }
    var isMetric by remember {
        mutableStateOf(true)
    }

    if(unitFromDB.isNotEmpty()) {
        unit = unitFromDB[0].unit
        isMetric = unit == "metric"
    }

    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)) {
        value = mainViewModel.getWeatherData(city = city.toString(), units = unit)
    }.value

    if (weatherData.loading == true){
        CircularProgressIndicator()
    }else if (weatherData.data != null){
        MainScaffold(weather = weatherData.data!!, navController, isMetric = isMetric)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScaffold(weather: Weather, navController: NavController, isMetric: Boolean) {

    Scaffold(topBar = {
        WeatherAppBar(title = weather.city.name+", "+weather.city.country,
            navController = navController,
            onAddActionClicked = {
                navController.navigate(WeatherScreens.SearchScreen.name)
            },
            elevation = 5.dp){
            Log.d("TAG", "MainScaffold: Button Clicked")
        }
    }) {
        MainContent(data = weather, isMetric = isMetric)
    }
}


@Composable
fun MainContent(data: Weather, isMetric: Boolean) {

    val weatherItem = data.list[0]

    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"

    val unit = if(isMetric){
        "°C"
    }
    else{
        "°F"
    }

    Column(
        Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = formatDate(weatherItem.dt),
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(6.dp)
        )

        Surface(modifier = Modifier
            .padding(4.dp)
            .size(200.dp),
            shape = CircleShape,
            color = Color(0xFFE0C61A)
        ) {

            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                WeatherStateImage(imageUrl = imageUrl)

                Text(text = formatDecimals(weatherItem.temp.day) + unit,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(text = weatherItem.weather[0].main,
                    color = MaterialTheme.colors.onPrimary,
                    fontStyle = FontStyle.Italic)
            }

        }

        HumidityWindPressureRow(weather = weatherItem)

        Divider()

        SunriseAndSunsetRow(weather = weatherItem)

        Text(text = "This Week",
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(5.dp)
        )

        Surface(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
            shape = RoundedCornerShape(20.dp)) {

            LazyColumn(modifier = Modifier
                .background(color = MaterialTheme.colors.onPrimary)){
                items(items = data.list){ item: WeatherItem ->
                    WeatherDetailsRow(weather = item, unit = unit)
                }
            }
        }
    }
}

