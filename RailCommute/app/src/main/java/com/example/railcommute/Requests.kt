package com.example.railcommute

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import kotlin.math.ln

class UserAuths() {
    companion object {
        private val Burl = "http://10.0.2.2:8000/auth/"

        fun doLogin(umob: String, pass: String, con: Context, listener: onComp) {

            val req = JSONObject().apply {
                put("mobile_no", umob)
                put("password", pass)
            }
            var rt: Boolean = false
            var token: String = ""

            val j1 = JsonObjectRequest(
                Request.Method.POST, Burl + "token/login/", req,
                { response ->
                    listener.perf(response, umob)
                },
                {
                    listener.canc()
//                val response: NetworkResponse = it.networkResponse
//                if (it is ServerError && response != null) {
//                    try {
//                        val res = String(
//                            response?.data ?: ByteArray(0),
//                            Charset.forName(
//                                HttpHeaderParser.parseCharset(
//                                    response?.headers,
//                                    "utf-8"
//                                )
//                            )
//                        )
//                        // Now you can use any deserializer to make sense of data
//                        val obj = JSONObject(res).getString("non_field_errors")
////                        Toast.makeText(con, obj, Toast.LENGTH_SHORT).show()
//                    } catch (e1: UnsupportedEncodingException) {
//                        // Couldn't properly decode data to string
//                        Toast.makeText(con, "Error cannot be parsed", Toast.LENGTH_SHORT).show()
//                        e1.printStackTrace()
//                    } catch (e2: JSONException) {
//                        // returned data is not JSONObject?
//                        Toast.makeText(con, "Server side error", Toast.LENGTH_SHORT).show()
//                        e2.printStackTrace()
//                    }
//                }
                }
            )
            MySingleton.getInstance(con).addToRequestQueue(j1)
        }

        fun doSignUp(umob: String, fname: String, lname: String, emal: String, pass: String, con: Context, list: regComp) {

            val req = JSONObject().apply {
                put("mobile_no", umob)
                put("first_name", fname)
                put("last_name", lname)
                put("email", emal)
                put("password", pass)
            }

            val j1 = JsonObjectRequest(
                Request.Method.POST, Burl + "users/", req,
                { response ->
                    list.dothis(umob, pass, fname, lname)
                },
                {
                    list.wrng()
//                val response: NetworkResponse = it.networkResponse
//                if (it is ServerError && response != null) {
//                    try {
//                        val res = String(
//                            response?.data ?: ByteArray(0),
//                            Charset.forName(
//                                HttpHeaderParser.parseCharset(
//                                    response?.headers,
//                                    "utf-8"
//                                )
//                            )
//                        )
//                        val obj = JSONObject(res).toString()
//                        Toast.makeText(con, obj, Toast.LENGTH_SHORT).show()
//                    } catch (e1: UnsupportedEncodingException) {
//                        // Couldn't properly decode data to string
//                        Toast.makeText(con, "Error cannot be parsed", Toast.LENGTH_SHORT).show()
//                        e1.printStackTrace()
//                    } catch (e2: JSONException) {
//                        // returned data is not JSONObject?
//                        Toast.makeText(con, "Server side error", Toast.LENGTH_SHORT).show()
//                        e2.printStackTrace()
//                    }
//                }
                }
            )

            MySingleton.getInstance(con).addToRequestQueue(j1)
        }

        fun doLogout(con: Context): Boolean {

            val st = SessionManagement(con)
            val token = st.getToken()

            val req: JsonObjectRequest = object : JsonObjectRequest(
                Request.Method.POST, Burl + "token/logout/",
                null, Response.Listener { response ->
                    val data = response.getString("detail")
                }, Response.ErrorListener {
                    Log.d("Restricted API Error", it.localizedMessage)
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
            MySingleton.getInstance(con).addToRequestQueue(req)
            st.removeSession()
            return true
        }
    }
}

class Datas(){
    companion object{
        fun findUsername (mn : String, con : Context, lis : usern){
            val st = SessionManagement(con)
            val token = st.getToken().toString()

            val j1 : JsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8000/tics/usr/?mn=$mn",
                null,
                { response ->
                    val ans = response.getString("u_name")     // ??
                    val user = User(token, mn, ans)
                    st.saveSession(user)
                    lis.useroncomp()
                },
                {
                    Toast.makeText(con, "Server side error", Toast.LENGTH_SHORT).show()
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
            MySingleton.getInstance(con).addToRequestQueue(j1)
        }

        fun walletAmount (mn : String, con : Context, list : wall){
            val st = SessionManagement(con)
            val token = st.getToken().toString()

            val j1 : JsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET,
                "http://10.0.2.2:8000/tics/wallet/?mn=$mn",
                null,
                { response ->
                    val ans = response.getInt("amount")
                    list.walloncomp(ans)

                },
                {
                    Toast.makeText(con, "Server side error", Toast.LENGTH_SHORT).show()
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
            MySingleton.getInstance(con).addToRequestQueue(j1)
        }
    }
}

interface onComp{
    fun perf(res : JSONObject, umob: String)
    fun canc()
}

interface regComp{
    fun dothis(umob: String, pass: String, fname: String, lname: String)
    fun wrng()
}

interface usern{
    fun useroncomp()
}

interface wall{
    fun walloncomp(amount : Int)
}