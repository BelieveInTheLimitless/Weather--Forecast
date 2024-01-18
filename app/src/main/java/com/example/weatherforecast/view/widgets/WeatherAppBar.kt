package com.example.weatherforecast.view.widgets

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.model.Favourite
import com.example.weatherforecast.view.WeatherScreens
import com.example.weatherforecast.viewmodel.FavouriteViewModel

@Composable
fun WeatherAppBar(
    title: String = "Title",
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 0.dp,
    navController: NavController,
    favouriteViewModel: FavouriteViewModel = hiltViewModel(),
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {}) {

    val showDialogue = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    if (showDialogue.value){
        ShowSettingDropDownMenu(showDialogue = showDialogue,
            navController = navController)
    }

    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                Text(text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        },
        modifier = Modifier.padding(7.dp),
        actions = {
            if(isMainScreen) {
                IconButton(onClick = {
                    onAddActionClicked.invoke()
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                }
                IconButton(onClick = {
                    showDialogue.value = true
                }) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = "More Icon"
                    )
                }
            }
            else {
                IconButton(onClick = { /*TODO*/ },
                    modifier = Modifier.padding(7.dp)) {
                    Icon(imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.Transparent)
                }
            }
        },
        navigationIcon = {
            if (icon != null){
                Icon(imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.clickable {
                        onButtonClicked.invoke()
                    })
            }
            if (isMainScreen){
                val isAlreadyFavourite = favouriteViewModel
                    .favList.collectAsState().value.filter { item ->
                        (item.city == title.split(",")[0])
                    }

                val dataList = title.split(",")

                if (isAlreadyFavourite.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            favouriteViewModel.deleteFavourite(
                                Favourite(
                                    city = dataList[0],   //city name
                                    country = dataList[1]  //country code
                                )
                            )
                            Toast.makeText(
                                context, "Deleted from Favourites",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = "Favourites Icon"
                        )
                    }
                }
                else{
                    IconButton(
                        onClick = {
                            favouriteViewModel.insertFavourite(
                                Favourite(
                                    city = dataList[0],   //city name
                                    country = dataList[1]  //country code
                                ))
                            Toast.makeText(
                                context, "Added to Favourites",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favourites Icon"
                        )
                    }
                }
            }
        },
        backgroundColor = Color(0xFF071335),
        elevation = elevation
    )
}

@Composable
fun ShowSettingDropDownMenu(showDialogue: MutableState<Boolean>,
                            navController: NavController) {

    var expanded by remember {
        mutableStateOf(true)
    }
    val items = listOf("About", "Favourites", "Settings")

    Column(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize(Alignment.TopEnd)
        .absolutePadding(top = 50.dp, right = 30.dp)) {
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(150.dp)
                .background(Color(0xFF071335))) {
            items.forEachIndexed { _, text ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    showDialogue.value = false
                }) {
                    Icon(imageVector = when(text){
                        "About" -> Icons.Default.Info
                        "Favourites" -> Icons.Default.Favorite
                        else -> Icons.Default.Settings
                    },
                        contentDescription = null,
                        tint = Color.White)
                    Text(text = text,
                        modifier = Modifier.clickable {
                            navController.navigate(
                                when(text){
                                    "About" -> WeatherScreens.AboutScreen.name
                                    "Favourites" -> WeatherScreens.FavouriteScreen.name
                                    else -> WeatherScreens.SettingsScreen.name
                                }
                            )
                        }, fontWeight = FontWeight.W300)
                }
            }
        }
    }
}
