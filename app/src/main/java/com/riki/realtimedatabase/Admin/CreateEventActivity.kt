package com.riki.realtimedatabase.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import com.riki.realtimedatabase.R
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper

class CreateEventActivity : AppCompatActivity() {

    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        //Sharedpref
        sharedpref = PreferencesHelper(this)

        val namaEvent : EditText = findViewById(R.id.namaEvent)
        val jamMasuk : EditText = findViewById(R.id.jamMasuk)
        val jamKeluar : EditText = findViewById(R.id.jamKeluar)
        val createEventButton : Button = findViewById(R.id.createEventButton)

        createEventButton.setOnClickListener {
            if(namaEvent.text.toString() != null && jamMasuk.text.toString() != null && jamKeluar.text.toString() != null ){
                database.child("Event").child(namaEvent.text.toString()).child("NamaEvent")
                    .setValue(namaEvent.text.toString())
                database.child("Event").child(namaEvent.text.toString()).child("JamMasuk")
                    .setValue(jamMasuk.text.toString())
                database.child("Event").child(namaEvent.text.toString()).child("JamKeluar")
                    .setValue(jamKeluar.text.toString())
            }
            else{

            }
        }
    }
}