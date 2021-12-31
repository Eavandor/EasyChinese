package LoginAndCreateAccount

import Services.AnnouncementService
import TestWord.TestingWordActivity
import TestWord.XToastUtils
import android.content.Context
import android.content.Intent
import android.widget.Toast
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getVerificationCode(conte:Context,email:String,pwd:String){
    LoginActivity.publicretrofit.create(AnnouncementService::class.java)
        .sendInformationUpdate(email,pwd)
        .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                XToastUtils.error("Unable to send verification code:"+t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                var fdbk = response.body()?.string()
                if (fdbk != null) {
                    CreateAccountActivity.veri=fdbk.substring(1,5)
                    CreateAccountActivity.hidedtext.text=fdbk.substring(1,5)
                    if (fdbk.length==6){
                        XToastUtils.success("The verification code has been sent to your email address.")
                    }
//                    XToastUtils.success("The verification code have been sent to your email address."+CreateAccountActivity.veri)
                } else {
                    XToastUtils.error("Empty Feedback")
                }
            }

        })
}
fun verify(conte:Context,email:String,vcode:String){
    LoginActivity.publicretrofit.create(AnnouncementService::class.java)
        .sendInformationUpdate(email,vcode)
        .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                XToastUtils.error("Unable to send verification code:"+t.message)
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                var fdbk = response.body()?.string()
                if (fdbk != null) {
                    CreateAccountActivity.veri=fdbk
                    XToastUtils.success("The verification code have been sent to your email address."+CreateAccountActivity.veri)
                } else {
                    XToastUtils.error("Empty Feedback")
                }
            }

        })
}

class SendNetworkRequest {

}