package user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.easychinese.R
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleEntry


class CandleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candle)
val candle1=findViewById<CandleStickChart>(R.id.candle1)
        val u1=CandleStickChartUtils(candle1)
        val candleEntry: MutableList<CandleEntry> = ArrayList()
        candleEntry.add(CandleEntry(1F, 7F, 3F, 4f, 6f))
        candleEntry.add(CandleEntry(2F, 10F, 5F, 6f, 9f))
        candleEntry.add(CandleEntry(3F, 10F, 2F, 9f, 3f))
        candleEntry.add(CandleEntry(4F, 8F, 2F, 3f, 7f))
        candleEntry.add(CandleEntry(5F, 15F, 6F, 7f, 14f))
        candleEntry.add(CandleEntry(6F, 21F, 13F, 14f, 20f))
        candleEntry.add(CandleEntry(7F, 27F, 18F, 20f, 26f))
        candleEntry.add(CandleEntry(8F, 26F, 8F, 26f, 10f))
        candleEntry.add(CandleEntry(9F, 12F, 2F, 10f, 3f))
        candleEntry.add(CandleEntry(10F, 8F, 2F, 3f, 7f))
        u1.setCandleStickData(candleEntry)
    }
}