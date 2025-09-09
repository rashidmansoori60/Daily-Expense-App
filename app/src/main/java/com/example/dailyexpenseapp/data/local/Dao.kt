package com.example.dailyexpenseapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    suspend fun insert(entity: Entity)

    @Delete
    suspend fun delete(entity: Entity)

    @Query("SELECT SUM(amount) from Entity WHERE type='income'")
     fun getincome(): Flow<Double?>

    @Query("SELECT SUM(amount) from Entity WHERE type='expense'")
     fun getexpense(): Flow<Double?>

    @Query("SELECT IFNULL((SELECT SUM(amount) FROM Entity WHERE type = 'income'), 0) - IFNULL((SELECT SUM(amount) FROM Entity WHERE type = 'expense'), 0)")
     fun getBalance(): Flow<Double?>

    @Query("SELECT * from Entity")
     fun getListtran(): Flow<List<Entity>>


}