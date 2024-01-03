package com.example.railcommute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class SeatAvailability : AppCompatActivity() {
    private lateinit var src_val : String
    private lateinit var dest_val : String
    private var train_no : Int = 0
    private lateinit var t_name : String
    private lateinit var date : String
    private lateinit var trNameView : TextView
    private lateinit var trIdView : TextView
    private lateinit var srcView : TextView
    private lateinit var destView : TextView
    private lateinit var seatView : TextView
    private var ans : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat_availability)

        val intent = getIntent()
        train_no = intent.getIntExtra("train_no", 0)
        t_name = intent.getStringExtra("t_name").toString()
        src_val = intent.getStringExtra("src_val").toString()
        dest_val = intent.getStringExtra("dest_val").toString()
        date = intent.getStringExtra("date").toString()

        trNameView = findViewById(R.id.textView6)
        trIdView = findViewById(R.id.textView7)
        srcView = findViewById(R.id.textView10)
        destView = findViewById(R.id.textView11)
        seatView = findViewById(R.id.show_seats_avail)

        trNameView.text = t_name
        trIdView.text = train_no.toString()
        srcView.text = src_val
        destView.text = dest_val

        showSeats()
    }

    fun showSeats(){
        val j1 = JsonObjectRequest(
            Request.Method.GET,
            "http://10.0.2.2:8000/statran/seats/?st1=$src_val&st2=$dest_val&tr=$train_no",
            null,
            { response ->
                ans = response.getInt("num")
                seatView.text = ans.toString()
            },
            {
                Toast.makeText(this, "Server side error", Toast.LENGTH_SHORT).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(j1)
    }

    fun open_booking_page(v : View){
        val intent = Intent(this, BookingPage::class.java)
        intent.putExtra("train_no", train_no)
        intent.putExtra("src_val", src_val)
        intent.putExtra("dest_val", dest_val)
        intent.putExtra("date", date)
        intent.putExtra("seatsA", ans)
        startActivity(intent)
    }
}