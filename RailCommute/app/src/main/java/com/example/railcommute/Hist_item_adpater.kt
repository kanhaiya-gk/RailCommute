package com.example.railcommute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Hist_item_adpater(private val listener : TicClicked, val mHPB : ProgressBar): RecyclerView.Adapter<ItemViewHolder>() {

    private val tics: ArrayList<Ticket> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_ticket, p0, false)
        val viewHolder = ItemViewHolder(view)
        view.setOnClickListener{
            listener.onTClick(tics[viewHolder.adapterPosition].ticId)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ItemViewHolder, pos: Int) {
        holder.mtrName.text = tics[pos].trName
        holder.mtrId.text = tics[pos].trId.toString()
        holder.mboSt.text = tics[pos].boSt
        holder.mboDate.text = tics[pos].boDate
        holder.mboTime.text = tics[pos].boTime
        holder.mdeSt.text = tics[pos].deSt
        holder.mdeDate.text = tics[pos].deDate
        holder.mdeTime.text = tics[pos].deTime
        holder.mbookdate.text = tics[pos].bookDate
        holder.mticCost.text = tics[pos].cost.toString()
        mHPB.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return tics.size
    }

    fun updateHistory(ti_data: ArrayList<Ticket>){
        tics.clear()
        tics.addAll(ti_data)

        notifyDataSetChanged()
    }

}

class ItemViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    val mtrName : TextView = itemView.findViewById(R.id.trainName)
    val mtrId : TextView = itemView.findViewById(R.id.trainId)
    val mboSt : TextView = itemView.findViewById(R.id.boStation)
    val mboDate : TextView = itemView.findViewById(R.id.boDate)
    val mboTime : TextView = itemView.findViewById(R.id.boTime)
    val mdeSt : TextView = itemView.findViewById(R.id.desStation)
    val mdeDate : TextView = itemView.findViewById(R.id.desDate)
    val mdeTime : TextView = itemView.findViewById(R.id.desTime)
    val mbookdate : TextView = itemView.findViewById(R.id.bookDate)
    val mticCost : TextView = itemView.findViewById(R.id.tic_cost)
}

interface TicClicked{
    fun onTClick(tic : Int)
}