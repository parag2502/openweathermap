package com.intelegencia.repositories.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "weatherdata"
)

data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "brief")
    val brief: String,
    @ColumnInfo(name = "description")
    val desc: String,
    @ColumnInfo(name = "temperature")
    val temp: String,
    @ColumnInfo(name = "city")
    val city: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "humidity")
    val humidity: String,
    @ColumnInfo(name = "wind")
    val wind: String
)
