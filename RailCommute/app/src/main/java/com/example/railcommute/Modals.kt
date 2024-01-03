package com.example.railcommute

class User(var user_token: String?, var mob: String?, var UserName: String?) {
    fun getToken(): String? {
        return this.user_token
    }

    fun getmob(): String? {
        return this.mob
    }

    fun getName(): String? {
        return this.UserName
    }
}

class Ticket(  // 12
    val ticId: Int,
    val trName: String,
    val trId: Int,
    val boSt: String,
    val deSt: String,
    val boDate: String,
    val deDate: String,
    val boTime: String,
    val deTime: String,
    val bookDate: String,
    val cost: Int,
    val isCancelled : Boolean
)

class Train( // 9
    val trName: String,
    val trId: Int,
    val boSt: String,
    val deSt: String,
    val boDate: String,
    val deDate: String,
    val boTime: String,
    val deTime: String,
    val cost: Int
)

class NewPassenger(val pName: String, val pAge: Int, val pGender: String)

class TicPassenger(val pName: String, val pAge: Int, val pGender: String, val pSeat: Int)