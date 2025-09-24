package com.example.dailyexpenseapp.ui.fragment

import android.annotation.SuppressLint
import android.app.FragmentManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.dailyexpenseapp.R
import com.example.dailyexpenseapp.data.local.Entity
import com.example.dailyexpenseapp.databinding.FragmentAddBinding
import com.example.dailyexpenseapp.ui.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import kotlin.getValue
@AndroidEntryPoint
class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.btnSave.setOnClickListener {
            handleSave()
        }

        binding.etDate.setOnClickListener {
            val datepicker= MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()

            datepicker.show(parentFragmentManager,"MaterialDatePicker")

            datepicker.addOnPositiveButtonClickListener { it ->
                val sdf= SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
                val date= Date(it)

                binding.etDate.setText(sdf.format(date).toString())
            }

        }
        binding.etTime.setOnClickListener {
            val timepicker= MaterialTimePicker.Builder()
                .setTitleText("Select time")
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .build()

            timepicker.show(parentFragmentManager,"MaterialTimePicker")

            timepicker.addOnPositiveButtonClickListener {

                val hour=timepicker.hour
                val minute=timepicker.minute
                val ampm=if(hour>=12)"pm" else "am"

                val formateHour=if(hour>12)hour-12 else if(hour==0)12 else hour
                val formateMinute=String.format("%02d" , minute)

                binding.etTime.setText("$formateHour:$formateMinute $ampm")


            }


        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            parentFragmentManager.popBackStack()
            activity?.findViewById<View>(R.id.fragment_container)?.visibility= View.GONE
        }

    }


    @SuppressLint("RepeatOnLifecycleWrongUsage")
    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                mainViewModel.toastsave.collect { it ->
                    Toast.makeText(requireContext(),it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun handleSave() {
        val title = binding.etTitle.text.toString().trim()
        val amountText = binding.etAmount.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val time = binding.etTime.text.toString().trim()         // Optional
        val notes = binding.etNotes.text.toString().trim()       // Optional
        val type = getSelectedType()

        var isValid = true

        // Validate required fields
        if (title.isEmpty()) {
            binding.etTitle.error = "Please enter title"
            isValid = false
        }

        if (date.isEmpty()) {
            binding.etDate.error = "Please enter date"
            isValid = false
        }

        if (amountText.isEmpty()) {
            binding.etAmount.error = "Please enter amount"
            isValid = false
        }

        val amount = amountText.toDoubleOrNull()
        if (amountText.isNotEmpty() && amount == null) {
            binding.etAmount.error = "Invalid amount"
            isValid = false
        }

        if (type.isEmpty()) {
            Toast.makeText(requireContext(), "Please select type!", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Submit if all valid
        if (isValid && amount != null) {
            val entity = Entity(
                title = title,
                amount = amount,
                type = type,
                date = date,
                time = time,
                notes = notes
            )

            lifecycleScope.launch {
                mainViewModel.uploadTran(entity)
                clearFields() // optional: clear form after save


            }
        }
    }

    private fun getSelectedType(): String {
        return when {
            binding.radioIncome.isChecked ->"income"
            binding.radioExpense.isChecked -> "expense"
            else -> ""
        }
    }


    private fun clearFields() {
        binding.etTitle.text?.clear()
        binding.etAmount.text?.clear()
        binding.etDate.text?.clear()
        binding.etTime.text?.clear()
        binding.etNotes.text?.clear()
        binding.radioIncome.isChecked = true  // default reset
    }


}
