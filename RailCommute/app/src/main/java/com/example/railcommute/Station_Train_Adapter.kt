package com.example.railcommute

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Station_Train_Adapter(private val listener: trainSelected): RecyclerView.Adapter<TrainViewHolder>() {

    private val trains: ArrayList<Train> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TrainViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.item_train, p0, false)
        val trainViewHolder = TrainViewHolder(view)
        view.setOnClickListener{
            listener.onSelect(trains[trainViewHolder.adapterPosition].trId, trains[trainViewHolder.adapterPosition].trName)
        }
        return trainViewHolder
    }

    override fun onBindViewHolder(holder: TrainViewHolder, pos: Int) {
        holder.mtrName.text = trains[pos].trName
        holder.mtrId.text = trains[pos].trId.toString()
        holder.mboSt.text = trains[pos].boSt
        holder.mboDate.text = trains[pos].boDate
        holder.mboTime.text = trains[pos].boTime
        holder.mdeSt.text = trains[pos].deSt
        holder.mdeDate.text = trains[pos].deDate
        holder.mdeTime.text = trains[pos].deTime
        holder.mticCost.text = trains[pos].cost.toString()
    }

    override fun getItemCount(): Int {
        return trains.size
    }

    fun updateTrains(tr_data: ArrayList<Train>){
        trains.clear()
        trains.addAll(tr_data)

        notifyDataSetChanged()
    }

}

class TrainViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    val mtrName : TextView = itemView.findViewById(R.id.st_trainName)
    val mtrId : TextView = itemView.findViewById(R.id.st_trainId)
    val mboSt : TextView = itemView.findViewById(R.id.st_boStation)
    val mboDate : TextView = itemView.findViewById(R.id.st_boDate)
    val mboTime : TextView = itemView.findViewById(R.id.st_boTime)
    val mdeSt : TextView = itemView.findViewById(R.id.st_desStation)
    val mdeDate : TextView = itemView.findViewById(R.id.st_desDate)
    val mdeTime : TextView = itemView.findViewById(R.id.st_desTime)
    val mticCost : TextView = itemView.findViewById(R.id.st_j_cost)
}

interface trainSelected{
    fun onSelect(train_no : Int, t_name : String){

    }
}