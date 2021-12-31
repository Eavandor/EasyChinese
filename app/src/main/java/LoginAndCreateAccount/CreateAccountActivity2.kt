package LoginAndCreateAccount

import Recite.registerUserInfo
import TestWord.XToastUtils
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.easychinese.R

class CreateAccountActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account2)
        findViewById<Button>(R.id.register2).setOnClickListener {
            val nickname=findViewById<EditText>(R.id.nkname).text.toString()
            val country=findViewById<EditText>(R.id.nation).text.toString()
            if (hintEmpty(nickname,country)){
                val check=getSharedPreferences("userinfo",Context.MODE_PRIVATE)!!
                val uid=check.getString("uid","")!!
                val password=check.getString("password","")!!
                registerUserInfo(uid,password,this,nickname,country)
            }
        }
        findViewById<LinearLayout>(R.id.parentlayout2).setOnClickListener {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }


    fun hintEmpty(s1:String,s2:String):Boolean{
        var msg="Please enter your "
        val ed1=findViewById<LinearLayout>(R.id.asdasd)
        val ed2=findViewById<LinearLayout>(R.id.dbyfd1)
        if (s1==""&&s2==""){
            msg+="nickname and country."
            ed2.startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity2,R.anim.left_right))
            ed1.startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity2,R.anim.left_right))
            return false
        }else if (s1==""){
            msg+="nickname."
            ed1.startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity2,R.anim.left_right))
            return false
        }else if (s2==""){
            msg+="country."
            ed2.startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity2,R.anim.left_right))
            return false
        }
        return true
    }
}