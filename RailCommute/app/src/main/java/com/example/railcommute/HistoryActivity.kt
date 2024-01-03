package com.example.railcommute

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class HistoryActivity : AppCompatActivity(), TicClicked {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: Hist_item_adpater
    lateinit var mHistProgressBar : ProgressBar
    private val tics = ArrayList<Ticket>()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val tlbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(tlbar)
        tlbar.overflowIcon = getResources().getDrawable(R.drawable.ic_options)
        mHistProgressBar = findViewById(R.id.hist_prog_bar)

        recyclerView = findViewById(R.id.ticketRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getTics()
        mAdapter = Hist_item_adpater(this, mHistProgressBar)
        recyclerView.adapter = mAdapter
//        mHistProgressBar.visibility = View.GONE
    }

    private fun getTics() {   // To add tickets in tics
        val st = SessionManagement(this)
        val mn = st.getmob()
        val req = JSONObject().apply {
            put("mobile_no", mn)
        }
        token = st.getToken().toString()
        val j1 : JsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,
            "http://10.0.2.2:8000/tics/usrtics/",
            req,
            { response ->
                val res = response.getJSONArray("tics")
                for(i in 0 until res.length()){
                    val paser = res.getJSONObject(i)
                    tics.add(
                        Ticket(
                            paser.getInt("tic_id"),
                            paser.getString("t_name"),
                            paser.getInt("train_no"),
                            paser.getString("src_st"),
                            paser.getString("dest_st"),
                            paser.getString("date_dep"),
                            paser.getString("date_arr"),
                            paser.getString("departure_time"),
                            paser.getString("arrival_time"),
                            paser.getString("date_book"),   // booking date
                            paser.getInt("cost"),
                            paser.getBoolean("is_cancelled")
                        )
                    )
                }

                mAdapter.updateHistory(tics)
            },
            {
                Toast.makeText(this, "Server side error", Toast.LENGTH_SHORT).show()
            }
        ){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Token $token"
                return headers
            }
        }
        MySingleton.getInstance(this).addToRequestQueue(j1)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.hist_toolbar_menu, menu)
        return true
    }

    override fun onTClick(ticId: Int) {
        val intent = Intent(this, booking_confirmation::class.java)
        intent.putExtra("t_id", ticId)
        intent.putExtra("fromHistory", true)
        startActivity(intent)
    }
}
