package com.example.dailyexpenseapp.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.dailyexpenseapp.R
import com.example.dailyexpenseapp.databinding.FragmentBottomSeetBinding
import com.example.dailyexpenseapp.ui.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
@AndroidEntryPoint
class BottomSeetFragment : BottomSheetDialogFragment() {

   lateinit var binding: FragmentBottomSeetBinding
   private val mainViewModel: MainViewModel by activityViewModels()



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBottomSeetBinding.inflate(inflater,container,false)

        binding.btnall.setOnClickListener {
          mainViewModel.getTranList()
            dismiss()
        }


        binding.btnToday.setOnClickListener {
            val today= LocalDate.now()
            val formetter= DateTimeFormatter.ofPattern("EEE, dd MMM yyyy", Locale.getDefault())
            val date= today.format(formetter)
            mainViewModel.getsorttran(date)
            dismiss()
        }


        binding.btnCalendar.setOnClickListener {

           val datepicker= MaterialDatePicker.Builder.datePicker().setTitleText("Select date").build()

            datepicker.show(parentFragmentManager,"Datepicker")
            datepicker.addOnPositiveButtonClickListener { it->
                val sdf= SimpleDateFormat("EEE, dd MMM yyyy",Locale.getDefault())
                val date= Date(it)
                val formatteddate=sdf.format(date)

                mainViewModel.getsorttran(formatteddate)

                dismiss()
            }
        }

        return binding.root
    }


}