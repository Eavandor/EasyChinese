package TestWord

import Recite.ReciteActivity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import com.example.easychinese.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import user.UserActivity

class TestWordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_test_word)
        //底部导航栏部分：
        var  mBottomNavigationView4: BottomNavigationView = findViewById<BottomNavigationView>(R.id.nav2)
        mBottomNavigationView4.selectedItemId=R.id.tools
        initBottomNavigation(this)

    }
    fun initBottomNavigation(cont:Context) {
        var  mBottomNavigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.nav2)
        // 添加监听
        mBottomNavigationView.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.getItemId()) {
                    R.id.myhome -> {var intent= Intent(cont,ReciteActivity::class.java)
                        startActivity(intent)}         //底部导航栏点击第一个，前往背单词界面
                    R.id.tools ->{}
                    R.id.me ->{var intent= Intent(cont,UserActivity::class.java)
                        startActivity(intent)}

                    else -> {
                    }
                }
                // 这里注意返回true,否则点击失效
                return true
            }
        })
    }
    override fun onStop() {
        super.onStop()
        this.finish()
    }

    override fun onPause() {
        super.onPause()
        this.finish()
    }
}