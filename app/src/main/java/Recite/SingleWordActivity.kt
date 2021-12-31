package Recite

import TestWord.TestingWordActivity
import TestWord.XToastUtils
import Utils.ToastUtil
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.easychinese.R
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread

class SingleWordActivity : AppCompatActivity() {
    companion object{
        var wordNow=""
        var start=0L
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()



        setContentView(R.layout.activity_single_word)
        start=Date().time
        var intent9=intent
        val jso=intent9.getStringExtra("json")
val jsa=JSONArray(jso)
        val word=findViewById<TextView>(R.id.wordCap)
        val pinying=findViewById<TextView>(R.id.pingyin)
        val translation=findViewById<TextView>(R.id.yisi)
        val zwlj=findViewById<TextView>(R.id.zwlj)
        val ywlj=findViewById<TextView>(R.id.ywlj)
        var current=0
        rool(this,jsa,word,pinying,translation, zwlj, ywlj,current)
        current++
        findViewById<Button>(R.id.nextw).setOnClickListener {
            rool(this,jsa,word,pinying,translation, zwlj, ywlj,current)
            current++
            thread {
                runOnUiThread {             findViewById<ScrollView>(R.id.jkhkjh).fullScroll(ScrollView.FOCUS_UP)   //回到顶部
                }
            }

        }
        findViewById<Button>(R.id.seemore).setOnClickListener {
            val intent=Intent(this, WebViewActivity::class.java)
            intent.putExtra("url","http://cn.bing.com/dict/search?q=${wordNow}&qs=n&form=Z9LH5&sp=-1&pq=qi%27c&sc=0-4&sk=&cvid=0F47084B710A48EC9B56D0BB75751F3A")
            startActivity(intent)
        }
    }
    fun rool(conte:Context,jsa:JSONArray,word:TextView,pinying:TextView,translation:TextView,zwlj:TextView,ywlj:TextView,current:Int){
        var fistjo=JSONObject()

        if(current<10){
            fistjo=jsa.getJSONObject(current)
            word.text=fistjo.getString("src")
            wordNow=fistjo.getString("src")
            pinying.text=fistjo.getString("pinyin")
            translation.text=fistjo.getString("dst")
            zwlj.text=fistjo.getString("example___src")
            ywlj.text=fistjo.getString("example___dst")
        }else{

            XToastUtils.info(R.string.finish_reciting)
            conte.startActivity(Intent(this@SingleWordActivity,ReciteActivity::class.java))
        }
    }

    @SuppressLint("CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.O)
    fun timeStart(cont:Context){
        val edi=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE).edit()
        val check=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
        val date=LocalDate.now()
        val year=date.year
        val month=date.monthValue
        val day=date.dayOfMonth
        start=System.currentTimeMillis()

        //获取SharedPreference里面的日期，如果和今天相同就OK，不一样就把todayMillis更新
        if (check.getInt("year",0)==year&&check.getInt("month",0)==month&&check.getInt("day",0)==day){
edi.putInt("todayWord",check.getInt("todayWord",0)+10)

        }else{
            edi.putLong("todayMillis",0)
            edi.putInt("year",year)
            edi.putInt("month",month)
            edi.putInt("day",day)
            edi.putInt("todayWord",0)
        }
        edi.apply()
    }
    fun timeExit(cont:Context){
        val diff=System.currentTimeMillis()- start
        val edi=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE).edit()
        val check=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
        val todayMillis=check.getLong("todayMillis",0)
        val totalMillis=check.getLong("totalMillis",0)
        edi.putLong("todayMillis",todayMillis+diff)
        edi.putLong("totalMillis",totalMillis+diff)
        edi.apply()
    }


//    override fun onStart() {
//        super.onStart()
//        start=Date().time
//    }
    override fun onPause() {
        super.onPause()
timeExit(this)

    }

//    override fun onStop() {
//        super.onStop()
//        timeExit(this)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        timeExit(this)
//    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        timeStart(this)
    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onRestart() {
//        super.onRestart()
//        timeStart(this)
//    }
}