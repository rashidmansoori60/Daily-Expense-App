package com.example.dailyexpenseapp.ui


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyexpenseapp.R
import com.example.dailyexpenseapp.databinding.ActivityMainBinding
import com.example.dailyexpenseapp.ui.Uistate.Uistate
import com.example.dailyexpenseapp.ui.fragment.AddFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.transition.Visibility
import com.example.dailyexpenseapp.ui.fragment.BottomSeetFragment
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var adapter: MainAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Expense Tracker"

        setupUI()
        setupCollectors()
        setupListeners()
    }

    private fun setupUI() {
        adapter = MainAdapter(emptyList())
        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionRecyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.sortBy.setOnClickListener {
            BottomSeetFragment().show(supportFragmentManager, "sortbottomsheet")
        }

        binding.addtransaction.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddFragment())
                .addToBackStack(null)
                .commit()

            binding.fragmentContainer.visibility = View.VISIBLE
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val isFragmentVisible = supportFragmentManager.backStackEntryCount > 0
            binding.addtransaction.visibility = if (isFragmentVisible) View.GONE else View.VISIBLE
        }
    }

    private fun setupCollectors() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.expense.collect {
                        binding.tvExpense.text = it.toCurrency()
                    }
                }
                launch {
                    mainViewModel.balance.collect {
                        binding.tvBalance.text = it.toCurrency()
                    }
                }
                launch {
                    mainViewModel.income.collect {
                        binding.tvIncome.text = it.toCurrency()
                    }
                }
                launch {
                    mainViewModel.toast.collect {
                        Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        lifecycleScope.launch {
            mainViewModel.list.collect { uiState ->
                when (uiState) {
                    is Uistate.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Uistate.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext, uiState.message, Toast.LENGTH_LONG).show()
                    }
                    is Uistate.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if(uiState.showNoData){
                        binding.noTranFound.isVisible=true
                            binding.transactionRecyclerView.visibility= View.GONE
                        }
                        else{
                            adapter.update(uiState.data)
                            binding.noTranFound.isVisible = false
                            binding.transactionRecyclerView.visibility= View.VISIBLE

                        }
                    }
                }
            }
        }
    }


    private fun Double.toCurrency(): String {
        return if (this < 0) {
            "-₹%.2f".format(-this)
        } else {
            "₹%.2f".format(this)
        }
    }
}
