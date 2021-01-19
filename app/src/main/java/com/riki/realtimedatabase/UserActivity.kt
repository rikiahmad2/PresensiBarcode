package com.riki.realtimedatabase

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //WIDGET GET ID
        val clearButton : Button = findViewById(R.id.clearButton)
        var loginUsername : TextView = findViewById(R.id.loginUsername)
        val inputNama : EditText = findViewById(R.id.inputNama)
        val inputNIK : EditText = findViewById(R.id.inputNIK)
        val inputNomor : EditText = findViewById(R.id.inputNomor)
        val inputEmail : EditText = findViewById(R.id.inputEmail)
        val inputPerusahaan : EditText = findViewById(R.id.inputPerusahaan)
        val buttonData : Button = findViewById(R.id.buttonData)
        val scanButton : Button = findViewById(R.id.scanButton)
        loginUsername = findViewById(R.id.loginUsername)


        //Sharedpreferences
        sharedpref = PreferencesHelper(this)
        loginUsername.text = sharedpref.getDataInteger(Constants.count).toString()
        var sessionUsername = sharedpref.getDataString(Constants.PREF_USERNAME).toString()


        clearButton.setOnClickListener {
            sharedpref.clearSession()
            startActivity((Intent(this, MainActivity::class.java)))
            finish()
        }

        //SUBMIT FORM
        buttonData.setOnClickListener {
            database.child("Form").child(sessionUsername).setValue(InputForm(inputNama.text.toString()
                    ,inputNIK.text.toString(),inputNomor.text.toString()
                    ,inputEmail.text.toString(),inputPerusahaan.text.toString(),sessionUsername))
        }

        //SCAN BUTTON
        scanButton.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.initiateScan()
        }
    }


    //SCAN BARCODE
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var hitung = 0;
        while (hitung <= sharedpref.getDataInteger(Constants.count)){
            hitung++
        }
        var sessionUsername = sharedpref.getDataString(Constants.PREF_USERNAME).toString()
        var jamke           = sharedpref.getDataInteger(Constants.count).toString()

        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {

                //CHECK DATA ON FIREBASE
                database.child("Form").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (result.contents == null) {
                            Toast.makeText(applicationContext, "CANCELED", Toast.LENGTH_SHORT).show()
                        }
                        else if(snapshot.child(sessionUsername).child("username").value == sessionUsername && result.contents != null){
                            if (snapshot.child(sessionUsername).child(jamke).exists() == false) {
                                Toast.makeText(applicationContext, "Jam Masuk Ditambahkan", Toast.LENGTH_SHORT).show()
                                database.child("Form").child(sessionUsername)
                                        .child(jamke).setValue(result.contents)
                                sharedpref.remove()
                                sharedpref.simpanDataInteger(Constants.count, hitung)
                            }
                        }
                        else{
                            Toast.makeText(applicationContext, "Data Tidak Ada", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
            else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}