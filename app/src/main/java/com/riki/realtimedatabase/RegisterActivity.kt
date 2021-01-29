package com.riki.realtimedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper

class RegisterActivity : AppCompatActivity() {
    private lateinit var sharedpref: PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername : EditText = findViewById(R.id.etUsername)
        val etPassword : EditText = findViewById(R.id.etPassword)
        val ButtonLogin : Button = findViewById(R.id.ButtonLogin)
        val etNama : EditText = findViewById(R.id.etNama)

        var username = etUsername.text.toString()
        var namaLengkap = etNama.text.toString()
        var pass = etPassword.text.toString()

        ButtonLogin.setOnClickListener {
            if (username != null && namaLengkap != null && pass != null) {
                database.child("Login").child(etUsername.text.toString()).child("level").setValue("user")
                database.child("Login").child(etUsername.text.toString()).child("nama").setValue(etNama.text.toString())
                database.child("Login").child(etUsername.text.toString()).child("password").setValue(etPassword.text.toString())
                database.child("Login").child(etUsername.text.toString()).child("username").setValue(etUsername.text.toString())
                Toast.makeText(this, "User Berhasil Dibuat !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}