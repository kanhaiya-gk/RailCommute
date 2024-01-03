package com.example.railcommute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun mainlogin(view : View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun mainsignup(view : View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }
}