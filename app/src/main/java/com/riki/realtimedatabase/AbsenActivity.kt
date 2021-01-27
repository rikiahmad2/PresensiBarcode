package com.riki.realtimedatabase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper
import java.text.SimpleDateFormat
import java.util.*

class AbsenActivity : AppCompatActivity() {
    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)

        //WIDGET ID
        val logoutAdmin : Button = findViewById(R.id.logoutAdmin)
        val tvAdmin : TextView = findViewById(R.id.tvAdmin)
        val tvTimer : TextView = findViewById(R.id.tvTimer)


        //Sharedpreferences
        sharedpref = PreferencesHelper(this)
        tvAdmin.text = sharedpref.getDataString(Constants.PREF_USERNAME).toString()

        logoutAdmin.setOnClickListener {
            sharedpref.clearSession()
            startActivity((Intent(this, MainActivity::class.java)))
            finish()
        }

        //GET DATA FROM RECYCLER
        var titles = intent.getStringExtra("TITLE")
        var tvEvent : TextView = findViewById(R.id.tvEvent)
        tvEvent.text = titles

        //CHECK TIME EVERY SECONDS
        val timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    // Stuff that updates the UI
                    updateTimer()
                    generateQrCode(tvTimer.text.toString())
                }
            }
        }, 0, 1000)



    }

    //GENERATE QR CODE
    private fun generateQrCode(data : String){
        val barcodeImage : ImageView = findViewById(R.id.barcodeImage)
        val multiFormatWriter = MultiFormatWriter()
        try{
            val bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            barcodeImage.setImageBitmap(bitmap)
        }catch (e : WriterException){
            e.printStackTrace()
        }

    }

    //TIMER FUNCTION UPDATE
    private fun updateTimer() {
        val tvTimer : TextView = findViewById(R.id.tvTimer)
        runOnUiThread {
            tvTimer.text = "${SimpleDateFormat("HH:mm").format(Date())}"
        }
    }
}