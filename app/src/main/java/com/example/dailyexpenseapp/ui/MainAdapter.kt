package com.example.dailyexpenseapp.ui
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyexpenseapp.databinding.ItemBinding
import com.example.dailyexpenseapp.ui.model.Expense
import javax.inject.Inject

class MainAdapter @Inject constructor(var lists:List<Expense>) : RecyclerView.Adapter<MainAdapter.MainViewHoler>(){

    fun update(list: List<Expense>){
        if(list!=null){
        this.lists=list
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHoler {
        val binding= ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MainViewHoler(binding)
    }

    override fun onBindViewHolder(
        holder: MainViewHoler,
        position: Int
    ) {
         var list=lists[position]
         holder.binding.tvTitle.text=list.title
         holder.binding.tvDateTime.text="${list.date},${list.time}"
         holder.binding.tvType.text=list.type



        when(list.type.lowercase()){
            "income"->{
                      holder.binding.tvAmount.setTextColor(Color.parseColor("#4CAF50")) //Green
                      holder.binding.tvAmount.text= "+₹${list.amount}"
                      holder.binding.tvType.setTextColor(Color.parseColor("#4CAF50"))
            }
            "expense"->{holder.binding.tvAmount.setTextColor(Color.parseColor("#F44336")) //Red
                       holder.binding.tvAmount.text= "-₹${list.amount}"
                       holder.binding.tvType.setTextColor(Color.parseColor("#F44336"))

            }
            else -> {
                holder.binding.tvAmount.setTextColor(Color.BLACK)
                holder.binding.tvAmount.text = "₹${list.amount}"
            }
        }

    }

    override fun getItemCount(): Int {
    return lists.size
    }

    class MainViewHoler(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
}