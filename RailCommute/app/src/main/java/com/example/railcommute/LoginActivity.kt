package com.example.railcommute

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), onComp, usern {
    lateinit var mn : TextInputLayout
    lateinit var pd : TextInputLayout
    lateinit var mni : EditText
    lateinit var pdi : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mn = findViewById(R.id.inputnm)
        pd = findViewById(R.id.inputPasswordLog)
        mni = findViewById(R.id.logmobinp)
        pdi = findViewById(R.id.logpassinp)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun signUp(view : View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }

    fun checkCred(view : View){
        val smn = mni.editableText.toString().trim()
        val spd = pdi.editableText.toString().trim()

        var c = 0
        if(!(smn.all {it in '0'..'9'}) || smn.length != 10)
        {
            showError("Please enter valid number", mn)
            ++c
        }
        else {
            removeError(mn)
        }
        if(spd.isEmpty() || spd.length < 8)
        {
            showError("Please enter valid password", pd)
            ++c
        }
        else removeError(pd)

        if(c == 0) {
            UserAuths.doLogin(smn, spd, this, this)
        }
    }

    private fun removeError(n : TextInputLayout) {
        n.setError(null)
    }

    fun showError(st : String, n : TextInputLayout){
        n.setError(st)
    }

    fun forgotps(view : View){
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    override fun perf(res: JSONObject, umob : String) {
        val st = SessionManagement(this)
        val usname = ""
        val user = User(res.getString("auth_token").trim(), umob, usname)
        st.saveSession(user)
        Datas.findUsername(umob, getApplicationContext(), this)
    }

    override fun canc() {
        Toast.makeText(this, "Wrong Credentials were provided", Toast.LENGTH_SHORT).show()
    }

    override fun useroncomp() {
        val intent = Intent(this, User_Profile::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}