package com.example.easychinese

import LoginAndCreateAccount.LoginActivity
import Recite.ReciteActivity
import Services.AnnouncementService
import TestWord.XToastUtils
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowerActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shower2)
        val t=intent.getStringExtra("fdbk")
        findViewById<TextView>(R.id.asuyukys).text=t
        if (ReciteActivity.isNew){
            val check=getSharedPreferences("userinfo", Context.MODE_PRIVATE)
            val uid=check.getString("current_dict","")!!
            val token=check.getString("token","")!!
            getRenew(this,uid,token,ReciteActivity.jso)
        }
    }


    fun getRenew(conte: Context, uid:String,token:String,b:String){
        val obj= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
        LoginActivity.publicretrofit.create(AnnouncementService::class.java)
            .renew(uid,token,obj)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    XToastUtils.error("Unable to update"+t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val fdbk = response.body()?.string()
                    if (fdbk != null) {
                        XToastUtils.info(fdbk)

                    } else {
                        XToastUtils.error("Empty Feedback")
                    }
                }

            })
    }
}