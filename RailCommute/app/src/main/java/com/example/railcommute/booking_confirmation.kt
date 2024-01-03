package com.example.railcommute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.util.HashMap

class booking_confirmation : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: TicPassengerAdapter
    private val ticpasers = ArrayList<TicPassenger>()
    private var isCancelled: Boolean = true
    private var ticId : Int = 0
    private lateinit var ccfTrainNameView : TextView
    private lateinit var ccfTrainNoView : TextView
    private lateinit var ccfBodateView : TextView
    private lateinit var ccfCostView : TextView
    private lateinit var ccfMsg : TextView
    private lateinit var ccfCancel : TextView
    private lateinit var pasView : TextView
    private lateinit var ticIdView: TextView

    var gotohist :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_confirm)

        ccfTrainNameView = findViewById(R.id.ccf_train_name)
        ccfTrainNoView = findViewById(R.id.ccf_train_id)
        ccfBodateView = findViewById(R.id.ccf_date_board)
        ccfCostView = findViewById(R.id.fare_actual)
        ccfCancel = findViewById(R.id.ccf_cancel)
        ccfMsg = findViewById(R.id.textView31)
        pasView = findViewById(R.id.textView23)
        ccfMsg.visibility = View.INVISIBLE
        ccfCancel.visibility = View.INVISIBLE
        ticIdView = findViewById(R.id.tic_id_show)

        recyclerView = findViewById(R.id.ccf_tic_paserRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val intent = getIntent()
        ticId = intent.getIntExtra("t_id", 0)
        gotohist = intent.getBooleanExtra("fromHistory", false)
        ticIdView.text = ticId.toString()

        getDetails()
        mAdapter = TicPassengerAdapter()
        recyclerView.setAdapter(mAdapter)
    }

    override fun onBackPressed() {
        if(gotohist) {
            super.onBackPressed()
        }
        else{
            val intent = Intent(this, User_Profile::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    fun getDetails(){
        val st = SessionManagement(this)
        val token = st.getToken()
        val req = JSONObject().apply {
            put("t_id", ticId)
        }
        val j1 : JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            "http://10.0.2.2:8000/tics/det/",
            req,
            { response ->
                ccfTrainNameView.text = response.getString("t_name")
                ccfTrainNoView.text = response.getInt("train_no").toString()
                ccfBodateView.text = response.getString("date_dep")
                ccfCostView.text = response.getInt("cost").toString()

                isCancelled = response.getBoolean("is_cancelled")
                if(!isCancelled){
                    ccfCancel.visibility = View.VISIBLE
                    val res = response.getJSONArray("p_list")
                    for (i in 0 until res.length()) {
                        val paser = res.getJSONObject(i)
                        ticpasers.add(
                            TicPassenger(
                                paser.getString("p_name"),
                                paser.getInt("p_age"),
                                paser.getString("p_gender"),
                                paser.getInt("p_seat")
                            )
                        )
                    }

                    mAdapter.updateOldPasers(ticpasers)
                }
                else{
                    ccfMsg.visibility = View.VISIBLE
                    pasView.visibility = View.INVISIBLE
                }
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

    fun cancelTic(view: View) {
        val intent = Intent(this, cancel_ticket::class.java)
        intent.putExtra("from_confirm", true)
        intent.putExtra("t_id", ticId)
        startActivity(intent)
    }
}