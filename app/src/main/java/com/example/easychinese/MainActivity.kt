package com.example.easychinese

import LoginAndCreateAccount.LoginActivity
import Recite.ReciteActivity
import Services.AnnouncementService
import TestWord.XToastUtils
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.xuexiang.xui.widget.textview.marqueen.MarqueeFactory
import com.xuexiang.xui.widget.textview.marqueen.SimpleNoticeMF
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IndexOutOfBoundsException
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var pd: ProgressDialog
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        pd= ProgressDialog(this@MainActivity)
        pd.setMessage("Loading......")
        val chineseList=ReciteActivity.chineseList
        var currentPage = 0
        showWord(chineseList[currentPage])
//        XToastUtils.info(chineseList[currentPage])
        currentPage++
        findViewById<Button>(R.id.gotonext).setOnClickListener {
            if (currentPage < chineseList.size) {
                showWord(chineseList[currentPage])
                findViewById<ScrollView>(R.id.jkjfgyuf).fullScroll(ScrollView.FOCUS_UP)
            } else {
                if (ReciteActivity.isNew){
                    val check=getSharedPreferences("userinfo",Context.MODE_PRIVATE)
                    val uid=check.getString("uid","")!!
                    val token=check.getString("token","")!!
                    getRenew(this,uid,token,ReciteActivity.jso)
                }
                startActivity(Intent(this, ReciteActivity::class.java))
                XToastUtils.success("Congratulations!! You have finished all the words in this list!")
            }
            currentPage++
        }


    }

    @SuppressLint("SetTextI18n")
    fun showWord(word: String) {
thread {


        runOnUiThread {
            pd.show()
        }
    val wwwww=JSONObject(word).getString("src")
        val doc0 = Jsoup.connect("https://cn.bing.com/dict/search?q=${wwwww}")
            .userAgent("Mozilla")
            .timeout(5000)
            .post()


        val doc = doc0.body()

    val format = HanyuPinyinOutputFormat()
    format.toneType = HanyuPinyinToneType.WITH_TONE_MARK
    format.vCharType = HanyuPinyinVCharType.WITH_U_UNICODE
    format.caseType = HanyuPinyinCaseType.LOWERCASE
    val pinyin ="/"+PinyinHelper.toHanYuPinyinString(wwwww,format," ",false)+"/"
//        val pinyin = doc.getElementsByClass("hd_p1_1")[0].text()
    var mean = ""
    try {
        val meaning = doc.getElementsByClass("def b_regtxt")

        for (a in meaning) {
            mean += a.text()
            mean += "\n"
        }
    }catch (e : IndexOutOfBoundsException){
        e.printStackTrace()
    }




    var sentence1="Unable to translate."
    var sentence2="Unable to translate."
    var sentence3="Unable to translate."
    var sentence4="Unable to translate."
    var sentence5="Unable to translate."
    try {
        val allEnglishSentense = doc.getElementsByClass("sen_en b_regtxt")
        val allChineseSentenses = doc.getElementsByClass("sen_cn b_regtxt")
//        sentence1="${allEnglishSentense[0].text()}\n\n${allChineseSentenses[0].text()}"
//        sentence2="${allEnglishSentense[1].text()}\n\n${allChineseSentenses[1].text()}"
//        sentence3="${allEnglishSentense[2].text()}\n\n${allChineseSentenses[2].text()}"
//        sentence4="${allEnglishSentense[3].text()}\n\n${allChineseSentenses[3].text()}"
//        sentence5="${allEnglishSentense[4].text()}\n\n${allChineseSentenses[4].text()}"
        sentence1="${allChineseSentenses[0].text()}\n\n${allEnglishSentense[0].text()}"
        sentence2="${allChineseSentenses[1].text()}\n\n${allEnglishSentense[1].text()}"
        sentence3="${allChineseSentenses[2].text()}\n\n${allEnglishSentense[2].text()}"
        sentence4="${allChineseSentenses[3].text()}\n\n${allEnglishSentense[3].text()}"
        sentence5="${allChineseSentenses[4].text()}\n\n${allEnglishSentense[4].text()}"
    }catch (e: IndexOutOfBoundsException){
        e.printStackTrace()
    }
        runOnUiThread {
            findViewById<TextView>(R.id.wordtitle).text = wwwww
            findViewById<TextView>(R.id.pppinyin).text = "soundmark:   "+pinyin
            findViewById<TextView>(R.id.mean1).text = mean
            findViewById<TextView>(R.id.sentencces).text = sentence1
            findViewById<TextView>(R.id.sentencces2).text = sentence2
            findViewById<TextView>(R.id.sentencces3).text = sentence3
            findViewById<TextView>(R.id.sentencces4).text = sentence4
            findViewById<TextView>(R.id.sentencces5).text = sentence5
            pd.dismiss()
        }
}
    }




    fun getRenew(conte: Context, uid:String,token:String,b:String){
        val obj= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), b);
        LoginActivity.publicretrofit.create(AnnouncementService::class.java)
            .renew(uid,token,obj)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    XToastUtils.error("Unable to send verification code:"+t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val fdbk = response.body()?.string()
                    if (fdbk != null) {


                    } else {
                        XToastUtils.error("Empty Feedback")
                    }
                }

            })
    }
}

