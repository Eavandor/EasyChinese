package Recite

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.widget.Button
import com.example.easychinese.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_web_view)
        var intent9=intent
        val jso=intent9.getStringExtra("url")
        findViewById<WebView>(R.id.adssfds).loadUrl(jso!!)

findViewById<Button>(R.id.hkjhk).setOnClickListener {
    finish()
}
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