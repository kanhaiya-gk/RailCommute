package com.example.railcommute

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.HashMap

class ResetPassword : AppCompatActivity() {

    private lateinit var olpv: EditText
    private lateinit var newpv: EditText
    private lateinit var conewpv: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        olpv = findViewById(R.id.oldPassword_val)
        newpv = findViewById(R.id.newPassword_val)
        conewpv = findViewById(R.id.confirmNewPassword_val)
    }

    fun resetPassword(view: View) {
        val f = olpv.editableText.toString().trim()
        val s = newpv.editableText.toString().trim()
        val t = conewpv.editableText.toString().trim()

        if (f.isBlank() || f.isEmpty()) {
            Toast.makeText(this, "Blank Field", Toast.LENGTH_SHORT).show()
        }
        if (s.isEmpty() || s.isBlank()) {
            Toast.makeText(this, "Blank Field", Toast.LENGTH_SHORT).show()
        } else if (s.length < 8) Toast.makeText(
            this,
            "Password length too short",
            Toast.LENGTH_SHORT
        ).show()
        else {
            if (!(s == t)) Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            else {

                olpv.getText().clear()
                newpv.getText().clear()
                conewpv.getText().clear()

                val j = JSONObject().apply {
                    put("new_password", s)
                    put("current_password", f)
                }
                val st = SessionManagement(this)
                val token = st.getToken()

                val req: JsonObjectRequest = object : JsonObjectRequest(
                    Request.Method.POST, "http://10.0.2.2:8000/auth/users/set_password/",
                    j, Response.Listener {
                        MaterialAlertDialogBuilder(this, R.style.MyDialogTheme)
                            .setTitle("Status")
                            .setMessage("Your has been changed successfully").show()
                    }, Response.ErrorListener {
                        MaterialAlertDialogBuilder(this, R.style.MyDialogTheme)
                            .setTitle("Amount")
                            .setMessage("Current password entered is not correct").show()
                    }
                ) {
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
                MySingleton.getInstance(this).addToRequestQueue(req)
            }
        }
    }
}