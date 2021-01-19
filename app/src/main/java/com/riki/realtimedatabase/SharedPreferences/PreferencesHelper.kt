package com.riki.realtimedatabase.SharedPreferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context : Context) {

    private val PREFS_NAME = "userpref123"
    private val sharedpref : SharedPreferences
    val editor :SharedPreferences.Editor

    init {
        sharedpref = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE)
        editor = sharedpref.edit()
    }

    fun simpanDataString(key : String, value : String){
        editor.putString(key,value)
                .apply()
    }

    fun simpanDataBoolean(key : String, value : Boolean){
        editor.putBoolean(key, value)
                .apply()
    }

    fun simpanDataInteger(key : String, value : Int){
        editor.putInt(key, value)
            .apply()
    }

    fun getDataString(key: String) : String? {
        return sharedpref.getString(key, null)
    }

    fun getDataBoolean(key: String) : Boolean{
        return sharedpref.getBoolean(key, false)
    }

    fun getDataInteger(key: String) : Int {
        return sharedpref.getInt(key, 0)
    }

    fun clearSession(){
        editor.remove("PREF_USERNAME")
        editor.remove("PREF_PASSWORD")
        editor.remove("PREF_IS_LOGIN")
                .apply()
    }

    fun remove(){
        editor.remove("count")
                .apply()
    }
}