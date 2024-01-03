package com.example.railcommute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest

class TrainActivity : AppCompatActivity(), trainSelected{
    private lateinit var recyclerView : RecyclerView
    private lateinit var mAdapter : Station_Train_Adapter
    private val trains = ArrayList<Train>()
    private lateinit var src_val : String
    private lateinit var dest_val : String
    private lateinit var date : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)

        val tlbar : Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tlbar)
        val txt : TextView = findViewById(R.id.toolbarTitle)
        txt.text = "List of Trains"

        recyclerView = findViewById(R.id.trainRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val intent = getIntent()
        src_val = intent.getStringExtra("src_val").toString()
        dest_val = intent.getStringExtra("dest_val").toString()
        date = intent.getStringExtra("date").toString()

        fetchStats()
        mAdapter = Station_Train_Adapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchStats(){

        val j1 = JsonObjectRequest(
            Request.Method.GET,
            "http://10.0.2.2:8000/statran/stat/?st1=$src_val&st2=$dest_val&date_search=$date",
            null,
            { response ->
                val res = response.getJSONArray("trains")
                for(i in 0 until res.length()){
                    val paser = res.getJSONObject(i)
                    trains.add(
                        Train(
                            paser.getString("t_name"),
                            paser.getInt("train_no"),
                            paser.getString("src_st"),
                            paser.getString("dest_st"),
                            paser.getString("date_dep"),
                            paser.getString("date_arr"),
                            paser.getString("departure_time"),
                            paser.getString("arrival_time"),
                            paser.getInt("cost")
                        )
                    )
                }

                mAdapter.updateTrains(trains)
            },
            {
                Toast.makeText(this, "Server side error", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(j1)
    }

    override fun onSelect(train_no: Int, t_name : String) {
        val intent = Intent(this, SeatAvailability::class.java)
        intent.putExtra("train_no", train_no)
        intent.putExtra("t_name", t_name)
        intent.putExtra("src_val", src_val)
        intent.putExtra("dest_val", dest_val)
        intent.putExtra("date", date)
        startActivity(intent)
    }
}