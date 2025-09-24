package com.example.dailyexpenseapp.data.local.Repostory

import com.example.dailyexpenseapp.data.local.Entity
import com.example.dailyexpenseapp.ui.model.Expense
import kotlinx.coroutines.flow.Flow

interface Repostary {

    suspend fun getIncome(): Flow<Double?>

    suspend fun getExpense(): Flow<Double?>

    suspend fun Insert(entity: Entity)

    suspend fun delete(entity: Entity)

    suspend fun getBalance(): Flow<Double?>

    suspend fun getAll(): Flow<List<Expense>>

    suspend fun getsort(date:String): Flow<List<Expense>>


}