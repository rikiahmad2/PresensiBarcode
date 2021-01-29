package com.riki.realtimedatabase

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import com.riki.realtimedatabase.Admin.DashboardAdminActivity
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserScanActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference
    lateinit var tvTitleEvent : TextView
    lateinit var namaEvent : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_scan)

        //GET WIDGET
        val scanUser : Button = findViewById(R.id.scanUser)
        tvTitleEvent = findViewById(R.id.tvTitleEvent)
        tvTitleEvent.text = intent.getStringExtra("TITLE")
        namaEvent =  tvTitleEvent.text.toString()

        val backBtn : ImageView = findViewById(R.id.backBtn)
        backBtn.setOnClickListener(this)
        
        //Sharedpreferences
        sharedpref = PreferencesHelper(this)

        //SCAN BARCODE WHEN BUTTON CLICK
        scanUser.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.initiateScan()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.backBtn -> {
                val moveIntent = Intent(this@UserScanActivity, UserActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }

    // (SCAN BARCODE)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            var sessionUsername : String = sharedpref.getDataString(Constants.PREF_USERNAME).toString()
            if (result != null) {
                //CHECK DATA ON FIREBASE
                database.child("Event").addValueEventListener(object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (result.contents != null && sharedpref.getDataString(Constants.PREF_USERNAME).toString() != null) {
                            Toast.makeText(applicationContext, result.contents, Toast.LENGTH_SHORT).show()
                            database.child("Event").child(namaEvent).child("Absen")
                                    .child(sessionUsername).setValue(result.contents)
                        }
                        else{
                            Toast.makeText(applicationContext, "Data Tidak Ada !", Toast.LENGTH_SHORT).show()
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

    private fun userDashboardIntent(){
        startActivity(Intent(this,UserActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}