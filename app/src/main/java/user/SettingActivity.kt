package user

import Recite.modifyCpuntry
import Recite.modifyName
import Recite.modifyPassword
import TestWord.XToastUtils
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.easychinese.R

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_setting)
        findViewById<ImageView>(R.id.back).setOnClickListener {
            this.finish()
        }
        findViewById<RelativeLayout>(R.id.helpandfdb).setOnClickListener {
            val intent = Intent()
            val key = "TPrOOV2fQ95tvW0_jj6m_S-OydQxOXDZ"
            intent.data =
                Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D" + key)
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                this.startActivity(intent)

                true
            } catch (e: java.lang.Exception) {
                // 未安装手Q或安装的版本不支持

                false
            }
        }
findViewById<RelativeLayout>(R.id.aboutus).setOnClickListener {
    AlertDialog.Builder(this).apply {
        setTitle("About Us")
        setMessage("    Memory Booster,the team project of team 4, is a powerful app to help Chinese learners to recite Chinese words.We also provide users with test and detailed analysis,and beautiful UI.\n"+
        "Developers: in 4th team:\n"+"Back-end Developer: ReMing Chen\n"+"Android Developer: Yuhan Lin\n"+"UI: XiLin Shen\n"+"Product Manager:Zhen Zhang\nJiaYi Ye\nBoChen Zhen\n"+
        "    Thanks all hardworking members in team 4.")
        setCancelable(false)
        setPositiveButton("OK") { dialog, which ->

        }
        setNegativeButton("Back") { dialog,
                                  which ->
        }
        show()

    }
}
//        findViewById<RelativeLayout>(R.id.viborate).setOnClickListener {
//            val check=this.getSharedPreferences("time_manager",Context.MODE_PRIVATE)
//            val edi=check.edit()
//            val viborate=check.getBoolean("viborate",true)
//            if (viborate){
//                XToastUtils.success("vibration is closed,\nclick again to open")
//                edi.putBoolean("viborate",false)
//            }else{
//                XToastUtils.success("vibration is opened,\nclick again to close")
//                edi.putBoolean("viborate",true)
//            }
//            edi.apply()
//        }

        findViewById<RelativeLayout>(R.id.modifucountry).setOnClickListener {
            //改国家
            buildPopDialog("Modify Country","Please enter your country",2)
        }
        findViewById<RelativeLayout>(R.id.modifypwd).setOnClickListener {
            //改密码
            buildPopDialog("Modify Password","Your new password",1)
        }
        findViewById<RelativeLayout>(R.id.modifunickname).setOnClickListener {
            //改昵称
            buildPopDialog("Modify Nickname","Your new nickname",0)
        }



    }
    fun buildPopDialog(title:String,msg:String,type:Int){//type=0:改昵称，1：改密码，2：改国家
        val view: View = View.inflate(application, R.layout.singleform, null)
        view.findViewById<EditText>(R.id.dialogtext).hint=msg
        if (type==1){
            view.findViewById<EditText>(R.id.dialogtext).inputType=InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@SettingActivity)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setPositiveButton("OK") { dialog, which ->
            val check=this@SettingActivity.getSharedPreferences("userinfo",Context.MODE_PRIVATE)
            val uid=check.getString("uid","")!!
            val token=check.getString("token","")!!
            val content=view.findViewById<EditText>(R.id.dialogtext).text.toString()
            when(type){
                0->{
                    modifyName(uid,token,content)
                val edi=check.edit()
                    edi.putString("nickname",content)
                    edi.apply()
                }
                1->{
                    modifyPassword(uid,token,content)}
                2->{
                    modifyCpuntry(uid,token,content)}
            }
        }
        builder.setNegativeButton("Cancel") { dialog,
                                              which ->
        }
        val dialog: android.app.AlertDialog = builder.create()
        dialog.show()
    }
}