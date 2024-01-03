package com.example.railcommute

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.util.HashMap

class cancel_ticket : AppCompatActivity() {
    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_ticket)

        editText = findViewById(R.id.cancel_tic_id_val)

        val intent = getIntent()
        val direct = intent.getBooleanExtra("from_confirm", false)
        if(direct){
            val td = intent.getIntExtra("t_id", 0)
            actualCancel(td)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, User_Profile::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    fun cancelticket(view : View){
        val canticId = editText.editableText.toString().trim()
        actualCancel(canticId.toInt())
    }

    fun actualCancel(canticId : Int){

        val st = SessionManagement(this)
        val token = st.getToken().toString()
        val req = JSONObject().apply {
            put("t_id", canticId)
        }
        val j1 : JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            "http://10.0.2.2:8000/tics/cancel/",
            req,
            {
                val str = it.getString("Status")
                if(str == "false")
                    errormsg()
                else
                    showmsg()
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

    fun showmsg(){
        MaterialAlertDialogBuilder(this, R.style.MyDialogTheme)
            .setTitle("Status")
            .setMessage("Your Ticket has been successfully cancelled\nThe ticket fare has been\n  credited to your wallet").show()
    }
    fun errormsg(){
        MaterialAlertDialogBuilder(this, R.style.MyDialogTheme)
            .setTitle("Status")
            .setMessage("Either ticket has been already cancelled\nOR\nTicket does not exist").show()
    }
}