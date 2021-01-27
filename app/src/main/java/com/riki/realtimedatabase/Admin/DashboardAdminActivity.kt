package com.riki.realtimedatabase.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.riki.realtimedatabase.MainActivity
import com.riki.realtimedatabase.R
import com.riki.realtimedatabase.RecyclerAdapter
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper

class DashboardAdminActivity : AppCompatActivity() {
    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        //GET REQUIRED WIDGET
        val tvUsernameAdmin : TextView = findViewById(R.id.tvUsernameAdmin)
        val createEventBtn : Button = findViewById(R.id.createEventBtn)
        val logoutBtn : Button = findViewById(R.id.logoutBtn)

        //Sharedpref
        sharedpref = PreferencesHelper(this)
        tvUsernameAdmin.text = sharedpref.getDataString(Constants.PREF_USERNAME).toString()

        //POST LIST VIEW TO RECYLER
        postToList()


        //CREATE EVENT BUTTON
        createEventBtn.setOnClickListener {
            createEventIntent()
        }

        //LOGOUT BUTTON USER
        logoutBtn.setOnClickListener {
            sharedpref.clearSession()
            logoutIntent()
        }
    }

    //Move Layout level Admin
    private fun createEventIntent(){
        startActivity(Intent(this, CreateEventActivity::class.java))
        finish()
    }

    //MOVE TO LOGIN PAGE
    private fun logoutIntent(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    //LIST OF ARRAY
    private fun addToList(title:String, description:String, image: Int){
        titlesList.add(title)
        descList.add(description)
        imageList.add(image)
        //RecyclerView
        val rv: RecyclerView = findViewById(R.id.rv_recyclerView)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = RecyclerAdapter(
            this,
            titlesList,
            descList,
            imageList,
                true
        )
    }

    private fun postToList(){
        database.child("Event").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var myString = i.key.toString()
                    var jamMasuk = i.child("JamMasuk").getValue().toString()
                    var jamKeluar = i.child("JamKeluar").getValue().toString()
                    addToList(myString, jamMasuk +" - "+ jamKeluar,
                        R.mipmap.ic_launcher_round
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}