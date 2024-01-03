package com.example.railcommute

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import org.json.JSONObject

class Register : AppCompatActivity(), regComp, onComp {
    lateinit var fn : TextInputLayout
    lateinit var ln : TextInputLayout
    lateinit var mn : TextInputLayout
    lateinit var em : TextInputLayout
    lateinit var pd : TextInputLayout
    lateinit var cp : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fn = findViewById(R.id.inputfname)
        ln = findViewById(R.id.inputlname)
        mn = findViewById(R.id.inputnumber)
        em = findViewById(R.id.inputEmail)
        pd = findViewById(R.id.inputPassword)
        cp = findViewById(R.id.inputConfirmPassword)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun regLogin(view : View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun checkCred(view : View){
        val sfn = fn.getEditText()?.getText().toString().trim()
        val sln = ln.getEditText()?.getText().toString().trim()
        val smn = mn.getEditText()?.getText().toString().trim()
        val sem = em.getEditText()?.getText().toString().trim()
        val spd = pd.getEditText()?.getText().toString().trim()
        val scp = cp.getEditText()?.getText().toString().trim()
        var c = 0

        if(sfn.isEmpty()) {
            showError("Field cannot be empty !", fn)
            ++c
        }
        else{
            removeError(fn)
        }
        if(sln.isEmpty()) {
            showError("Field cannot be empty !", ln)
            ++c
        }else{
            removeError(ln)
        }
        if(!(smn.all {it in '0'..'9'}) || smn.length != 10) {
            showError("Please enter valid number", mn)
            ++c
        }
        else{
            removeError(mn)
        }
        if(sem.isEmpty() || ("@" !in sem)) {
            showError("Please enter valid Email ID", em)
            ++c
        }
        else{
            removeError(em)
        }
        if(spd.isEmpty() || spd.length < 8) {
            showError("Password must contain 8 characters", pd)
            ++c
        }else{
            removeError(pd)
        }
        if(!(spd == scp) || scp.isEmpty()) {
            showError("Password not matching", cp)
            ++c
        }else{
            removeError(cp)
        }
        if(c == 0) {
            UserAuths.doSignUp(smn, sfn, sln, sem, spd, this, this)
        }
    }

    fun showError(st : String, n : TextInputLayout){
        n.setError(st)
    }

    private fun removeError(n : TextInputLayout) {
        n.setError(null)
    }

    override fun dothis(umob : String, pass : String, fname: String, lname: String) {
        val st = SessionManagement(this)
        val user = User(st.getToken(), umob, "${fname.capitalize()} ${lname.capitalize()}")
        st.saveSession(user)
        UserAuths.doLogin(umob, pass, this, this)
    }

    override fun wrng() {
        Toast.makeText(this, "Something went wrong!!\nPlease try again",Toast.LENGTH_SHORT).show()
    }

    override fun perf(res: JSONObject, umob: String) {
        val intent = Intent(this, User_Profile::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun canc() {

    }

}