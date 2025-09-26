package com.example.dailyexpenseapp.ui.model

data class Expense(
    var id: Int=-1,
    val title: String= "",
    val amount: Double =0.0,
    val type: String="",
    val notes: String= "",
    var date: String= "",
    var time: String= "",
)