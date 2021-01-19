package com.riki.realtimedatabase.SharedPreferences

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Constants {

    companion object{
        val PREF_USERNAME = "PREF_USERNAME"
        val PREF_PASSWORD = "PREF_PASSWORD"
        val PREF_IS_LOGIN = "PREF_IS_LOGIN"
        val PREF_LEVEL = "PREF_LEVEL"
        var count = "count"
    }
}