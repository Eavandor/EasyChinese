package Recite

import LoginAndCreateAccount.LoginActivity
import Services.AnnouncementService
import TestWord.TestingWordActivity
import TestWord.XToastUtils
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.easychinese.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonArray
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import user.UserActivity
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class ReciteActivity : AppCompatActivity() {
    companion object {
        val arr = ArrayList<Int>()
        var testarr = ArrayList<Int>()
        var isNew=false
        lateinit var pd1: ProgressDialog
        var jso = " [\n" +
                "    {\n" +
                "        \"uid\": \"16353359658265150002713607244\",\n" +
                "        \"src\": \"你好\",\n" +
                "        \"pinyin\": \"ni  hao\",\n" +
                "        \"dst\": \"hello\",\n" +
                "        \"example___src\": \"你好呀，好久不见。\",\n" +
                "        \"example___dst\": \"Hello,I haven't see you for a long time.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"1635335965827020000848284867\",\n" +
                "        \"src\": \"吃\",\n" +
                "        \"pinyin\": \"chi\",\n" +
                "        \"dst\": \"eat\",\n" +
                "        \"example___src\": \"我吃不下了\",\n" +
                "        \"example___dst\": \"I can't eat more.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"16353359658274620002811490086\",\n" +
                "        \"src\": \"房子\",\n" +
                "        \"pinyin\": \"fang zi\",\n" +
                "        \"dst\": \"house\",\n" +
                "        \"example___src\": \"这里的房子好多。\",\n" +
                "        \"example___dst\": \"There are so many houses here.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"16353359658278950002085418402\",\n" +
                "        \"src\": \"书\",\n" +
                "        \"pinyin\": \"shu\",\n" +
                "        \"dst\": \"book\",\n" +
                "        \"example___src\": \"图书馆是看书的地方。\",\n" +
                "        \"example___dst\": \"Library is the place to read.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"16353359658283170004047594795\",\n" +
                "        \"src\": \"电脑\",\n" +
                "        \"pinyin\": \"dian nao\",\n" +
                "        \"dst\": \"computer\",\n" +
                "        \"example___src\": \"你的电脑是联想的还是苹果的？\",\n" +
                "        \"example___dst\": \"What's the brand of your computer?Lenove or Apple?\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"1635335965828825000391126221\",\n" +
                "        \"src\": \"单词\",\n" +
                "        \"pinyin\": \"dan ci\",\n" +
                "        \"dst\": \"word\",\n" +
                "        \"example___src\": \"背单词对学习英语很重要\",\n" +
                "        \"example___dst\": \"Reciting words is an important process while learning English.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"1635335965829322000408046826\",\n" +
                "        \"src\": \"手机\",\n" +
                "        \"pinyin\": \"shou ji\",\n" +
                "        \"dst\": \"cellphone\",\n" +
                "        \"example___src\": \"我的手机坏了\",\n" +
                "        \"example___dst\": \"My cellphone is browken.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"16353359658298250001242905684\",\n" +
                "        \"src\": \"汽车\",\n" +
                "        \"pinyin\": \"qi che\",\n" +
                "        \"dst\": \"car\",\n" +
                "        \"example___src\": \"路上有好多汽车\",\n" +
                "        \"example___dst\": \"There so many cars in the road.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"16353359658305260003059775702\",\n" +
                "        \"src\": \"人类\",\n" +
                "        \"pinyin\": \"ren lei\",\n" +
                "        \"dst\": \"human\",\n" +
                "        \"example___src\": \"人类是已知地球上最聪明的生物。\",\n" +
                "        \"example___dst\": \"Human is the most clever creature in the Earth so far.\",\n" +
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"uid\": \"16353359658312800002078604679\",\n" +
                "        \"src\": \"足球\",\n" +          //单词
                "        \"pinyin\": \"zu qiu\",\n" +      //拼音
                "        \"dst\": \"football\",\n" +        //使用者语言的翻译（单词翻译）
                "        \"example___src\": \"你最喜欢哪一支足球队？\",\n" +      //中文例句
                "        \"example___dst\": \"Which football team do you like most?\",\n" +   //目标语言文字例句
                "        \"state\": 0,\n" +
                "        \"level\": 1,\n" +
                "        \"group\": 4,\n" +
                "        \"from\": \"zh\",\n" +
                "        \"to\": \"en\"\n" +
                "    }\n" +
                "]"

        lateinit var msg:TextView
        var token=""
        var uid=""
        val chineseList = ArrayList<String>()
        var current_dict0=""
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        supportActionBar?.hide()
        chineseList.clear()
        chineseList.add("你好")
        chineseList.add("汽车")
        chineseList.add("火车")
        chineseList.add("地铁")
        chineseList.add("轮船")
        chineseList.add("飞机")
        chineseList.add("电脑")
        chineseList.add("手机")
        chineseList.add("房子")
        chineseList.add("集装箱")
        pd1 = ProgressDialog(this)
        pd1.setMessage("Loading......")
        popXunZhang()
        setContentView(R.layout.activity_recite)
        val spf=getSharedPreferences("userinfo",Context.MODE_PRIVATE)
       token=spf.getString("token","")!!
        uid=spf.getString("uid","")!!

        msg=findViewById(R.id.jkhads)
        //底部导航栏部分：
        initBottomNavigation(this)
        //顶部小日历实现部分：
        val todayOfWeek = LocalDate.now().dayOfWeek.value    //得到现在的时间，用毫秒表示
        val minu1 = todayOfWeek - 1
        val mi = minu1.toLong()                           //今天的时间减去一天
        val day1_1 = LocalDate.now().minusDays(mi)       //这周一的时间（mi是今天的时间减去一天的毫秒），比如说，求周一（Long类型的），
        val day1 =
            day1_1.dayOfMonth                      //周五日期（毫秒）减去周四日期（毫秒）=周一日期（毫秒），周三日期（毫秒）减去周二日期（毫秒）=周一
        val day2 = day1_1.plusDays(1).dayOfMonth   //day1是周一的日期，周二周三以此类推，依次加1天
        val day3 = day1_1.plusDays(2).dayOfMonth     //得到周一到周天每一天的日期。
        val day4 = day1_1.plusDays(3).dayOfMonth
        val day5 = day1_1.plusDays(4).dayOfMonth
        val day6 = day1_1.plusDays(5).dayOfMonth
        val day7 = day1_1.plusDays(6).dayOfMonth

        findViewById<TextView>(R.id.mon1).text = "" + day1        //把得到的周一到周天每一天的日期，都放进对应的控件中显示
        findViewById<TextView>(R.id.mon2).text = "" + day2        //（按照本周）日历制作完成
        findViewById<TextView>(R.id.mon3).text = "" + day3
        findViewById<TextView>(R.id.mon4).text = "" + day4
        findViewById<TextView>(R.id.mon5).text = "" + day5
        findViewById<TextView>(R.id.mon6).text = "" + day6
        findViewById<TextView>(R.id.mon7).text = "" + day7
        var mon = "Oct."
        when (LocalDate.now().monthValue) {
            1 -> {
                mon = "Jan."
            }
            2 -> {
                mon = "Feb."
            }
            3 -> {
                mon = "Mar."
            }
            4 -> {
                mon = "Apr."
            }
            5 -> {
                mon = "May"
            }
            6 -> {
                mon = "Jun."
            }
            7 -> {
                mon = "Jul."
            }
            8 -> {
                mon = "Aug."
            }
            9 -> {
                mon = "Sep."
            }
            10 -> {
                mon = "Oct."
            }
            11 -> {
                mon = "Nov."
            }
            12 -> {
                mon = "Dec."
            }
        }
        findViewById<TextView>(R.id.month).text = mon
        when (todayOfWeek) {                                                  //得出今天是星期几
            1 -> {
                findViewById<TextView>(R.id.mon1).setTextColor(Color.GREEN)     //如果是星期一，把星期一所在的TextView控件字体变成黑色
                findViewById<TextView>(R.id.mon1).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
            }
            2 -> {
                findViewById<TextView>(R.id.mon2).setTextColor(Color.GREEN)
                findViewById<TextView>(R.id.mon2).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
            }
            3 -> {
                findViewById<TextView>(R.id.mon3).setTextColor(Color.GREEN)
                findViewById<TextView>(R.id.mon3).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
            }
            4 -> {
                findViewById<TextView>(R.id.mon4).setTextColor(Color.GREEN)
                findViewById<TextView>(R.id.mon4).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
            }
            5 -> {
                findViewById<TextView>(R.id.mon5).setTextColor(Color.GREEN)
                findViewById<TextView>(R.id.mon5).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
            }
            6 -> {
                findViewById<TextView>(R.id.mon6).setTextColor(Color.GREEN)
                findViewById<TextView>(R.id.mon6).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
            }
            7 -> {
                findViewById<TextView>(R.id.mon7).setTextColor(Color.GREEN)
                findViewById<TextView>(R.id.mon7).getPaint().setFakeBoldText(true)  //并且把当天字体加粗
            }

        }
        getUserInfo2(uid,token)
        findViewById<Button>(R.id.fejkshjkfs).setOnClickListener {
isNew=true
//            val intent = Intent(this, SingleWordActivity::class.java)
////            val intent=Intent(this, TestingWordActivity::class.java)
//
//
//            intent.putExtra("json", jso)
//            startActivity(intent)

//            getNewGroup(this,uid, token)
            getNewGroup(this, current_dict0, token)
        }
        //recyclerView部分：
//        val fruitList = ArrayList<Card>()
//        val recyclerView =
//            findViewById<RecyclerView>(R.id.rclrvneedtoreview)

//        getRecited(uid, fruitList, recyclerView,token)


        findViewById<Button>(R.id.modifyPlan).setOnClickListener {
            findViewById<LinearLayout>(R.id.hided).visibility = View.VISIBLE
            findViewById<Button>(R.id.search).setOnClickListener {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(
                    "url",
                    "http://cn.bing.com/dict/search?q=${findViewById<EditText>(R.id.targer_word).text.toString()}&qs=n&form=Z9LH5&sp=-1&pq=qi%27c&sc=0-4&sk=&cvid=0F47084B710A48EC9B56D0BB75751F3A"
                )
                startActivity(intent)
            }
        }
    }

    fun initBottomNavigation(cont: Context) {
        val mBottomNavigationView: BottomNavigationView =
            findViewById<BottomNavigationView>(R.id.nav1)
        mBottomNavigationView.selectedItemId = R.id.myhome
        // 添加监听
        mBottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.myhome -> {
                    }
                    R.id.tools -> {
                        var group=0
                        if (testarr.size>0){
                            group= testarr[testarr.size-1]
                            testarr.removeAt(testarr.size-1)
                        }else{
                            if (arr.size==0){
                                XToastUtils.info("You haven't recited any words,please recite some words before testing.")
                            }else{
                                testarr= arr
                                group= testarr[testarr.size-1]
                                testarr.removeAt(testarr.size-1)
                            }
                        }
//getGroup(this@ReciteActivity,uid,""+group)
                        getGroup(this@ReciteActivity, current_dict0,""+group, token)


//                        val intent = Intent(this@ReciteActivity, TestingWordActivity::class.java)
//                        intent.putExtra("json", jso)
//                        startActivity(intent)
                    }
                    R.id.me -> {
                        val intent = Intent(cont, UserActivity::class.java)
                        startActivity(intent)
                    }

                    else -> {
                    }
                }
                // 这里注意返回true,否则点击失效
                return true
            }
        })
    }

    fun getRecited(uid: String, fruitList: ArrayList<Card>, recyclerView: RecyclerView,token:String) {
        pd1.show()
        arr.clear()
        testarr.clear()
        LoginActivity.publicretrofit.create(AnnouncementService::class.java)
            .getRecitedGroup(uid,token)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    pd1.dismiss()
                    Toast.makeText(
                        getApplicationContext(),
                        "请求失败！！！原因：\n" + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    pd1.dismiss()
                    val fdbk = response.body()?.string()
                    if (fdbk != null) {
//                    val l = "[2,3,4]"
                        val l = fdbk.replace("\n", "").replace(" ", "")
                        var i = 0
                        var str = ""
                        var currentChar = ""
                        while (i < l.length) {
                            currentChar = "" + l[i]
                            if (currentChar == "[") {
                                i++
                                continue
                            }
                            if (currentChar == "," || currentChar == "]") {
                                arr.add(Integer.parseInt(str))
                                testarr.add(Integer.parseInt(str))
                                i++
                                str = ""
                                continue
                            }
                            str += currentChar
                            i++
                        }
                        arr.forEach {
                            fruitList.add(Card("" + it, "1", "20211111"))
                        }
                        findViewById<TextView>(R.id.needreviewwordnum).text = "" + arr.size * 10
                        findViewById<TextView>(R.id.processdigit).text="已学单词：   ${arr.size * 10}/100"
                        var iit=100
                        if (arr.size*10<100){
                            iit=arr.size*10
                        }
                        findViewById<SeekBar>(R.id.skbb).progress=iit
                        findViewById<TextView>(R.id.cardnum).text = "" + arr.size
                        val sher = this@ReciteActivity.getSharedPreferences(
                            "time_manager",
                            Context.MODE_PRIVATE
                        ).edit()
                        sher.putInt("totalWord", fruitList.size * 10)
                        if (fruitList.size >= 5) sher.putBoolean("lv1", true)  //背的单词超过50，lv1登记获得
                        if (fruitList.size >= 50) sher.putBoolean("lv4", true)  //背的单词超过500，lv4登记获得
                        if (fruitList.size >= 100) sher.putBoolean(
                            "lv5",
                            true
                        )  //背的单词超过1000，lv5登记获得
                        val check = this@ReciteActivity.getSharedPreferences(
                            "time_manager",
                            Context.MODE_PRIVATE
                        )
                        if (check.getLong("totalMillis", 0) >= 18000000L) sher.putBoolean(
                            "lv3",
                            true
                        )
                        sher.apply()

                        val layoutManager =
                            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                        recyclerView.layoutManager = layoutManager
                        val adapter = Adapter2(fruitList)
                        recyclerView.adapter = adapter


                    } else {
                        Toast.makeText(this@ReciteActivity, "No feedback", Toast.LENGTH_LONG)
                            .show();
                    }
                }

            })
    }


    fun getGroup(conte: Context, uid: String, group: String,token:String) {
        LoginActivity.publicretrofit.create(AnnouncementService::class.java)
            .getSingleCard(uid, group,token)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        conte,
                        "请求失败！！！原因：\n" + t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val fdbk = response.body()?.string()
                    if (fdbk != null) {
                        chineseList.clear()
                        val ja=JSONArray(fdbk)
                        var r=0
                        while (r<10){
                            chineseList.add(ja.getJSONObject(r).getString("src"))
                            r++
                        }
                        val intent = Intent(conte, TestingWordActivity::class.java)
                        intent.putExtra("json", fdbk)
                        conte.startActivity(intent)

                    } else {
                        Toast.makeText(conte, "Empty feedback", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        initBottomNavigation(this)
    }

    override fun onRestart() {
        super.onRestart()
        initBottomNavigation(this)
    }

    fun popXunZhang() {
        val check = this@ReciteActivity.getSharedPreferences("time_manager", Context.MODE_PRIVATE)
        val edi = check.edit()
        val lv1 = check.getBoolean("lv1", false)
        val lv2 = check.getBoolean("lv2", false)
        val lv3 = check.getBoolean("lv3", false)
        val lv4 = check.getBoolean("lv4", false)
        val lv5 = check.getBoolean("lv5", false)
        val lv1_poped = check.getBoolean("lv1_poped", false)
        val lv2_poped = check.getBoolean("lv2_poped", false)
        val lv3_poped = check.getBoolean("lv3_poped", false)
        val lv4_poped = check.getBoolean("lv4_poped", false)
        val lv5_poped = check.getBoolean("lv5_poped", false)
        val lv_now = check.getInt("lv_now", 0)
        var pop_target = -1
        if (lv1 && !lv1_poped) {
            pop_target = 1
            edi.putBoolean("lv1_poped", true)
        } else if (lv2 && !lv2_poped) {
            pop_target = 2
            edi.putBoolean("lv2_poped", true)
        } else if (lv3 && !lv3_poped) {
            pop_target = 3
            edi.putBoolean("lv3_poped", true)
        } else if (lv4 && !lv4_poped) {
            pop_target = 4
            edi.putBoolean("lv4_poped", true)
        } else if (lv5 && !lv5_poped) {
            pop_target = 5
            edi.putBoolean("lv5_poped", true)
        }
        var msg = ""
        when (pop_target) {
            1 -> {
                msg =
                    "    You have recited 50 words,and now your level increases to: LV.${lv_now + 1}"
            }
            2 -> {
                msg =
                    "    You got more than 80 in a test,and now your level increases to: LV.${lv_now + 1}"
            }
            3 -> {
                msg =
                    "    Your accumulated learning time exceeds 5 hours,and now your level increases to: LV.${lv_now + 1}"
            }
            4 -> {
                msg =
                    "    You have recited 500 words,and now your level increase to: LV.${lv_now + 1}"
            }
            5 -> {
                msg =
                    "    You have recited 1000 words,and now your level increases to: LV.${lv_now + 1}"
            }
        }
        if (msg.length > 2) {
            edi.putInt("lv_now", lv_now + 1)
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@ReciteActivity)
//            AlertDialog.Builder(this@ReciteActivity, R.style.AlertDialog)

            val view: View = View.inflate(application, R.layout.xunzhang, null)
            builder.setView(view)
            builder.setCancelable(true)
            builder.setTitle("Congratulations!")
            builder.setPositiveButton("OK") { dialog, which ->
            }
            builder.setNegativeButton("Cancel") { dialog,
                                                  which ->
            }
            view.findViewById<TextView>(R.id.text).text = msg
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

//        edi.apply()
//        edi.putBoolean("lv1",false)
//        edi.putBoolean("lv2",false)
//        edi.putBoolean("lv3",false)
//        edi.putBoolean("lv4",false)
//        edi.putBoolean("lv5",false)
//        edi.putBoolean("lv1_poped",false)
//        edi.putBoolean("lv2_poped",false)
//        edi.putBoolean("lv3_poped",false)
//        edi.putBoolean("lv4_poped",false)
//        edi.putBoolean("lv5_poped",false)
//        edi.putInt("lv_now",0)
        edi.apply()

    }
fun addChineseWord(j:String){
    chineseList.clear()
    val job=JSONArray(j)
    var i=0
    while (i<10){
        chineseList.add(job.getString(i))
        i++
    }
}


    fun getUserInfo2(uid:String,token:String){
        LoginActivity.publicretrofit.create(AnnouncementService::class.java)
            .getUserInfo(uid,token)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    XToastUtils.error("Unable to get user information:"+t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val fdbk = response.body()?.string()
                    if (fdbk != null) {
                        val userinfo=JSONObject(fdbk).getJSONObject("User")
                        val nickname=userinfo.getString("nickname")
                        val country=userinfo.getString("country")
                        val current_dict=userinfo.getString("current_dict")
                        current_dict0=current_dict
                        val spf=this@ReciteActivity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit()
                        spf.putString("nickname",nickname)
                        spf.putString("country",country)
                        spf.putString("current_dict",current_dict)
                        spf.apply()

                        //recyclerView部分：
                        val fruitList = ArrayList<Card>()
                        val recyclerView =
                            findViewById<RecyclerView>(R.id.rclrvneedtoreview)

                        getRecited(current_dict, fruitList, recyclerView, Companion.token)

                    } else {
                        XToastUtils.error("No User information")
                    }
                }

            })
    }
}