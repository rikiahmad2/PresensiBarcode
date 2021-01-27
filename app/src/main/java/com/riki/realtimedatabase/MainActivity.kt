package com.riki.realtimedatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.riki.realtimedatabase.Admin.DashboardAdminActivity
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper


class MainActivity : AppCompatActivity() {
    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedpref = PreferencesHelper(this)

        //WIDGET
        val sButton : Button = findViewById(R.id.sButton)
        val tvPassword : EditText = findViewById(R.id.tvPassword)
        val tvUsername : EditText = findViewById(R.id.tvUsername)
        val vUsername : TextView = findViewById(R.id.vUsername)

        //LOGIN
        sButton.setOnClickListener {
            var username = tvUsername.text.toString()
            var password = tvPassword.text.toString()

            //GET DATA FROM FIREBASE
            database.child("Login").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    //Log.d("TAG", snapshot.child("admin").child("username").toString())
                    if (snapshot.child(username).exists() && snapshot.child(username).child("password").value == password){
                        if(snapshot.child(username).child("level").value == "user") {
                            saveSession(username, password, "user")
                            moveIntentUser()
                        }
                        else if(snapshot.child((username)).child("level").value == "admin"){
                            saveSession(username, password, "admin")
                            moveIntentAdmin()
                        }
                    }
                    else {
                        showToast("Data Tidak anda !!")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }



    }

    //WHEN APPS STARTED
    override fun onStart() {
        super.onStart()
        if (sharedpref.getDataBoolean(Constants.PREF_IS_LOGIN) && sharedpref.getDataString(Constants.PREF_LEVEL).toString() == "admin"){
            moveIntentAdmin()
        }
        else if (sharedpref.getDataBoolean(Constants.PREF_IS_LOGIN) && sharedpref.getDataString(Constants.PREF_LEVEL).toString() == "user"){
            moveIntentUser()
        }
    }

    //SESSION SAVE
    private fun saveSession(username : String, password : String, level : String){
        sharedpref.simpanDataString(Constants.PREF_USERNAME, username)
        sharedpref.simpanDataString(Constants.PREF_PASSWORD, password)
        sharedpref.simpanDataString(Constants.PREF_LEVEL,level)
        sharedpref.simpanDataBoolean(Constants.PREF_IS_LOGIN, true)
    }

    //TOAST POP-UP
    private fun showToast(message : String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    //Move Layout level User
    private fun moveIntentUser(){
        startActivity(Intent(this,UserActivity::class.java))
        finish()
    }

    //Move Layout level Admin
    private fun moveIntentAdmin(){
        startActivity(Intent(this,
            DashboardAdminActivity::class.java))
        finish()
    }
}