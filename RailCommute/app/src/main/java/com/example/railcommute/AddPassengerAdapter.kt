package com.example.railcommute

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AddPassengerAdapter(private val newpasers: ArrayList<NewPassenger>): RecyclerView.Adapter<AddPaserViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AddPaserViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_new_paser, p0, false)
        return AddPaserViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddPaserViewHolder, pos: Int) {
        holder.mName.text =  newpasers[pos].pName
        holder.mGender.text =  newpasers[pos].pGender
        holder.mAge.text =  newpasers[pos].pAge.toString()
    }

    override fun getItemCount(): Int {
        return newpasers.size
    }

}

class AddPaserViewHolder(itemview : View): RecyclerView.ViewHolder(itemview){
    val mName : TextView = itemView.findViewById(R.id.newpaser_actual_name)
    val mGender : TextView = itemView.findViewById(R.id.newpaser_actual_gender)
    val mAge : TextView = itemView.findViewById(R.id.newpaser_actual_age)
}