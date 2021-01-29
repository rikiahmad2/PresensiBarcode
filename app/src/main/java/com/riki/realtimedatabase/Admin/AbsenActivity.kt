package com.riki.realtimedatabase.Admin

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
import com.riki.realtimedatabase.R
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper
import java.text.SimpleDateFormat
import java.util.*

class AbsenActivity : AppCompatActivity() {
    private lateinit var  sharedpref : PreferencesHelper
    var database = FirebaseDatabase.getInstance().reference
    private lateinit var  barcodeImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)

        //WIDGET ID
        val lihatAbsenButton : Button = findViewById(R.id.lihatAbsenButton)
        val tvAdmin : TextView = findViewById(R.id.tvAdmin)
        val tvTimer : TextView = findViewById(R.id.tvTimer)
        var tvEvent : TextView = findViewById(R.id.tvEvent)
        var tvJam : TextView = findViewById(R.id.tvJam)
        barcodeImage= findViewById(R.id.barcodeImage)

        //GET DATA FROM RECYCLER
        var titles = intent.getStringExtra("TITLE")
        var jam = intent.getStringExtra("JAM")
        tvEvent.text = titles
        tvJam.text = jam

        //Sharedpreferences
        sharedpref = PreferencesHelper(this)
        tvAdmin.text = sharedpref.getDataString(Constants.PREF_USERNAME).toString()

        //TOMBOL LIHAT ABSEN
        lihatAbsenButton.setOnClickListener {
            val intent = (Intent(this, AbsenListActivity::class.java))
            intent.putExtra("TITLE", titles)
            this.startActivity(intent)
        }

        //TOMBOL LOGOUT


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
        }, 2000, 1000)



    }

    //GENERATE QR CODE
    private fun generateQrCode(data : String){
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

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}