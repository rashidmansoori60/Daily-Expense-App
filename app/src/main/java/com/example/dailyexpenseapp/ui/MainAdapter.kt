package com.example.dailyexpenseapp.ui
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyexpenseapp.databinding.ItemBinding
import com.example.dailyexpenseapp.ui.mapper.EntityMapper
import com.example.dailyexpenseapp.ui.model.Expense
import javax.inject.Inject

class MainAdapter @Inject constructor(private var lists:List<Expense>,private val deleteInterface: DeleteInterface,private val entityMapper: EntityMapper) : RecyclerView.Adapter<MainAdapter.MainViewHoler>(){

    fun update(list: List<Expense>){

            this.lists=list
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

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete!")
                .setMessage("Are you sure want to delete?")
                .setPositiveButton("Delete") { dialog,_ ->
                      deleteInterface.deleteentity(entityMapper.map(list))
                    dialog.dismiss()

                }.setNegativeButton("Cancle"){dialog,_ ->
                    dialog.dismiss()

                }.show()
                true
        }
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