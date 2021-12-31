package Recite

import LoginAndCreateAccount.LoginActivity
import Services.AnnouncementService
import TestWord.TestingWordActivity
import TestWord.XToastUtils
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.easychinese.MainActivity
import com.example.easychinese.R
import com.example.easychinese.ShowerActivity2
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adapter2(val fruitList: List<Card>) : RecyclerView.Adapter<Adapter2.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fruitImage: ImageView = view.findViewById(R.id.fruitImage)
        val fruitName: TextView = view.findViewById(R.id.fruitName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.iteminme, parent, false)
        val viewHolder = ViewHolder(view)
        val conte=parent.context

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val fruit = fruitList[position]
            getRecited(conte,LoginActivity.uid,fruit.cardId)

//            Toast.makeText(parent.context, "you clicked view ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        viewHolder.fruitImage.setOnClickListener {
            val position = viewHolder.adapterPosition
            val fruit = fruitList[position]
            getRecited(conte,LoginActivity.uid,fruit.cardId)
//            Toast.makeText(parent.context, "you clicked image ${fruit.name}", Toast.LENGTH_SHORT).show()
        }
        return viewHolder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitImage.setImageResource(R.drawable.book1)
        holder.fruitName.text = "Card "+fruit.cardId
    }
    override fun getItemCount() = fruitList.size

    fun getRecited(conte:Context,uid:String,group:String){
        val token=conte.getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("token","")
        val current_dict=conte.getSharedPreferences("userinfo",Context.MODE_PRIVATE).getString("current_dict","")!!
        if (token != null) {
            LoginActivity.publicretrofit.create(AnnouncementService::class.java)
                .getSingleCard(current_dict,group,token)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(
                            conte,
                            "请求失败！！！原因：\n" + t.message,
                            Toast.LENGTH_LONG
                        ).show();
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        var fdbk = response.body()?.string()
                        if (fdbk != null) {

//                            val intent= Intent(conte, SingleWordActivity::class.java)
//                            intent.putExtra("json", fdbk)
//                            conte.startActivity(intent)   //这个是前往旧接口的，非爬虫的
    ReciteActivity.jso=fdbk
    ReciteActivity.isNew=false
ReciteActivity.chineseList.clear()
    val jsa=JSONArray(fdbk)
                            var i=0
                            while (i<10){
                                ReciteActivity.chineseList.add(jsa.getString(i))
                                i++
                            }
//                            if (fdbk.contains("0")||fdbk.contains("1")||fdbk.contains("2")||fdbk.contains("3")||fdbk.contains("4")||fdbk.contains("5")||fdbk.contains("6")||fdbk.contains("7")||fdbk.contains("8")||fdbk.contains("9")){
//                                val intent= Intent(conte, ShowerActivity2::class.java)
//                                intent.putExtra("fdbk",fdbk)
//                                conte.startActivity(intent)   //这个是前往爬虫接口 的
//                            }else{
                                val intent= Intent(conte, MainActivity::class.java)
                                conte.startActivity(intent)   //这个是前往爬虫接口 的
//                            }


                        } else {
                            Toast.makeText(conte, "Empty feedback", Toast.LENGTH_LONG)
                                .show();
                        }
                    }

                })
        }else{
            XToastUtils.error("No token is available.")
        }
    }
}