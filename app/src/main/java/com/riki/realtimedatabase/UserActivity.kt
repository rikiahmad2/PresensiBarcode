package com.riki.realtimedatabase

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper


class UserActivity : AppCompatActivity() {
    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference
    lateinit var loginUsername : TextView
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //WIDGET GET ID
        val clearButton : Button = findViewById(R.id.clearButton)
        var loginUsername : TextView = findViewById(R.id.loginUsername)
        loginUsername = findViewById(R.id.loginUsername)


        //Sharedpreferences
        sharedpref = PreferencesHelper(this)
        loginUsername.text = sharedpref.getDataString(Constants.PREF_USERNAME).toString()
        var sessionUsername = sharedpref.getDataString(Constants.PREF_USERNAME).toString()

        clearButton.setOnClickListener {
            sharedpref.clearSession()
            sharedpref.remove()
            startActivity((Intent(this, MainActivity::class.java)))
            finish()
        }

        //POST LIST VIEW TO RECYLER
        postToList()
    }

    //LIST OF ARRAY
    private fun addToList(title:String, description:String, image: Int){
        titlesList.add(title)
        descList.add(description)
        imageList.add(image)
        //RecyclerView
        val rv: RecyclerView = findViewById(R.id.rv_recyclerView)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = RecyclerAdapter(this,titlesList, descList, imageList, true)
    }

    //POST LIST VIEW TO RECYLER
    private fun postToList(){
        database.child("Event").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var myString = i.key.toString()
                    var jamMasuk = i.child("JamMasuk").getValue().toString()
                    var jamKeluar = i.child("JamKeluar").getValue().toString()
                    addToList(myString, jamMasuk +" - "+ jamKeluar, R.mipmap.ic_launcher_round)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}