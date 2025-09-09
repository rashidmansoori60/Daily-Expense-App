package com.example.dailyexpenseapp.ui


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

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    val mainViewModel: MainViewModel by viewModels()
    lateinit var adapter: MainAdapter
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title ="Expence tracker"


        mainViewModel.expense.observe(this){ value ->
            binding.tvExpense.text=value.toString()
        }
        mainViewModel.balance.observe(this){ value ->
            binding.tvBalance.text=value.toString()
        }
        mainViewModel.income.observe(this){value ->
            binding.tvIncome.text=value.toString()
        }

        adapter= MainAdapter(emptyList())

        lifecycleScope.launch{
            mainViewModel.list.collect { it ->
                when(it) {
                    is Uistate.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE

                    }

                    is Uistate.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(applicationContext,it.message, Toast.LENGTH_LONG).show()
                    }

                    is Uistate.Success -> {
                        binding.progressBar.visibility = View.GONE
                        if(it.data.isEmpty()){
                            binding.noTranFound.visibility= View.VISIBLE
                        }else{
                        adapter.update(it.data)
                        }

                    }
                }
            }

        }

        binding.transactionRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.transactionRecyclerView.adapter=adapter

        binding.addtransaction.setOnClickListener {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddFragment())
                .addToBackStack(null)
                .commit()

            binding.fragmentContainer.visibility= View.VISIBLE


        }
        supportFragmentManager.addOnBackStackChangedListener {
            val isFragmentVisible = supportFragmentManager.backStackEntryCount > 0
            if (isFragmentVisible) {
                binding.addtransaction.visibility = View.GONE
            } else {
                binding.addtransaction.visibility = View.VISIBLE
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.toast.collect {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    }
