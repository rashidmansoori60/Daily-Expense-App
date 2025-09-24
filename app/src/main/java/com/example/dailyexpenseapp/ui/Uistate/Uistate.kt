package com.example.dailyexpenseapp.ui.Uistate

sealed interface Uistate<out T> {
    data object Loading: Uistate<Nothing>
    data class Success<T>(var data: T,var showNoData: Boolean):Uistate<T>
    data class Error(var message: String):Uistate<Nothing>

}