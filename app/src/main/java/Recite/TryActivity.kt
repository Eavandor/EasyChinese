package Recite

import TestWord.XToastUtils
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.easychinese.R
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType


class TryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_try)



        val format = HanyuPinyinOutputFormat()
        format.toneType = HanyuPinyinToneType.WITH_TONE_MARK
        format.vCharType = HanyuPinyinVCharType.WITH_U_UNICODE
        format.caseType = HanyuPinyinCaseType.LOWERCASE
val str=PinyinHelper.toHanYuPinyinString("你好呀!",format," ",false)
        findViewById<TextView>(R.id.kjsga).text=str

    }
}