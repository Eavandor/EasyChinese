package Utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ToastUtil {
    fun pop(c:String,cont: Context){
        Toast.makeText(
            cont,
            c,
            Toast.LENGTH_SHORT
        )
            .show();
    }
    fun popW(title:String,content:String,cont:Context){
        AlertDialog.Builder(cont).apply {
            setTitle(title)
            setMessage(content)
            setCancelable(false)
            setPositiveButton("OK") { dialog, which ->

            }
            setNegativeButton("Go Back") { dialog,
                                           which ->
            }
            show()

        }
    }

}