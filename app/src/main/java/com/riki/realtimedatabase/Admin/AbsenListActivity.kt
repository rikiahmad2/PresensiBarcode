package com.riki.realtimedatabase.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.riki.realtimedatabase.R
import com.riki.realtimedatabase.RecyclerAdapter
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper

class AbsenListActivity : AppCompatActivity() {
    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference
    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen_list)

        //Sharedpreferences
        sharedpref = PreferencesHelper(this)
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
        rv.adapter = RecyclerAdapter(
            this,
            titlesList,
            descList,
            imageList,
                false
        )
    }

    private fun postToList(){
        //GET DATA FROM RECYCLER
       var namaEvent = intent.getStringExtra("TITLE").toString()
        database.child("Event").child(namaEvent).child("Absen").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    var myString = i.key.toString()
                    var jam = i.getValue().toString()
                    addToList(myString, jam,
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