package com.example.railcommute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicPassengerAdapter (): RecyclerView.Adapter<TicPaserViewHolder>() {

    private val addpasers: ArrayList<TicPassenger> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TicPaserViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_ticket_paser, p0, false)
        return TicPaserViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicPaserViewHolder, pos: Int) {
        holder.mName.text =  addpasers[pos].pName
        holder.mGender.text =  addpasers[pos].pGender
        holder.mAge.text =  addpasers[pos].pAge.toString()
        holder.mSeat.text =  addpasers[pos].pSeat.toString()
    }

    override fun getItemCount(): Int {
        return addpasers.size
    }

    fun updateOldPasers(olp_data: ArrayList<TicPassenger>){
        addpasers.clear()
        addpasers.addAll(olp_data)

        notifyDataSetChanged()
    }

}

class TicPaserViewHolder(itemview : View): RecyclerView.ViewHolder(itemview){
    val mName : TextView = itemView.findViewById(R.id.ticpaser_actual_name)
    val mGender : TextView = itemView.findViewById(R.id.ticpaser_actual_gender)
    val mAge : TextView = itemView.findViewById(R.id.ticpaser_actual_age)
    val mSeat : TextView = itemView.findViewById(R.id.ticpaser_actual_seat)
}