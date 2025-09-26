package com.example.dailyexpenseapp.ui.mapper

import com.example.dailyexpenseapp.Core.ListMapper
import com.example.dailyexpenseapp.Core.Mapper
import com.example.dailyexpenseapp.data.local.Entity
import com.example.dailyexpenseapp.ui.model.Expense
import javax.inject.Inject

class EntityMapper @Inject constructor(): Mapper<Expense, Entity> {
    override fun map(input: Expense)= Entity(
        id=input.id,
        title = input.title,
        amount = input.amount,
        type = input.type,
        notes = input.notes,
        date = input.date,
        time = input.time
    )


}
class EntityListMapper @Inject constructor(private val entityMapper: EntityMapper): ListMapper<Expense,Entity> {
    override fun map(input: List<Expense>): List<Entity> {

        return input.map{ entityMapper.map(it) }

    }
}