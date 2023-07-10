package com.example.kotlin_heatmap

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "dataTable")
data class DBData(
    @PrimaryKey (autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "dateAdded") val date: Date,
    @ColumnInfo(name = "modal") val responseJson: String
)
