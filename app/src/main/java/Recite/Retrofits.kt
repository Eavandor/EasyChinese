package Recite

import LoginAndCreateAccount.LoginActivity
import Services.AnnouncementService
import TestWord.XToastUtils
import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.example.easychinese.MainActivity
import com.example.easychinese.ShowerActivity2
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getNewGroup(conte: Context, uid:String,token:String){
    LoginActivity.publicretrofit.create(AnnouncementService::class.java)
        .getNewGroup(uid,token)
        .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                XToastUtils.error("Unable to send verification code:"+t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val fdbk = response.body()?.string()
                if (fdbk != null) {
                    if (fdbk.contains("no group can be return")){
                        XToastUtils.info("Congratulations! You have finished all words.")
                    }else if (fdbk.equals("")){
                        XToastUtils.error("No dictionary is found.")
                    }else{
                        if (fdbk.contains("[")){
                            val jsa=JSONArray(fdbk)
//                            val firstw=jsa.getJSONObject(0).getString("src")
                            ReciteActivity.jso=fdbk
                            ReciteActivity.jso=ReciteActivity.jso.replace(" ","")
                            ReciteActivity.jso=ReciteActivity.jso.replace("\"state\":0","\"state\":1")
                            var i=0
                            ReciteActivity.chineseList.clear()
                            while (i<10){
                                ReciteActivity.chineseList.add(jsa.getString(i))
                                i++
                            }
//                            val intent=Intent(conte,SingleWordActivity::class.java)
//                            intent.putExtra("jso",fdbk)
//                            conte.startActivity(intent)



//                            if (firstw.contains("0")||firstw.contains("1")||firstw.contains("2")||firstw.contains("3")||firstw.contains("4")||firstw.contains("5")||firstw.contains("6")||firstw.contains("7")||firstw.contains("8")||firstw.contains("9")){
//                                val intent= Intent(conte, ShowerActivity2::class.java)
//                                intent.putExtra("fdbk",fdbk)
//                                conte.startActivity(intent)   //这个是错误返回接口的
//                            }else{
                                val intent= Intent(conte, MainActivity::class.java)
                                conte.startActivity(intent)   //这个是前往爬虫接口 的
//                            }
                        }else{
//                            XToastUtils.error("Unknown error:${fdbk}")
                            ReciteActivity.msg.text=fdbk
                        }
                    }
                } else {
                    XToastUtils.error("Empty Feedback")
                }
            }

        })
}

fun modifyName(uid:String,token:String,name:String){
    LoginActivity.publicretrofit.create(AnnouncementService::class.java)
        .modifyName(uid,token,name)
        .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                XToastUtils.error("Unable to modify:"+t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val fdbk = response.body()?.string()
                if (fdbk != null) {
                    XToastUtils.success("返回信息：${fdbk}")

                } else {
                    XToastUtils.error("Empty Feedback")
                }
            }

        })
}
fun modifyCpuntry(uid:String,token:String,country:String){
    LoginActivity.publicretrofit.create(AnnouncementService::class.java)
        .modifyCountry(uid,token,country)
        .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                XToastUtils.error("Unable to modify:"+t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val fdbk = response.body()?.string()
                if (fdbk != null) {
                    XToastUtils.success("返回信息：${fdbk}")

                } else {
                    XToastUtils.error("Empty Feedback")
                }
            }

        })
}
fun modifyPassword(uid:String,token:String,pwd:String){
    LoginActivity.publicretrofit.create(AnnouncementService::class.java)
        .modifyPassword(uid,token,pwd)
        .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                XToastUtils.error("Unable to modify:"+t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val fdbk = response.body()?.string()
                if (fdbk != null) {
                    XToastUtils.success("返回信息：${fdbk}")

                } else {
                    XToastUtils.error("Empty Feedback")
                }
            }

        })
}


fun getUserInfo(uid:String,token:String,cont:Context,titlename:TextView){
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
                    val spf=cont.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit()
                    spf.putString("nickname",nickname)
                    spf.putString("country",country)
                    spf.apply()
                    titlename.text=nickname
                } else {
                    XToastUtils.error("No User information")
                }
            }

        })
}


fun registerUserInfo(uid:String,password:String,cont:Context,nickname:String,country:String){
    LoginActivity.publicretrofit.create(AnnouncementService::class.java)
        .createuserinfo(uid,password,nickname,country)
        .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                XToastUtils.error("Register failed:unable to upload user information:"+t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val fdbk = response.body()?.string()
                if (fdbk != null) {
                    if (fdbk.contains("err to sign")){
                        XToastUtils.error("Fail to upload your user information,and register process is failed.")
                    }else{
                        val userinfo=JSONObject(fdbk)
                        val uuid=userinfo.getString("Uid")
                        val token=userinfo.getString("Token")
                        val spf=cont.getSharedPreferences("userinfo",Context.MODE_PRIVATE).edit()
                        spf.putString("uid",uuid)
                        spf.putString("token",token)
                        spf.apply()
                        cont.startActivity(Intent(cont,ReciteActivity::class.java))
                    }
                } else {
                    XToastUtils.error("Empty feedback.")
                }
            }
        })
}

