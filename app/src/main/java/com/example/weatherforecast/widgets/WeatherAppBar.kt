package com.example.weatherforecast.widgets

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.model.Favourite
import com.example.weatherforecast.navigation.WeatherScreens
import com.example.weatherforecast.screens.favourites.FavouriteViewModel

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

    val showIt = remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    if (showDialogue.value){
        ShowSettingDropDownMenu(showDialogue = showDialogue,
            navController = navController)
    }

    TopAppBar(
        title = {
            Text(text = title,
                style = TextStyle(fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp))
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
            }else Box {}
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
                val isAlreadyFavList = favouriteViewModel
                    .favList.collectAsState().value.filter { item ->
                        (item.city == title.split(",")[0])
                    }

                if (isAlreadyFavList.isEmpty()){
                    Icon(imageVector = Icons.Default.Favorite,
                        contentDescription = "Add Favourite Icon",
                        modifier = Modifier
                            .scale(0.9f)
                            .clickable {
                                val dataList = title.split(",")
                                favouriteViewModel.insertFavourite(
                                    Favourite(
                                        city = dataList[0],   //city name
                                        country = dataList[1]  //country code
                                    )).run {
                                    showIt.value = true
                                }
                            },
                        tint = Color.White)
                }
                else{
                    Icon(imageVector = Icons.Default.Favorite,
                        contentDescription = "Delete Favourite Icon",
                        modifier = Modifier
                            .scale(0.9f)
                            .clickable {

                            },
                        tint = Color.Red).run {
                        showIt.value = false
                    }
                }
                ShowToast(context = context, showIt)
            }
        },
        backgroundColor = Color(0xFF071335),
        elevation = elevation
    )
}


@Composable
fun ShowToast(context: Context, showIt: MutableState<Boolean>) {
    if (showIt.value){
        Toast.makeText(context, "Added to Favourites",
            Toast.LENGTH_SHORT).show()
    }
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
        .absolutePadding(top = 45.dp, right = 20.dp)) {
        DropdownMenu(expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(140.dp)
                .background(Color(0xFF071335))) {
            items.forEachIndexed { index, text ->
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
