package com.riki.realtimedatabase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.riki.realtimedatabase.Admin.AbsenActivity
import com.riki.realtimedatabase.SharedPreferences.Constants
import com.riki.realtimedatabase.SharedPreferences.PreferencesHelper


class RecyclerAdapter(private val context: Context, var titles: List<String>, private var details:List<String>, private var images:List<Int>, private var bool : Boolean) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private lateinit var  sharedpref : PreferencesHelper

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemDetail: TextView = itemView.findViewById(R.id.tv_description)
        val itemPicture: ImageView = itemView.findViewById(R.id.iv_image)

        init{
            itemView.setOnClickListener { v : View ->
                if (sharedpref.getDataString(Constants.PREF_LEVEL).toString() == "admin" && bool == true) {
                    Toast.makeText(itemView.context, itemTitle.text.toString(), Toast.LENGTH_SHORT).show()
                    val intent = (Intent(context, AbsenActivity::class.java))
                    intent.putExtra("TITLE", itemTitle.text.toString())
                    intent.putExtra("JAM", itemDetail.text.toString())
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
                else if(sharedpref.getDataString(Constants.PREF_LEVEL).toString() == "user"){
                    Toast.makeText(itemView.context, itemTitle.text.toString(), Toast.LENGTH_SHORT).show()
                    val intent = (Intent(context, UserScanActivity::class.java))
                    intent.putExtra("TITLE", itemTitle.text.toString())
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
                else{

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.event_list,parent, false)
        sharedpref = PreferencesHelper(context)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDetail.text = details[position]
        holder.itemPicture.setImageResource(images[position])
    }
}