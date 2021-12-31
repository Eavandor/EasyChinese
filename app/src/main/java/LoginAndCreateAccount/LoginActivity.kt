package LoginAndCreateAccount

import Recite.ReciteActivity
import Services.AnnouncementService
import TestWord.XToastUtils
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.easychinese.R
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {
    companion object {
        val client: OkHttpClient = OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS)
            .build()   //设置各种超时都是5秒钟

        var publicretrofit =
            Retrofit.Builder().baseUrl("http://47.99.163.120:7070/").client(
                client
            ).build()

        var email = ""
        var uid = "16364691107708623952271722022"
        var nickname = "用户名"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        findViewById<TextView>(R.id.gotocreate).setOnClickListener {
            startActivity(Intent(this,CreateAccountActivity::class.java))
        }
        findViewById<Button>(R.id.login).setOnClickListener {
             email=findViewById<EditText>(R.id.ed2).text.toString()
            val password=findViewById<EditText>(R.id.ed3).text.toString()
            shakeEmpty()
            if (email== null|| email.length<1){
                XToastUtils.warning("Please enter your email address")
            }else if (password==null|| password.length<1){
                XToastUtils.warning("Please enter your password")
                pop("",this)
            }else if (password.length<=5){
                XToastUtils.warning("The length of your password should contains at least 6 characters")
            }else{
                //user have filled all information in a correct form, and we can start login.
                login(email,password)
//                startActivity(Intent(this,ReciteActivity::class.java))
            }
        }
        findViewById<TextView>(R.id.help).setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Guidance")
                setMessage("1.If you have already successfully registered for our app,you can enter your email address and your password to login.\n" +
                        "2.If you haven't registered for our app,please click \"Register for a new account 》\" to register for this app.\n" +
                        "3.If you forget your password,please click \"Forget Password?\", and we will send your password to your email mailbox."
                )
                setCancelable(false)
                setPositiveButton("OK") { dialog, which ->

                }
                setNegativeButton("Go Back") { dialog,
                                          which ->
                }
                show()

            }



        }

        findViewById<LinearLayout>(R.id.parentlayout).setOnClickListener {
            cancelKeyboard()
        }
        findViewById<LinearLayout>(R.id.parentlayout2).setOnClickListener {
            cancelKeyboard()
        }
        findViewById<LinearLayout>(R.id.parentlayout3).setOnClickListener {
            cancelKeyboard()
        }

        findViewById<TextView>(R.id.forget).setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle("Forget your password?")
                setMessage("Click \"OK\", and your password will be sent to your email address.")
                setCancelable(false)
                setPositiveButton("OK") { dialog, which ->


                }
                setNegativeButton("Back") { dialog,
                                            which ->
                }
                show()

            }
        }

    }
    fun pop(c:String,cont:Context){
        Toast.makeText(
            cont,
            c,
            Toast.LENGTH_SHORT
        )
            .show();
    }
    override fun onStop() {
        super.onStop()
        this.finish()
    }

    override fun onPause() {
        super.onPause()
        this.finish()
    }



    fun login( email:String, vcode:String){
        LoginActivity.publicretrofit.create(AnnouncementService::class.java)
            .login(email,vcode)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    XToastUtils.error("Unable to verify:"+t.message)
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val fdbk = response.body()?.string()

                    if (fdbk != null) {
                        if (fdbk.contains("log err: wrong password")){
                            //密码错误
                            findViewById<LinearLayout>(R.id.parentlayout2).startAnimation(AnimationUtils.loadAnimation(this@LoginActivity,R.anim.left_right))
//                            findViewById<LinearLayout>(R.id.hjkh2).startAnimation(AnimationUtils.loadAnimation(this@LoginActivity,R.anim.left_right))
                            XToastUtils.error("The password is wrong")
                        }else if (fdbk.contains("log err: not have the user")){
                            //email未注册
                            findViewById<LinearLayout>(R.id.parentlayout2).startAnimation(AnimationUtils.loadAnimation(this@LoginActivity,R.anim.left_right))
//                            findViewById<LinearLayout>(R.id.hjkh).startAnimation(AnimationUtils.loadAnimation(this@LoginActivity,R.anim.left_right))
                            XToastUtils.error("This email address has not been registered.")
                        }else if(fdbk.contains("Token")&&fdbk.contains("Uid")){
                            //登录成功
                                val jso=JSONObject(fdbk)
                            uid=jso.getString("Uid")
                            val token=jso.getString("Token")
                                val edi=this@LoginActivity.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit()
//                                    edi.putString("token",fdbk.substring(1,fdbk.length-1))
                            edi.putString("token",token)
                            edi.putString("uid",uid)
                                        edi.putString("email",email)
                                        edi.apply()
                            this@LoginActivity.startActivity(Intent(this@LoginActivity,ReciteActivity::class.java))
//                            XToastUtils.success("Login successfully! token:\n${fdbk.substring(1,fdbk.length-1)}")
                        }else{
                            XToastUtils.error("Unknown error:${fdbk}")
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

    fun shakeEmpty(){
        val email=findViewById<EditText>(R.id.ed2).text.toString()
        val pwd1=findViewById<EditText>(R.id.ed3).text.toString()
        if (email== "")findViewById<LinearLayout>(R.id.hjkh).startAnimation(AnimationUtils.loadAnimation(this@LoginActivity,R.anim.left_right))
        if (pwd1== "")findViewById<LinearLayout>(R.id.hjkh2).startAnimation(AnimationUtils.loadAnimation(this@LoginActivity,R.anim.left_right))
    }
}