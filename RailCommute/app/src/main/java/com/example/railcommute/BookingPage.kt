package com.example.railcommute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class BookingPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: AddPassengerAdapter
    private var ticId : Int = 0
    private val newpasers = ArrayList<NewPassenger>()
    private lateinit var src_val : String
    private lateinit var dest_val : String
    private var train_no : Int = 0
    private lateinit var date : String
    private lateinit var pasnm : EditText
    private lateinit var pasage : EditText
    private lateinit var pasgen : RadioGroup
    private var seatsAv : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_page)

        val intent = getIntent()
        train_no = intent.getIntExtra("train_no", 0)
        src_val = intent.getStringExtra("src_val").toString()
        dest_val = intent.getStringExtra("dest_val").toString()
        date = intent.getStringExtra("date").toString()
        seatsAv = intent.getIntExtra("seatsA", 0)

        pasnm = findViewById(R.id.inp_paser_name_value)
        pasage = findViewById(R.id.inp_paser_age_value)
        pasgen = findViewById(R.id.paser_gender)

        recyclerView = findViewById(R.id.newpaserRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = AddPassengerAdapter(newpasers)
        recyclerView.setAdapter(mAdapter)
    }

    fun updatePasers(view : View){
        val gender = when (pasgen.getCheckedRadioButtonId()) {
            R.id.radioMale -> "Male"
            R.id.radioFemale -> "Female"
            else -> "Others"
        }
        val pasname = pasnm.editableText.toString().trim()
        val age = pasage.editableText.toString().trim()
        newpasers.add(
            NewPassenger(pasname, age.toInt(), gender)
        )

        mAdapter.notifyDataSetChanged()

        pasgen.clearCheck()
        pasnm.getText().clear()
        pasage.getText().clear()
        // notify the recycler view dataset changed
    }

    fun finallyBookTicket(view : View){
        if(newpasers.size == 0){
            Toast.makeText(this, "You have not added any passenger!", Toast.LENGTH_SHORT).show()
            return
        }

        if(newpasers.size > seatsAv){
            Toast.makeText(this, "You can only add $seatsAv passengers", Toast.LENGTH_SHORT).show()
            return
        }

        val st = SessionManagement(this)
        val token = st.getToken()
        val mn = st.getmob()

        // returned data : "t_id"

        val p_list = JSONArray()
        for(i in 0 until newpasers.size){
            p_list.put(JSONObject().apply{
                put("p_name", newpasers[i].pName)
                put("p_age", newpasers[i].pAge)
                put("p_gender", newpasers[i].pGender)
            })
        }
        val req = JSONObject().apply {
            put("mn", mn)
            put("date_book", date)
            put("st1", src_val)
            put("st2",dest_val)
            put("train_no", train_no)
            put("p_list", p_list)
        }
        val j1 : JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            "http://10.0.2.2:8000/tics/book/",
            req,
            { response ->
                ticId = response.getInt("t_id")
                showConfirmation()
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

    fun showConfirmation(){
        val intent = Intent(this, booking_confirmation::class.java)
        intent.putExtra("t_id", ticId)
        startActivity(intent)
    }

}