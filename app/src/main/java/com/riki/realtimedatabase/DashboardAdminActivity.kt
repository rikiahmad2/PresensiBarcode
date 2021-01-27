package com.riki.realtimedatabase

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
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper
import java.lang.StringBuilder

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

        //Sharedpref
        sharedpref = PreferencesHelper(this)
        tvUsernameAdmin.text = sharedpref.getDataString(Constants.PREF_USERNAME).toString()

        //POST LIST VIEW TO RECYLER
        postToList()


        createEventBtn.setOnClickListener {
            createEventIntent()
        }
    }

    //Move Layout level Admin
    private fun createEventIntent(){
        startActivity(Intent(this,CreateEventActivity::class.java))
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
        rv.adapter = RecyclerAdapter(this,titlesList, descList, imageList)
    }

    private fun postToList(){
        database.child("Event").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var myString = i.key.toString()
                    Log.d("TAG", myString)
                    addToList(myString, "Deskripsi Event", R.mipmap.ic_launcher_round)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
}