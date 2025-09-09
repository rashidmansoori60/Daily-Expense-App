package com.example.dailyexpenseapp.data.local.Repostory

import com.example.dailyexpenseapp.data.local.Dao
import com.example.dailyexpenseapp.data.local.Entity
import com.example.dailyexpenseapp.ui.mapper.ExpenseListMapper
import com.example.dailyexpenseapp.ui.mapper.ExpenseMapper
import com.example.dailyexpenseapp.ui.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImp @Inject constructor(val dao: Dao,private val expenseListMapper: ExpenseListMapper): Repostary {
    override suspend fun getIncome(): Flow<Double?> {
      return  dao.getincome()
    }

    override suspend fun getExpense(): Flow<Double?> {
        return dao.getexpense()
    }

    override suspend fun Insert(entity: Entity) {
        withContext(Dispatchers.IO){
        dao.insert(entity)
        }
    }

    override suspend fun delete(entity: Entity) {
        dao.delete(entity)
    }

    override suspend fun getBalance(): Flow<Double?> {
      return dao.getBalance()
    }

    override suspend fun getAll(): Flow<List<Expense>> =
          flow {
              emit(emptyList())
              dao.getListtran().collect { it ->
                  delay(1000)
                emit(expenseListMapper.map(it))
            }


        }.flowOn(Dispatchers.IO)



}