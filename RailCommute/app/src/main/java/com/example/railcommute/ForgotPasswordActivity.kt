package com.example.railcommute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var mn : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mn = findViewById(R.id.fopd_mn)
    }

    fun checkCred(view : View){
        val smn = mn.getEditText()?.getText().toString()
        var c = 0
        if(!(smn.all {it in '0'..'9'}) || smn.length != 10)
        {
            mn.setError("Please enter valid number")
            ++c
        }
        else mn.setError(null)

        if(c == 0) Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        // Sign Up logic
    }

    fun mkToast(view : View){
        Toast.makeText(this, "You will shortly receive an OTP", Toast.LENGTH_SHORT).show()
    }
}