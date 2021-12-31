package Recite

import LoginAndCreateAccount.LoginActivity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import com.example.easychinese.R
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {
    companion object{
        var isgone=false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)
        isgone=false
        val button=findViewById<Button>(R.id.skip)
       thread{
           Thread.sleep(1000)
           runOnUiThread { button.text="skip2" }
           Thread.sleep(1000)
           runOnUiThread { button.text="skip1" }
           Thread.sleep(1000)
           if (!isgone)
           startActivity(Intent(this,LoginActivity::class.java))
        }
        findViewById<Button>(R.id.skip).setOnClickListener {
            isgone=true
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}