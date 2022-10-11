package com.example.weatherforecast.widgets

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.weatherforecast.R
import com.example.weatherforecast.model.WeatherItem
import com.example.weatherforecast.utils.formatDate
import com.example.weatherforecast.utils.formatDateTime
import com.example.weatherforecast.utils.formatDecimals


@Composable
fun WeatherDetailsRow(weather: WeatherItem) {

    val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}.png"

    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth()
        .fillMaxHeight(),
        shape = CircleShape.copy(),
        color = Color(0xFF071335)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            Text(text = formatDate(weather.dt)
                .split(",")[0],
                modifier = Modifier.padding(start = 15.dp),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.SemiBold,

                )

            WeatherStateImage(imageUrl = imageUrl)

            Surface(modifier = Modifier.padding(0.dp),
                shape = CircleShape,
                color = Color(0xFFE0C61A)
            ) {
                Text(text = weather.weather[0].description,
                    modifier = Modifier
                        .padding(4.dp),
                    color = Color.Black,
                    style = MaterialTheme.typography.caption
                )
            }

            Text(text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.Yellow.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold
                )
                ){
                    append(formatDecimals(weather.temp.max) + "°C")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.LightGray)
                ){
                    append(formatDecimals(weather.temp.min) + "°C")
                }
            }, modifier = Modifier.padding(end = 15.dp))
        }

    }
}

@Composable
fun SunriseAndSunsetRow(weather: WeatherItem) {
    Row(modifier = Modifier
        .padding(top = 15.dp, bottom = 6.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Row(modifier = Modifier.padding(4.dp)) {
            Icon(
                painter = painterResource(R.drawable.sunrise),
                contentDescription = "sunrise icon",
                modifier = Modifier.size(30.dp)
            )

            Text(
                text = formatDateTime(weather.sunrise),
                style = MaterialTheme.typography.caption
            )
        }

        Row{
            Text(
                text = formatDateTime(weather.sunset),
                style = MaterialTheme.typography.caption
            )

            Icon(
                painter = painterResource(R.drawable.sunset),
                contentDescription = "sunset icon",
                modifier = Modifier.size(30.dp)
            )
        }
    }

}

@Composable
fun HumidityWindPressureRow(weather: WeatherItem) {
    Row(modifier = Modifier
        .padding(12.dp)
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Row(modifier = Modifier.padding(4.dp)) {
            Icon(painter = painterResource(R.drawable.humidity),
                contentDescription = "humidity icon",
                modifier = Modifier.size(20.dp))

            Text(text = " ${weather.humidity}%",
                style = MaterialTheme.typography.caption)
        }

        Row{
            Icon(painter = painterResource(R.drawable.pressure),
                contentDescription = "pressure icon",
                modifier = Modifier.size(20.dp))

            Text(text = " ${weather.pressure}Pa",
                style = MaterialTheme.typography.caption)
        }

        Row{
            Icon(painter = painterResource(R.drawable.wind),
                contentDescription = "wind icon",
                modifier = Modifier.size(20.dp))

            Text(text = " ${weather.speed}km/h",
                style = MaterialTheme.typography.caption)
        }

    }
}

@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(painter = rememberImagePainter(imageUrl),
        contentDescription = "icon image" ,
        modifier = Modifier.size(80.dp))

    }
