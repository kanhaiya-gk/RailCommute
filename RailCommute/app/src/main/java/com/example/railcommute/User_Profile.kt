package com.example.railcommute

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class User_Profile : AppCompatActivity(), wall {
    lateinit var tv : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val st = SessionManagement(this)
        tv = findViewById(R.id.username)
        tv.text = st.getName()
    }

    fun upd(v : View){
        intent = Intent(this, ResetPassword::class.java)
        startActivity(intent)
    }

    fun ran(v : View){
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    fun showWallet(v : View){
        val st = SessionManagement(this)
        val mn = st.getmob().toString()
        val am = Datas.walletAmount(mn, this, this)
    }

    fun profbooktic(v : View){
        val intent = Intent(this, BookTicket::class.java)
        startActivity(intent)
    }

    fun canceltic(v : View){
        val intent = Intent(this, cancel_ticket::class.java)
        startActivity(intent)
    }

    fun plzLogout(v : View){
        UserAuths.doLogout(getApplicationContext())
        intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun walloncomp(amount: Int) {
        MaterialAlertDialogBuilder(this, R.style.MyDialogTheme)
            .setTitle("Amount")
            .setMessage("Your wallet has Rs. $amount").show()
    }
}
