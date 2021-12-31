package TestWord

import Recite.ReciteActivity
import Recite.SingleWordActivity
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.easychinese.MainActivity
import com.example.easychinese.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xuexiang.xui.widget.progress.CircleProgressView.CircleProgressUpdateListener
import com.xuexiang.xui.widget.progress.HorizontalProgressView.HorizontalProgressUpdateListener
import com.xuexiang.xui.widget.toast.XToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
import org.json.JSONArray
import org.jsoup.Jsoup
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext


class TestingWordActivity : AppCompatActivity(), HorizontalProgressUpdateListener,
    CircleProgressUpdateListener {
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val th = msg.data
            if (th.getInt("digit") == 10) {

            } else {
                findViewById<TextView>(R.id.kjh).text = "" + th.getInt("digit")
            }
        }
    }

    companion object {
        var mark = 0
        var count = -1
        var threadrun = false
        var barStart=0.toFloat()
        var timeConsumeing=0
        var pinyinScore=0
        var translateScore=0
        var start=0L
        var vibrate=true
        var isPa=false
        lateinit var pd: ProgressDialog
        var loadfin=0

    }
    var chinese = ArrayList<String>()
    var english = ArrayList<String>()
    var pinyin = ArrayList<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_testing_word)
        chinese.clear()
        english.clear()
        pinyin.clear()
        loadfin=10
        pinyinScore=0
        translateScore=0
        pd = ProgressDialog(this@TestingWordActivity)
        pd.setMessage("Loading......")
        mark = 0
        count = 0
        timeConsumeing=0
        val check=this.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
        vibrate=check.getBoolean("viborate",true)

