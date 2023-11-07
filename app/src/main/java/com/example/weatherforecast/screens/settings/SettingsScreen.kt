package com.example.weatherforecast.screens.settings

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItemDefaults.containerColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecast.model.Unit
import com.example.weatherforecast.widgets.WeatherAppBar


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()){
    Scaffold(topBar = {
        WeatherAppBar(title = "Settings",
            icon = Icons.Default.ArrowBack,
            isMainScreen = false,
            navController = navController){
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Change Unit of Measurement",
                    modifier = Modifier.padding(bottom = 25.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.Serif,
                    style = MaterialTheme.typography.subtitle1
                )

                val context = LocalContext.current
                val units = listOf("Celsius °C", "Fahrenheit °F")
                val unitFromDB = settingsViewModel.unitList.collectAsState().value

                val defaultChoice = if (unitFromDB.isEmpty()) "metric" else unitFromDB[0].unit
                var selectedUnit by remember {
                    mutableStateOf(
                        if(defaultChoice == "metric") "Celsius °C" else "Fahrenheit °F")
                }
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.width(175.dp)
                ) {
                    OutlinedTextField(
                        value = selectedUnit,
                        onValueChange = {
                            selectedUnit = it
                        },
                        modifier = Modifier.menuAnchor(),
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = containerColor,
                            unfocusedContainerColor = containerColor,
                            disabledContainerColor = containerColor,
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(
                                Color(0xFF071335)
                            )
                    ) {
                        units.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item, color = Color(0xFFE0C61A), fontSize = 15.sp) },
                                onClick = {
                                    selectedUnit = item
                                    expanded = false

                                    settingsViewModel.deleteAllUnits()

                                    if(selectedUnit == "Celsius °C"){
                                        settingsViewModel.insertUnit(Unit(unit = "metric"))
                                    }
                                    else{
                                        settingsViewModel.insertUnit(Unit(unit = "imperial"))
                                    }

                                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}