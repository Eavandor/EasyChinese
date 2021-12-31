package user

import LoginAndCreateAccount.LoginActivity
import Recite.ReciteActivity
import Recite.getUserInfo
import Services.AnnouncementService
import TestWord.TestWordActivity
import TestWord.TestingWordActivity
import TestWord.XToastUtils
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.easychinese.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date
import java.time.LocalDate
import java.util.*

class UserActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_user)
        val ch=getSharedPreferences("userinfo",Context.MODE_PRIVATE)
        val ton=ch.getString("token","")
        val u=ch.getString("uid","")
        if (ton!=null&&u!=null)getUserInfo(u,ton,this,findViewById(R.id.nickname))
        setRecord(this)
        //底部导航栏部分：
        var  mBottomNavigationView4: BottomNavigationView = findViewById<BottomNavigationView>(R.id.nav3)
        mBottomNavigationView4.selectedItemId=R.id.me
        initBottomNavigation(this)
        //顶部小日历实现部分：
        var todayOfWeek= LocalDate.now().dayOfWeek.value    //得到现在的时间，用毫秒表示
        var minu1=todayOfWeek-1
        var mi=minu1.toLong()                           //今天的时间减去一天
        var day1_1= LocalDate.now().minusDays(mi)       //这周一的时间（mi是今天的时间减去一天的毫秒），比如说，求周一（Long类型的），
        var day1=day1_1.dayOfMonth                      //周五日期（毫秒）减去周四日期（毫秒）=周一日期（毫秒），周三日期（毫秒）减去周二日期（毫秒）=周一
        var day2=day1_1.plusDays(1).dayOfMonth   //day1是周一的日期，周二周三以此类推，依次加1天
        var day3=day1_1.plusDays(2).dayOfMonth     //得到周一到周天每一天的日期。
        var day4=day1_1.plusDays(3).dayOfMonth
        var day5=day1_1.plusDays(4).dayOfMonth
        var day6=day1_1.plusDays(5).dayOfMonth
        var day7=day1_1.plusDays(6).dayOfMonth

        findViewById<TextView>(R.id.mon2).text=""+day1        //把得到的周一到周天每一天的日期，都放进对应的控件中显示
        findViewById<TextView>(R.id.tue2).text=""+day2        //（按照本周）日历制作完成
        findViewById<TextView>(R.id.wen2).text=""+day3
        findViewById<TextView>(R.id.thu2).text=""+day4
        findViewById<TextView>(R.id.fri2).text=""+day5
        findViewById<TextView>(R.id.sat2).text=""+day6
        findViewById<TextView>(R.id.sun2).text=""+day7

        when(todayOfWeek){                                                  //得出今天是星期几
            1->{ findViewById<TextView>(R.id.mon2).setTextColor(Color.BLACK)     //如果是星期一，把星期一所在的TextView控件字体变成黑色
                findViewById<TextView>(R.id.mon2).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
                findViewById<TextView>(R.id.mon1).setTextColor(Color.BLACK)        //要改两个控件，一个显示月份中的日期数字，另一个显示星期几
                findViewById<TextView>(R.id.mon1).getPaint().setFakeBoldText(true)}

            2->{ findViewById<TextView>(R.id.tue2).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.tue1).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.tue2).getPaint().setFakeBoldText(true)
                findViewById<TextView>(R.id.tue1).getPaint().setFakeBoldText(true)}
            3->{ findViewById<TextView>(R.id.wen2).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.wen1).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.wen1).getPaint().setFakeBoldText(true)
                findViewById<TextView>(R.id.wen2).getPaint().setFakeBoldText(true)}
            4->{ findViewById<TextView>(R.id.thu2).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.thu1).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.thu1).getPaint().setFakeBoldText(true)
                findViewById<TextView>(R.id.thu2).getPaint().setFakeBoldText(true)}
            5->{ findViewById<TextView>(R.id.fri2).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.fri1).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.fri1).getPaint().setFakeBoldText(true)
                findViewById<TextView>(R.id.fri2).getPaint().setFakeBoldText(true)}
            6->{ findViewById<TextView>(R.id.sat2).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.sat1).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.sat1).getPaint().setFakeBoldText(true)
                findViewById<TextView>(R.id.sat2).getPaint().setFakeBoldText(true)}
            7->{ findViewById<TextView>(R.id.sun2).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.sun1).setTextColor(Color.BLACK)
                findViewById<TextView>(R.id.sun1).getPaint().setFakeBoldText(true)
                findViewById<TextView>(R.id.sun2).getPaint().setFakeBoldText(true)}

        }
        var mon="Oct."
        when(LocalDate.now().monthValue){
            1->{mon="Jan."}
            2->{mon="Feb."}
            3->{mon="Mar."}
            4->{mon="Apr."}
            5->{mon="May"}
            6->{mon="Jun."}
            7->{mon="Jul."}
            8->{mon="Aug."}
            9->{mon="Sep."}
            10->{mon="Oct."}
            11->{mon="Nov."}
            12->{mon="Dec."}
        }
        findViewById<TextView>(R.id.khjkads).text=mon
        findViewById<Button>(R.id.signin).setOnClickListener {
            val edi=this@UserActivity.getSharedPreferences("time_manager",Context.MODE_PRIVATE).edit()
            edi.putString("compare","${day1_1.year}${day1_1.dayOfYear}")
            when(LocalDate.now().dayOfWeek.value){
                1->{findViewById<TextView>(R.id.mon2).setTextColor(Color.GREEN)
                edi.putBoolean("mon",true)
                XToastUtils.success("Sign in successfully!")}
                2->{findViewById<TextView>(R.id.tue2).setTextColor(Color.GREEN)
                    edi.putBoolean("tue",true)
                    XToastUtils.success("Sign in successfully!")}
                3->{findViewById<TextView>(R.id.wen2).setTextColor(Color.GREEN)
                    edi.putBoolean("wed",true)
                    XToastUtils.success("Sign in successfully!")}
                4->{findViewById<TextView>(R.id.thu2).setTextColor(Color.GREEN)
                    edi.putBoolean("thu",true)
                    XToastUtils.success("Sign in successfully!")}
                5->{findViewById<TextView>(R.id.fri2).setTextColor(Color.GREEN)
                    edi.putBoolean("fri",true)
                    XToastUtils.success("Sign in successfully!")}
                6->{findViewById<TextView>(R.id.sat2).setTextColor(Color.GREEN)
                    edi.putBoolean("sat",true)
                    XToastUtils.success("Sign in successfully!")}
                7->{findViewById<TextView>(R.id.sun2).setTextColor(Color.GREEN)
                    edi.putBoolean("sun",true)
                    XToastUtils.success("Sign in successfully!")}
            }
            edi.apply()
        }
        showSignIn()
        findViewById<ImageView>(R.id.shezhi).setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }

    }
    fun initBottomNavigation(cont: Context) {
        var  mBottomNavigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.nav3)
        // 添加监听
        mBottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.myhome -> {val intent= Intent(cont, ReciteActivity::class.java)
                        startActivity(intent)}
                    R.id.tools ->{
                        var group=0
                        if (ReciteActivity.testarr.size>0){
                            group= ReciteActivity.testarr[ReciteActivity.testarr.size-1]
                            ReciteActivity.testarr.removeAt(ReciteActivity.testarr.size-1)
                        }else{
                            if (ReciteActivity.arr.size==0){
                                XToastUtils.info("You haven't recited any words,please recite some words before testing.")
                            }else{
                                ReciteActivity.testarr = ReciteActivity.arr
                                group= ReciteActivity.testarr[ReciteActivity.testarr.size-1]
                                ReciteActivity.testarr.removeAt(ReciteActivity.testarr.size-1)
                            }
                        }
//                        getGroup(this@UserActivity, LoginActivity.uid,""+group)
                        getGroup(this@UserActivity, ReciteActivity.current_dict0,""+group)
                    }
                    R.id.me ->{}

                    else -> {
                    }
                }
                // 这里注意返回true,否则点击失效
                return true
            }
        })
    }

    fun getGroup(conte:Context,uid:String,group:String){
        val token=conte.getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("token","")

        if (token != null) {
            LoginActivity.publicretrofit.create(AnnouncementService::class.java)
                .getSingleCard(uid,group,token)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            conte,
                            "请求失败！！！原因：\n" + t.message,
                            Toast.LENGTH_LONG
                        ).show();
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        var fdbk = response.body()?.string()
                        if (fdbk != null) {
//                            val intent= Intent(conte, TestingWordActivity::class.java)
//                            intent.putExtra("json", fdbk)
////                            intent.putExtra("json", ReciteActivity.jso)
//                            conte.startActivity(intent)


                            ReciteActivity.chineseList.clear()
                            val ja= JSONArray(fdbk)
                            var r=0
                            while (r<10){
                                ReciteActivity.chineseList.add(ja.getJSONObject(r).getString("src"))
                                r++
                            }
                            val intent = Intent(conte, TestingWordActivity::class.java)
                            intent.putExtra("json", fdbk)
                            conte.startActivity(intent)

                        } else {
                            Toast.makeText(conte, "Empty feedback", Toast.LENGTH_LONG)
                                .show();
                        }
                    }

                })
        }
    }

    fun setRecord(cont:Context){
        val check=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
        val today=check.getLong("todayMillis",0)/60000
        val total=check.getLong("totalMillis",0)/60000
        val lv_now=check.getInt("lv_now",0)
        val mon=check.getBoolean("mon",false)
        val tue=check.getBoolean("tue",false)
        val wed=check.getBoolean("wed",false)
        val thu=check.getBoolean("thu",false)
        val fri=check.getBoolean("fri",false)
        val sat=check.getBoolean("sat",false)
        val sun=check.getBoolean("sun",false)
        var counter=0
        if (mon)counter++
        if (tue)counter++
        if (wed)counter++
        if (thu)counter++
        if (fri)counter++
        if (sat)counter++
        if (sun)counter++
        findViewById<TextView>(R.id.continous).text="${counter} Days"

        findViewById<TextView>(R.id.t3).text=""+today
        findViewById<TextView>(R.id.t4).text=""+total
        findViewById<TextView>(R.id.t1).text=""+check.getInt("todayWord",0)
        findViewById<TextView>(R.id.t2).text=""+check.getInt("totalWord",0)
        if (lv_now>=1){
            findViewById<ImageView>(R.id.xunzhang1).setImageResource(R.drawable.xunzhang)
            findViewById<TextView>(R.id.lv1).setTextColor(Color.BLACK)
        }
        if (lv_now>=2){
            findViewById<ImageView>(R.id.xunzhang2).setImageResource(R.drawable.xunzhang)
            findViewById<TextView>(R.id.lv2).setTextColor(Color.BLACK)
        }
        if (lv_now>=3){
            findViewById<ImageView>(R.id.xunzhang3).setImageResource(R.drawable.xunzhang)
            findViewById<TextView>(R.id.lv3).setTextColor(Color.BLACK)
        }
        if (lv_now>=4){
            findViewById<ImageView>(R.id.xunzhang4).setImageResource(R.drawable.xunzhang)
            findViewById<TextView>(R.id.lv4).setTextColor(Color.BLACK)
        }
        if (lv_now==5){
            findViewById<ImageView>(R.id.xunzhang5).setImageResource(R.drawable.xunzhang)
            findViewById<TextView>(R.id.lv5).setTextColor(Color.BLACK)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun showSignIn(){

        val check=this@UserActivity.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
        val compare=check.getString("compare","")

        val todayOfWeek= LocalDate.now().dayOfWeek.value
        val minu1=todayOfWeek-1
        val mi=minu1.toLong()                           //今天的时间减去一天
        val day1_1= LocalDate.now().minusDays(mi)       //这周一的时间（mi是今天的时间减去一天的毫秒），比如说，求周一（Long类型的），
        val date="${day1_1.year}${day1_1.dayOfYear}"
        if (date!=compare){
            val edi=check.edit()
            edi.putBoolean("mon",false)
            edi.putBoolean("tue",false)
            edi.putBoolean("wed",false)
            edi.putBoolean("thu",false)
            edi.putBoolean("fri",false)
            edi.putBoolean("sat",false)
            edi.putBoolean("sun",false)
            edi.apply()
        }


        val mon=check.getBoolean("mon",false)
        val tue=check.getBoolean("tue",false)
        val wed=check.getBoolean("wed",false)
        val thu=check.getBoolean("thu",false)
        val fri=check.getBoolean("fri",false)
        val sat=check.getBoolean("sat",false)
        val sun=check.getBoolean("sun",false)

        if (mon)findViewById<TextView>(R.id.mon2).setTextColor(Color.GREEN)
        if (tue)findViewById<TextView>(R.id.tue2).setTextColor(Color.GREEN)
        if (wed)findViewById<TextView>(R.id.wen2).setTextColor(Color.GREEN)
        if (thu)findViewById<TextView>(R.id.thu2).setTextColor(Color.GREEN)
        if (fri)findViewById<TextView>(R.id.fri2).setTextColor(Color.GREEN)
        if (sat)findViewById<TextView>(R.id.sat2).setTextColor(Color.GREEN)
        if (sun)findViewById<TextView>(R.id.sun2).setTextColor(Color.GREEN)

    }

    override fun onResume() {
        super.onResume()
        val nickname=getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("nickname","User Name")!!
        findViewById<TextView>(R.id.nickname).text=nickname

    }
}