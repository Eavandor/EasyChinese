package TestWord

import Recite.ReciteActivity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.easychinese.R
import com.xuexiang.xui.widget.progress.CircleProgressView
import com.xuexiang.xui.widget.progress.HorizontalProgressView

class TestingResultActivity : AppCompatActivity(),
    HorizontalProgressView.HorizontalProgressUpdateListener,
    CircleProgressView.CircleProgressUpdateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_testing_result)
        findViewById<Button>(R.id.jkjhkads).setOnClickListener {
            this.finish()
            startActivity(Intent(this, ReciteActivity::class.java))
        }
        val ani3 = AnimationUtils.loadAnimation(this, R.anim.in1)
        findViewById<LinearLayout>(R.id.asdad).startAnimation(ani3)
        val ani4 = AnimationUtils.loadAnimation(this, R.anim.in2)
        findViewById<LinearLayout>(R.id.hkjhkj).startAnimation(ani4)
        val ani5 = AnimationUtils.loadAnimation(this, R.anim.in3)
        findViewById<Button>(R.id.jkjhkads).startAnimation(ani5)
        val intent = intent
        val mark = intent.getIntExtra("mark", 80)
        val timemark = intent.getIntExtra("time", 80)
        val difficulty = intent.getIntExtra("difficulty", 75)
        if (this.getSharedPreferences("time_manager", Context.MODE_PRIVATE)
                .getBoolean("lv2", false) == false && mark >= 80
        ) {
            val edi = this.getSharedPreferences("time_manager", Context.MODE_PRIVATE).edit()
            edi.putBoolean("lv2", true)             //若分数大于80，且lv2未获得，把lv2变成true
            edi.apply()
        }
        findViewById<LinearLayout>(R.id.asdad).setOnClickListener {
            goToRadar(
                mark.toFloat(),
                timemark.toFloat(),
                difficulty.toFloat(),
                TestingWordActivity.pinyinScore.toFloat(),
                TestingWordActivity.translateScore.toFloat()
            )
        }
        findViewById<LinearLayout>(R.id.hkjhkj).setOnClickListener {
            goToRadar(
                mark.toFloat(),
                timemark.toFloat(),
                difficulty.toFloat(),
                TestingWordActivity.pinyinScore.toFloat(),
                TestingWordActivity.translateScore.toFloat()
            )
        }
        findViewById<Button>(R.id.more).setOnClickListener {
            goToRadar(
                mark.toFloat(),
                timemark.toFloat(),
                difficulty.toFloat(),
                TestingWordActivity.pinyinScore.toFloat(),
                TestingWordActivity.translateScore.toFloat()
            )
        }


        val progressViewCircleMain = findViewById<CircleProgressView>(R.id.progressView_circle_main)
        val bar1 = findViewById<HorizontalProgressView>(R.id.hpv_language)
        val bar2 = findViewById<HorizontalProgressView>(R.id.hpv_language2)
        val bar3 = findViewById<HorizontalProgressView>(R.id.hpv_language3)

        progressViewCircleMain.setGraduatedEnabled(true)
        progressViewCircleMain.setProgressViewUpdateListener(this)
        progressViewCircleMain.setEndProgress(mark.toFloat())
        progressViewCircleMain.startProgressAnimation()

        bar1.setProgressViewUpdateListener(this)
        bar2.setProgressViewUpdateListener(this)
        bar3.setProgressViewUpdateListener(this)
        bar1.setEndProgress(timemark.toFloat())
        bar2.setEndProgress(mark.toFloat())
        bar3.setEndProgress(difficulty.toFloat())

        var msg = ""
        if (mark <= 40) {
            msg += "    It's a pity that you didn't get a high mark in this test,keep trying"
            if (difficulty > 60) {
                msg += "But don't worry,the difficulty of those word is ${difficulty}, it may be too hard.You can learn some easier word before that."
            }
        } else if (mark <= 60) {
            msg += "    You got ${mark} in this test,it's not that high, but don't worry,it would be hard when you start to learn a new language."
            if (difficulty > 60) {
                msg += "But don't worry,the difficulty of those word is ${difficulty}, it may be too hard.You can learn some easier word before that."
            }
        } else if (mark <= 80) {
            msg += "    Congratulations!!! You got ${mark} in this test,your mark is higher than ${(73 + Math.random() * 10).toInt()}% students."
        } else {
            msg += "    Congratulations!!! You got ${mark} in this test,your mark is higher than ${(85 + Math.random() * 10).toInt()}% students."
        }
        if (timemark < 40) {
            msg += "By the way,you are not fast enough when you are testing,it might because you are not familiar enough with those words,please remember to review new vocabulary regularly,I'm sure that you will do better next time!"
        } else {
            msg += "By the way,you finished the test in a short time."
            if (mark > 60) {
                msg += "It may because you are very familiar with those words,well done!"
            } else {
                msg += "People tends to be careless when they don't have enough time to do something.Take your time,and think carefully,I'm sure that you will do better next time!"
            }

        }
        findViewById<TextView>(R.id.kjhds).text = msg


    }

    override fun onHorizontalProgressStart(view: View?) {

    }

    override fun onHorizontalProgressUpdate(view: View?, progress: Float) {

    }

    override fun onHorizontalProgressFinished(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.hpv_language -> {
                    findViewById<com.xuexiang.xui.widget.progress.HorizontalProgressView>(R.id.hpv_language2).startProgressAnimation()
                }
                R.id.hpv_language2 -> {
                    findViewById<com.xuexiang.xui.widget.progress.HorizontalProgressView>(R.id.hpv_language3).startProgressAnimation()
                }
                R.id.hpv_language3 -> {
                }
            }
        }
    }

    override fun onCircleProgressStart(view: View?) {

    }

    override fun onCircleProgressUpdate(view: View?, progress: Float) {
        if (view != null) {
            if (view.id == R.id.progressView_circle_main) {
                findViewById<TextView>(R.id.jkh).text = "" + progress.toInt()
            }
        }

    }

    override fun onCircleProgressFinished(view: View?) {
        if (view != null) {
            if (view.id == R.id.progressView_circle_main) {
                findViewById<com.xuexiang.xui.widget.progress.HorizontalProgressView>(R.id.hpv_language).startProgressAnimation()

            }
        }

    }

    //    override fun onStop() {
//        super.onStop()
//        this.finish()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        this.finish()
//    }
    fun goToRadar(total: Float, speed: Float, difficulty: Float, pinyin: Float, meaning: Float) {
        val i = Intent(this@TestingResultActivity, RadarChartActivity::class.java)
        i.putExtra("totalScore", total)
        i.putExtra("difficulty", difficulty)
        i.putExtra("speed", speed)
        i.putExtra("pinyin", pinyin)
        i.putExtra("meaning", meaning)
        this@TestingResultActivity.startActivity(i)
    }
}