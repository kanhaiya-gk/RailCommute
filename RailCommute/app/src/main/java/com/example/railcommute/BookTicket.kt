package com.example.railcommute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class BookTicket : AppCompatActivity() {
    private lateinit var src_st : EditText
    private lateinit var dest_st : EditText
    private lateinit var datePicker: DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_ticket)

        src_st = findViewById(R.id.source_station_value)
        dest_st = findViewById(R.id.dest_station_value)
        datePicker = findViewById(R.id.date)
    }

    fun showListTrains(v : View){
        val day : Int = datePicker.getDayOfMonth()
        val month : Int = datePicker.getMonth() + 1
        val year : Int = datePicker.getYear()
        val date = "$year-$month-$day"
        val src_val = src_st.editableText.toString().trim()
        val dest_val = dest_st.editableText.toString().trim()

        if(src_val.isEmpty() || dest_val.isEmpty()){
            Toast.makeText(this, "Please enter the station value", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, TrainActivity::class.java)
        intent.putExtra("src_val", src_val)
        intent.putExtra("dest_val", dest_val)
        intent.putExtra("date", date)
//        Toast.makeText(this, "$date $src_val $dest_val", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }
}