//        val jso = intent9.getStringExtra("json")
        val jso=" [\n" +
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
        val jsa = JSONArray(jso)
        val format = HanyuPinyinOutputFormat()
        format.toneType = HanyuPinyinToneType.WITH_TONE_MARK
        format.vCharType = HanyuPinyinVCharType.WITH_U_UNICODE
        format.caseType = HanyuPinyinCaseType.LOWERCASE
        isPa=true
        if (!isPa){//后端模式：从后端的json中解析
            var r = 0
            while (r < 10) {
                val j = jsa.getJSONObject(r)
                chinese.add(j.getString("src"))
                english.add(j.getString("dst"))
                pinyin.add(j.getString("pinyin"))
                r++
            }
        }else{
            //爬虫模式，直接去网上爬
                pd.show()
            val c = ArrayList<String>()
            c.add("汽车")
            c.add("电脑")
            c.add("手机")
            c.add("建筑")
            c.add("国家")
            c.add("瓶子")
            c.add("被子")
            c.add("相机")
            c.add("水")
            c.add("睡觉")

//            for(a in ReciteActivity.chineseList){
            for(a in c){
                thread {
////    val cs=CoroutineScope(context)
//    val job=Job()
//                val csc=CoroutineScope(job)
//                csc.launch(Dispatchers.IO) {
                    val doc0 = Jsoup.connect("https://cn.bing.com/dict/search?q=${a}")
                        .userAgent("Mozilla")
                        .timeout(12000)
                        .post()
                    val doc = doc0.body()
//                    val py=doc.getElementsByClass("hd_p1_1")[0].text()

                    val py= PinyinHelper.toHanYuPinyinString(a,format,"  ",false)

                    var en="null"
                    try {
                        en=doc.getElementsByClass("def b_regtxt")[0].text()
                    }catch (e:IndexOutOfBoundsException){
                        e.printStackTrace()
                    }

                    var en2=en
                    if (en.contains(";")||en.contains(",")){
                        var ii=0
                        var symblecounter=0
                        while (ii<en.length){
                            if(en[ii].equals(';')||en[ii].equals(',')){
                                if (++symblecounter==1){
                                    en2=en.substring(0,ii)
                                }
                                if (++symblecounter==2){
                                    en2=en.substring(0,ii)
                                    break
                                }
                            }
                            ii++
                        }
                    }
                    addMeaningAndPinyin(a,en2,py)
                    decreaseLoad()
                }
            }
        }


    }

    fun showNext(
        page: Int,
        chinese: ArrayList<String>,
        english: ArrayList<String>,
        pinyin: ArrayList<String>
    ) {
        if (page>20){
            val r=Intent(
                this@TestingWordActivity,
                TestingResultActivity::class.java
            )
            r.putExtra("mark",mark*5)
            r.putExtra("difficulty",75)
            r.putExtra("time", (timeConsumeing*0.1).toInt())
            this@TestingWordActivity.startActivity(r)
            return
        }
        var now = page - 1
        count++
        threadrun = true
        findViewById<TextView>(R.id.progress).text = "Progress:" + count + "/20"
        val horizonalBar= this@TestingWordActivity.findViewById<com.xuexiang.xui.widget.progress.HorizontalProgressView>(R.id.hpv_language)
        horizonalBar.setStartProgress(0.toFloat())
        horizonalBar.setEndProgress(barStart+5)
        barStart+=5
        horizonalBar.setProgressViewUpdateListener(this)
        horizonalBar.startProgressAnimation()
        findViewById<TextView>(R.id.great).text = "Greade:" + mark * 5
        findViewById<Button>(R.id.adfds).visibility = View.INVISIBLE
        var correctpos = 0
        var clickedpos = 0
        if (page <= 10) {
            findViewById<TextView>(R.id.question).text = chinese.get(now)
            val time = Math.floor(Math.random() * 5).toInt()
            if (time % 4 == 0) {
                correctpos = 0
                findViewById<TextView>(R.id.a1).text = english.get(now)
                findViewById<TextView>(R.id.a2).text = english.get((now + 2) % 10)
                findViewById<TextView>(R.id.a3).text = english.get((now + 7) % 10)
                findViewById<TextView>(R.id.a4).text = english.get((now + 5) % 10)
            } else if (time % 4 == 1) {
                correctpos = 1
                findViewById<TextView>(R.id.a1).text = english.get((now + 6) % 10)
                findViewById<TextView>(R.id.a2).text = english.get(now)
                findViewById<TextView>(R.id.a3).text = english.get((now + 7) % 10)
                findViewById<TextView>(R.id.a4).text = english.get((now + 3) % 10)
            } else if (time % 4 == 2) {
                correctpos = 2
                findViewById<TextView>(R.id.a1).text = english.get((now + 5) % 10)
                findViewById<TextView>(R.id.a2).text = english.get((now + 7) % 10)
                findViewById<TextView>(R.id.a3).text = english.get(now)
                findViewById<TextView>(R.id.a4).text = english.get((now + 6) % 10)
            } else {
                correctpos = 3
                findViewById<TextView>(R.id.a1).text = english.get((now + 5) % 10)
                findViewById<TextView>(R.id.a2).text = english.get((now + 4) % 10)
                findViewById<TextView>(R.id.a3).text = english.get((now + 6) % 10)
                findViewById<TextView>(R.id.a4).text = english.get(now)
            }

        } else if (page <= 20) {
            findViewById<TextView>(R.id.heading).text="Please choose the correct soundmark"
            findViewById<TextView>(R.id.question).text = chinese.get(now - 10)
            val time = Math.floor(Math.random() * 5).toInt()
            if (time % 4 == 0) {
                correctpos = 0
                findViewById<TextView>(R.id.a1).text = pinyin.get(now - 10)
                findViewById<TextView>(R.id.a2).text = pinyin.get((now + 2) % 10)
                findViewById<TextView>(R.id.a3).text = pinyin.get((now + 7) % 10)
                findViewById<TextView>(R.id.a4).text = pinyin.get((now + 5) % 10)
            } else if (time % 4 == 1) {
                correctpos = 1
                findViewById<TextView>(R.id.a1).text = pinyin.get((now + 6) % 10)
                findViewById<TextView>(R.id.a2).text = pinyin.get(now - 10)
                findViewById<TextView>(R.id.a3).text = pinyin.get((now + 7) % 10)
                findViewById<TextView>(R.id.a4).text = pinyin.get((now + 3) % 10)
            } else if (time % 4 == 2) {
                correctpos = 2
                findViewById<TextView>(R.id.a1).text = pinyin.get((now + 5) % 10)
                findViewById<TextView>(R.id.a2).text = pinyin.get((now + 7) % 10)
                findViewById<TextView>(R.id.a3).text = pinyin.get(now - 10)
                findViewById<TextView>(R.id.a4).text = pinyin.get((now + 6) % 10)
            } else {
                correctpos = 3
                findViewById<TextView>(R.id.a1).text = pinyin.get((now + 5) % 10)
                findViewById<TextView>(R.id.a2).text = pinyin.get((now + 4) % 10)
                findViewById<TextView>(R.id.a3).text = pinyin.get((now + 6) % 10)
                findViewById<TextView>(R.id.a4).text = pinyin.get(now - 10)
            }
        }
        val circle =
            findViewById<com.xuexiang.xui.widget.progress.CircleProgressView>(R.id.progressView_circle_small)
        circle.setProgressViewUpdateListener(this)


        var clicknum = 0
        thread {
            Thread.sleep(800)
            runOnUiThread {
                circle.startProgressAnimation()
            }

            thread {                   //这个线程是倒计时的开启
                var i = 50
                while (i >= 0) {
                    if (threadrun == false) {
                        timeConsumeing+=i
                        break
                    }
                    if (i%10==0){
                        val a = Message.obtain()
                        val b = Bundle()
                        b.putInt("digit", i/10)
                        a.data = b
                        handler.sendMessage(a)
                    }

                    i--
                    if (i == -1) break
                    Thread.sleep(100)
                }
                if (i == -1) {

                    clicknum++
                    runOnUiThread {
                        makeGreen(correctpos)
                        findViewById<Button>(R.id.adfds).visibility = View.VISIBLE
                        XToastUtils.warning(R.string.time_is_up)
                    }

                }

            }



        }



        findViewById<FrameLayout>(R.id.p1).setOnClickListener {
            if (clicknum != 0) {
            } else {
                clickedpos = 0
                clicknum++
                threadrun = false
                circle.stopProgressAnimation()
                if (clickedpos == correctpos) {
                    mark++
                    if (page<=10){
                        translateScore+=10
                    }else{
                        pinyinScore+=10
                    }
                }else{
                    shake(1)
                    if (vibrate)playVibrate()
                }
                makeRed(clickedpos)
                makeGreen(correctpos)
            }

        }
        findViewById<FrameLayout>(R.id.p2).setOnClickListener {
            if (clicknum != 0) {
            } else {
                clickedpos = 1
                clicknum++
                threadrun = false
                circle.stopProgressAnimation()
                if (clickedpos == correctpos) {
                    mark++
                    if (page<=10){
                        translateScore+=10
                    }else{
                        pinyinScore+=10
                    }
                }else{
                    shake(2)
                    if (vibrate)playVibrate()
                }
                makeRed(clickedpos)
                makeGreen(correctpos)
            }

        }
        findViewById<FrameLayout>(R.id.p3).setOnClickListener {
            if (clicknum != 0) {
            } else {
                clickedpos = 2
                clicknum++
                threadrun = false
                circle.stopProgressAnimation()
                if (clickedpos == correctpos) {
                    mark++
                    if (page<=10){
                        translateScore+=10
                    }else{
                        pinyinScore+=10
                    }
                }else{
                    shake(3)
                    if (vibrate)playVibrate()
                }
                makeRed(clickedpos)
                makeGreen(correctpos)
            }
        }
        findViewById<FrameLayout>(R.id.p4).setOnClickListener {
            if (clicknum != 0) {
            } else {
                clickedpos = 3
                clicknum++
                threadrun = false
                circle.stopProgressAnimation()
                if (clickedpos == correctpos) {
                    mark++
                    if (page<=10){
                        translateScore+=10
                    }else{
                        pinyinScore+=10
                    }
                }else{
                    shake(4)
                    if (vibrate)playVibrate()

                }
                makeRed(clickedpos)
                makeGreen(correctpos)
            }
        }
        findViewById<Button>(R.id.adfds).setOnClickListener {
            makewhite()
            showNext(page + 1, chinese, english, pinyin)

        }


    }

    fun makeRed(choose: Int) {
        val resources: Resources = applicationContext.resources
        val btnDrawable: Drawable = resources.getDrawable(R.drawable.crbg6)
        when (choose) {
            0 -> {
                findViewById<FrameLayout>(R.id.p1).background = btnDrawable
            }
            1 -> {
                findViewById<FrameLayout>(R.id.p2).background = btnDrawable
            }
            2 -> {
                findViewById<FrameLayout>(R.id.p3).background = btnDrawable
            }
            3 -> {
                findViewById<FrameLayout>(R.id.p4).background = btnDrawable
            }
        }
        findViewById<Button>(R.id.adfds).visibility = View.VISIBLE
    }

    fun makeGreen(correct: Int) {
        val resources: Resources = applicationContext.resources
        val btnDrawable: Drawable = resources.getDrawable(R.drawable.crbg7)
        when (correct) {
            0 -> {
                findViewById<FrameLayout>(R.id.p1).background = btnDrawable
            }
            1 -> {
                findViewById<FrameLayout>(R.id.p2).background = btnDrawable
            }
            2 -> {
                findViewById<FrameLayout>(R.id.p3).background = btnDrawable
            }
            3 -> {
                findViewById<FrameLayout>(R.id.p4).background = btnDrawable
            }
        }
    }

    fun makewhite() {
        val resources: Resources = applicationContext.resources
        val btnDrawable: Drawable = resources.getDrawable(R.drawable.crbg5)
        findViewById<FrameLayout>(R.id.p1).background = btnDrawable
        findViewById<FrameLayout>(R.id.p2).background = btnDrawable
        findViewById<FrameLayout>(R.id.p3).background = btnDrawable
        findViewById<FrameLayout>(R.id.p4).background = btnDrawable
        val ani=AnimationUtils.loadAnimation(this@TestingWordActivity,R.anim.left)
        val ani2=AnimationUtils.loadAnimation(this@TestingWordActivity,R.anim.right)
        findViewById<FrameLayout>(R.id.p0).startAnimation(ani2)
        findViewById<FrameLayout>(R.id.p1).startAnimation(ani)
        findViewById<FrameLayout>(R.id.p2).startAnimation(ani2)
        findViewById<FrameLayout>(R.id.p3).startAnimation(ani)
        findViewById<FrameLayout>(R.id.p4).startAnimation(ani2)

    }

    fun shake(pos:Int)=when(pos){
        1->{findViewById<FrameLayout>(R.id.p1).startAnimation(AnimationUtils.loadAnimation(this@TestingWordActivity,R.anim.left_right))}
        2->{findViewById<FrameLayout>(R.id.p2).startAnimation(AnimationUtils.loadAnimation(this@TestingWordActivity,R.anim.left_right))}
        3->{findViewById<FrameLayout>(R.id.p3).startAnimation(AnimationUtils.loadAnimation(this@TestingWordActivity,R.anim.left_right))}
        4->{findViewById<FrameLayout>(R.id.p4).startAnimation(AnimationUtils.loadAnimation(this@TestingWordActivity,R.anim.left_right))}
        else->{}
    }
    @SuppressLint("CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.O)
    fun timeStart(cont:Context){
        val edi=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE).edit()
        val check=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
        val date= LocalDate.now()
        val year=date.year
        val month=date.monthValue
        val day=date.dayOfMonth
        SingleWordActivity.start =System.currentTimeMillis()

        //获取SharedPreference里面的日期，如果和今天相同就OK，不一样就把todayMillis更新

        if (check.getInt("year",0)==year&&check.getInt("month",0)==month&&check.getInt("day",0)==day){

        }else{
            edi.putLong("todayMillis",0)
            edi.putInt("year",year)
            edi.putInt("month",month)
            edi.putInt("day",day)
        }
        edi.apply()
    }
    fun timeExit(cont:Context){
        val diff=System.currentTimeMillis()- SingleWordActivity.start
        val edi=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE).edit()
        val check=cont.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
        val todayMillis=check.getLong("todayMillis",0)
        val totalMillis=check.getLong("totalMillis",0)
        edi.putLong("todayMillis",todayMillis+diff)
        edi.putLong("totalMillis",totalMillis+diff)
        edi.apply()
    }
    override fun onHorizontalProgressStart(view: View?) {

    }

    override fun onHorizontalProgressUpdate(view: View?, progress: Float) {

    }

    override fun onHorizontalProgressFinished(view: View?) {

    }

    override fun onCircleProgressStart(view: View?) {

    }

    override fun onCircleProgressUpdate(view: View?, progress: Float) {

    }

    override fun onCircleProgressFinished(view: View?) {

    }

//    override fun onStop() {
//        super.onStop()
//        timeExit(this)
//        this.finish()
//    }

    override fun onPause() {
        super.onPause()
        timeExit(this)
        this.finish()
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onRestart() {
//        super.onRestart()
//        timeStart(this)
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
         mark = 0
         count = -1
         threadrun = false
         barStart=0.toFloat()
         timeConsumeing=0
         pinyinScore=0
         translateScore=0
        timeStart(this)
    }
    fun playVibrate() {
        val vibrator = this@TestingWordActivity.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        val vibrationPattern = longArrayOf(0, 180, 80, 120)
        // 第一个参数为开关开关的时间，第二个参数是重复次数，振动需要添加权限
        vibrator.vibrate(vibrationPattern, -1)
    }

    @Synchronized
    fun decreaseLoad(){
        loadfin--
        if(loadfin<=0) {
            pd.dismiss()
            var pagenow = 1
            runOnUiThread {
                showNext(pagenow, chinese, english, pinyin)
            }

        }
    }

    @Synchronized
    fun addMeaningAndPinyin(cn:String,yisi:String,py:String){
        chinese.add(cn)
        english.add(yisi)
        pinyin.add(py)
    }

}