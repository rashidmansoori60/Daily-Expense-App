package com.example.dailyexpenseapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String? = null,
    var amount: Double? = null,
    var type: String? = null,
    var notes: String? = null,
    var date: String? = null,
    var time: String? = null
)
