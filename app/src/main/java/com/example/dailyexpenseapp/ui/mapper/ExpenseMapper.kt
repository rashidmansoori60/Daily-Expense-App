package com.example.dailyexpenseapp.ui.mapper

import com.example.dailyexpenseapp.Core.ListMapper
import com.example.dailyexpenseapp.Core.Mapper
import com.example.dailyexpenseapp.data.local.Entity
import com.example.dailyexpenseapp.ui.model.Expense
import javax.inject.Inject

class ExpenseMapper @Inject constructor(): Mapper<Entity?, Expense> {
    override fun map(input: Entity?) = Expense(
        id = input?.id ?: -1,
        title = input?.title ?: "",
        amount = input?.amount ?: 0.0,
        type = input?.type ?: "",
        notes = input?.notes ?: "",
        date = input?.date ?: "",
        time = input?.time ?: ""

    )
}

    class ExpenseListMapper @Inject constructor(private val expenseMapper: ExpenseMapper): ListMapper<Entity, Expense> {
        override fun map(input: List<Entity>): List<Expense> {

            return input.map{ expenseMapper.map(it) }

        }
    }
