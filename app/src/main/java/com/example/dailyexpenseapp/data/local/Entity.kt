package com.example.dailyexpenseapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Entity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String? = "",
    var amount: Double? =0.0,
    var type: String? = "",
    var notes: String? ="",
    var date: String? = "",
    var time: String? = ""
)
