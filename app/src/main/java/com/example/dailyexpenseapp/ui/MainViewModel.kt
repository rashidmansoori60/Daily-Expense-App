package com.example.dailyexpenseapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Delete
import com.example.dailyexpenseapp.data.local.Entity
import com.example.dailyexpenseapp.data.local.Repostory.Repostary
import com.example.dailyexpenseapp.ui.Uistate.Uistate
import com.example.dailyexpenseapp.ui.model.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val repostary: Repostary): ViewModel() {

    private val _income= MutableStateFlow(0.00)

    val income: StateFlow<Double> get() = _income.asStateFlow()

    private val _expense= MutableStateFlow(0.00)

    val expense: StateFlow<Double> get() = _expense.asStateFlow()

    private val _balance= MutableStateFlow(0.00)
    val balance: StateFlow<Double> get() = _balance.asStateFlow()

    private val _toast = MutableSharedFlow<String>()
    val toast  =_toast.asSharedFlow()

    private val _toastsave = MutableSharedFlow<String>()
    val toastsave  =_toastsave.asSharedFlow()


    private val _list= MutableStateFlow<Uistate<List<Expense>>>(Uistate.Loading)

    val list: StateFlow<Uistate<List<Expense>>> get() = _list.asStateFlow()

    init {
        loadIncome()
        loadExpense()
        loadBalance()
        getTranList()
    }


    suspend fun uploadTran(entity: Entity){

        try {
            repostary.Insert(entity)
            _toastsave.emit("Successful Saved!")

        }
        catch (e: Exception){
            _toastsave.emit("Failed to save: ${e.message ?: "Unknown Error"}")
        }
    }

     fun deletefromdb(entity: Entity){
         viewModelScope.launch {
             try {
                 repostary.delete(entity)
                 _toast.emit("Deleted!")
             } catch (e: Exception) {
                 _toast.emit("Failed to delete please try again,"+e.message.toString())
             }
         }
    }

     fun loadIncome(){
         viewModelScope.launch {
             repostary.getIncome().collect { it->
                 _income.value = it?:0.00
             }
         }
    }

     fun loadExpense(){
         viewModelScope.launch {
        repostary.getExpense().collect { it ->
            _expense.value=it?:0.00
        }}
    }

     fun loadBalance(){
         viewModelScope.launch {
        repostary.getBalance().collect { it ->
            _balance.value=it?:0.00
        }}
         
    }


      fun getTranList(){
         viewModelScope.launch {
         repostary.getAll()
             .onStart {
                 _list.value= Uistate.Loading
                 delay(1000)
             }
            .catch { it ->
            _list.value= Uistate.Error(it.message.toString())
            }
            .collect { it ->
                _list.value= Uistate.Success(data = it, showNoData=it.isEmpty())}
         }
     }

    fun getsorttran(date: String){
        viewModelScope.launch {
            repostary.getsort(date)
                .onStart { _list.value= Uistate.Loading
                delay(1000)
                }
                .catch {it->
                  _list.value=Uistate.Error(it.message.toString()) }
                .collect { it->
                    _list.value= Uistate.Success(data = it, showNoData=it.isEmpty())}
                }
        }
    }

