package com.example.weatherforecast.model.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherforecast.model.Favourite
import com.example.weatherforecast.model.Unit

@Database(entities = [Favourite::class, Unit::class], version = 2, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}