package LoginAndCreateAccount

import Recite.ReciteActivity
import Services.AnnouncementService
import TestWord.XToastUtils
import Utils.ToastUtil
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.example.easychinese.R
import com.xuexiang.xui.XUI
import com.xuexiang.xui.widget.guidview.GuideCaseQueue
import com.xuexiang.xui.widget.guidview.GuideCaseView
import com.xuexiang.xui.widget.popupwindow.ViewTooltip
import com.xuexiang.xui.widget.toast.XToast
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAccountActivity : AppCompatActivity() {
    companion object{
        var veri=""
        var finalemail=""
        @SuppressLint("StaticFieldLeak")
        lateinit var hidedtext:TextView
        val uid="16381696068329008093734006792"
        var fdbkuid=""
        var pswd=""
    }
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_create_account)
        hidedtext=findViewById(R.id.afteddsaad)
//        showTextGuideView()
        findViewById<TextView>(R.id.gotologin).setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.parentlayout).setOnClickListener {
            cancelKeyboard()
        }
        findViewById<Button>(R.id.register).setOnClickListener {
            var email=findViewById<EditText>(R.id.ed235).text.toString()
            var pwd1=findViewById<EditText>(R.id.ed236).text.toString()
            var pwd2=findViewById<EditText>(R.id.ed237).text.toString()
            val verificationCode=findViewById<EditText>(R.id.ed556).text.toString()
if (veri.equals(verificationCode)){
createaccount(finalemail,verificationCode,pswd)
}else{
    XToastUtils.error("The verification code is wrong")
}
        }
        findViewById<Button>(R.id.vinlogin4).setOnClickListener {
            val email=findViewById<EditText>(R.id.ed235).text.toString()
            val pwd1=findViewById<EditText>(R.id.ed236).text.toString()
            val pwd2=findViewById<EditText>(R.id.ed237).text.toString()
            val v=findViewById<EditText>(R.id.ed556).text.toString()
            if (email==""||pwd1==""||pwd2==""){
                shakeEmpty()
                XToastUtils.error("Please enter your email and password before get verification code.")
            }else if (pwd1!=pwd2){
                XToastUtils.error("Two passwords are not the same.")
                findViewById<LinearLayout>(R.id.pwd02).startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity,R.anim.left_right))
            }else if (pwd1.length<=8){
                findViewById<LinearLayout>(R.id.pwd01).startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity,R.anim.left_right))
                XToastUtils.error("The length of your password should be longer than 9 characters")
            }else{
//                findViewById<LinearLayout>(R.id.invisiblelayout).visibility=View.VISIBLE
                finalemail=email
                pswd=pwd1
                getVerificationCode(this,email,pwd1)
//findViewById<Button>(R.id.vinlogin4).visibility=View.GONE
//                findViewById<TextView>(R.id.afteddsaad).visibility=View.VISIBLE
            }
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
    private fun showTextGuideView() {
        val guideStep1 = GuideCaseView.Builder(this@CreateAccountActivity)
            .title("Your password should contain both characters and digits,and the minimum length is 9.")
            .titleStyle(0, Gravity.BOTTOM)
            .focusOn(findViewById(R.id.ed236))
            .focusRectAtPosition(600, 850, 800, 140)
            .roundRectRadius(60)
            .build()
        val guideStep2 = GuideCaseView.Builder(this@CreateAccountActivity)
            .title("Click here,and the verification code will be sent to your email address.")
            .focusOn(findViewById(R.id.vinlogin4))
            .titleStyle(0, Gravity.BOTTOM)
            .focusRectAtPosition(810, 1180, 300, 140)
            .roundRectRadius(60)
            .build()
        val guideStep3 = GuideCaseView.Builder(this@CreateAccountActivity)
            .title("If you already have registered successfully,please click here to login.")
            .focusOn(findViewById(R.id.gotologin))
            .titleStyle(0, Gravity.BOTTOM)
            .focusRectAtPosition(530, 1540, 300, 90)
            .roundRectRadius(60)
            .build()

        GuideCaseQueue()
            .add(guideStep1)
            .add(guideStep2)
            .add(guideStep3)
            .show()
    }
    fun shakeEmpty(){
        val email=findViewById<EditText>(R.id.ed235).text.toString()
        val pwd1=findViewById<EditText>(R.id.ed236).text.toString()
        val pwd2=findViewById<EditText>(R.id.ed237).text.toString()
        if (email== "")findViewById<LinearLayout>(R.id.email).startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity,R.anim.left_right))

        if (pwd1== "")findViewById<LinearLayout>(R.id.pwd01).startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity,R.anim.left_right))
        if (pwd2== "")findViewById<LinearLayout>(R.id.pwd02).startAnimation(AnimationUtils.loadAnimation(this@CreateAccountActivity,R.anim.left_right))

    }


    fun createaccount( email:String, vcode:String,pwd:String){
        LoginActivity.publicretrofit.create(AnnouncementService::class.java)
            .getCheck(email,vcode)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    XToastUtils.error("Unable to verify:"+t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    var fdbk = response.body()?.string()
                    if (fdbk != null) {
                        if (fdbk.equals("\"100\"")){
                            //验证码不匹配
                            XToastUtils.error("Sorry,your verification is wrong")
                        }else if(fdbk.equals("\"101\"")){
                            //此邮箱未请求注册
                            XToastUtils.error("The email address had never asked for a verificaton code.")
                        }else if (fdbk.length>6){
                            val jso=JSONObject(fdbk)
                            val uid=jso.getString("Uid")
                            val token=jso.getString("Token")
                            val edi=this@CreateAccountActivity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit()
                            edi.putString("uid",uid)
                            edi.putString("email", finalemail)
                            edi.putString("token",token)
                            edi.putString("password",pwd)
                            edi.apply()
                            fdbkuid=fdbk.substring(1,fdbk.length-1)
                            XToastUtils.success("Account is created successfully!")
//                            this@CreateAccountActivity.startActivity(Intent(this@CreateAccountActivity,ReciteActivity::class.java))
                            this@CreateAccountActivity.startActivity(Intent(this@CreateAccountActivity,CreateAccountActivity2::class.java))

                        }else{
                            XToastUtils.error("Unknown error: ${fdbk}")
                        }
                    } else {
                        XToastUtils.error("Empty Feedback")
                    }
                }

            })
    }
    fun cancelKeyboard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // 隐藏软键盘
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)

    }
}