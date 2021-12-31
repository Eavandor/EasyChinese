package com.example.easychinese

import TestWord.XToastUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class LabActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab2)
        //https://pics4.baidu.com/feed/267f9e2f07082838cad04faa2e6ba1064e08f1ad.jpeg




    }
    fun getBitmap(imgUrl: String?): Bitmap? {
        var inputStream: InputStream? = null
        var outputStream: ByteArrayOutputStream? = null
        var url: URL? = null
        try {
            url = URL(imgUrl)
            val httpURLConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestMethod("GET")
            httpURLConnection.setReadTimeout(2000)
            httpURLConnection.connect()
            if (httpURLConnection.getResponseCode() === 200) {
                //网络连接成功
                inputStream = httpURLConnection.getInputStream()
                outputStream = ByteArrayOutputStream()
                val buffer = ByteArray(1024 * 8)
                var len = -1
                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStream.write(buffer, 0, len)
                }
                val bu: ByteArray = outputStream.toByteArray()
                return BitmapFactory.decodeByteArray(bu, 0, bu.size)
            } else {
                Log.d("fuck", "网络连接失败----" + httpURLConnection.getResponseCode())
            }
        } catch (e: Exception) {
            // TODO: handle exception
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
        return null
    }
    fun save(bmp:Bitmap?){
        if (bmp==null){
            XToastUtils.error("bitmap is null")
            return
        }

    }
}