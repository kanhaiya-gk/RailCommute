package com.example.railcommute

import android.content.Context
import android.content.SharedPreferences

public class SessionManagement(context: Context) {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    companion object{
        var PREF_NAME = "session"
        var SESSION_KEY = "session_user"
        var SESSION_MOB = "session_umob"
        var USER_NAME = "session_username"
    }

    fun saveSession(user: User) {
        //save session of user whenever user is logged in
        val id: String? = user.getToken()
        val umn = user.getmob()
        val unam = user.getName()
        editor.putString(SESSION_KEY, id).commit()
        editor.putString(SESSION_MOB, umn).commit()
        editor.putString(USER_NAME, unam).commit()
    }

    fun getToken() : String? {
        //return user token whose session is saved
        return sharedPreferences.getString(SESSION_KEY, "")
    }

    fun getmob() : String? {
        return sharedPreferences.getString(SESSION_MOB, "")
    }

    fun getName() : String? {
        return sharedPreferences.getString(USER_NAME, "")
    }

    fun removeSession() {
        editor.putString(SESSION_KEY, null).commit()
    }

    fun isLoggedIn() : Boolean{
        val ns = this.getToken()
        return (ns == null || ns.isEmpty())
    }

}