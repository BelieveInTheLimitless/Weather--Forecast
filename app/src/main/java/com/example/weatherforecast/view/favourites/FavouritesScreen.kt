package com.example.weatherforecast.view.favourites

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.model.Favourite
import com.example.weatherforecast.view.WeatherScreens
import com.example.weatherforecast.viewmodel.FavouriteViewModel
import com.example.weatherforecast.view.widgets.WeatherAppBar

@Composable
fun FavouritesScreen(navController: NavController,
                     favouriteViewModel: FavouriteViewModel = hiltViewModel()){
    Scaffold(topBar = { WeatherAppBar(
        title = "Favourite Cities",
        icon = Icons.Default.ArrowBack,
        isMainScreen = false,
        navController = navController
    ){ navController.popBackStack() }
    }) {
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {

            val list = favouriteViewModel.favList.collectAsState().value

            if (list.isEmpty()){
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "No favourites added yet!")
                }
            }
            else{
                LazyColumn(horizontalAlignment = Alignment.CenterHorizontally){
                    items(items = list){favourite ->
                        CityRow(favourite = favourite,
                            navController = navController,
                            favouriteViewModel = favouriteViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun CityRow(favourite: Favourite,
            navController: NavController,
            favouriteViewModel: FavouriteViewModel
) {
    Surface(
        Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(50.dp)
            .clickable {
                navController.navigate(WeatherScreens.MainScreen.name + "/${favourite.city}")
            },
        shape = CircleShape,
        color = Color(0xFF071335)
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = favourite.city, modifier = Modifier.padding(start = 4.dp))

            Surface(modifier = Modifier.padding(0.dp),
                shape = CircleShape,
                color = Color(0xFFE0C61A)
            ) {
                Text(text = favourite.country,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.caption)
            }

            val context = LocalContext.current

            Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete",
                modifier = Modifier.clickable {
                    favouriteViewModel.deleteFavourite(favourite)
                    Toast.makeText(
                        context, "Deleted from Favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                tint = Color.Red.copy(alpha = 0.3f))
        }
    }
}
