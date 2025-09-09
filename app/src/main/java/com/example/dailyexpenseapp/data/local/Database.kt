package com.example.dailyexpenseapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entity::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun getdao(): Dao

}