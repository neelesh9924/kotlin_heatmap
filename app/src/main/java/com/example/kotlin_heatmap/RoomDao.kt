package com.example.kotlin_heatmap

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dbData: DBData)

    @Query("SELECT * FROM dataTable")
    fun getAll(): Flow<List<DBData>>

}