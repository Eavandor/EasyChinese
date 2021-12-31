package TestWord

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.easychinese.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import java.io.File
import java.util.*


class RadarChartActivity : AppCompatActivity() {
    companion object{
       lateinit var chart:com.github.mikephil.charting.charts.RadarChart
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        supportActionBar?.hide()
        setContentView(R.layout.activity_radar_chart)
        val intent=intent
        val speed=intent.getFloatExtra("speed",0f)
        val totalScore=intent.getFloatExtra("totalScore",0f)
        val difficulty=intent.getFloatExtra("difficulty",0f)
        val pinyin=intent.getFloatExtra("pinyin",0f)
        val meaningScore=intent.getFloatExtra("meaning",0f)
//        val speed=60F
//        val totalScore=87F
//        val difficulty=78f
//        val pinyin=50f
//        val meaningScore=90f

chart=findViewById<com.github.mikephil.charting.charts.RadarChart>(R.id.chart1)
        initChartStyle()
        initChartLabel()
        setChartData(5, 100f,totalScore,speed,difficulty,pinyin,meaningScore)
        // 设置雷达图显示的动画
        for (set in chart.data.dataSets) {
            set.setDrawValues(true)
        }
        // 设置雷达图显示的动画
        chart.animateXY(1400, 1400, Easing.EaseInOutQuad)
        findViewById<Button>(R.id.hkjhk).setOnClickListener {
            this.finish()
        }

        findViewById<Button>(R.id.khkj).setOnClickListener {
//            if (chart.saveToGallery(""+System.currentTimeMillis(),70)){
            if (chart.saveToGallery(""+System.currentTimeMillis(),70)){
                XToastUtils.toast("Saved successfully!")
                sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/DCIM/"))
                    )
                )
            }else{
                XToastUtils.toast("Fail to save the chart!")
            }

        }

//        chart.invalidate()
    }
    protected fun initChartStyle() {
        // 设置雷达图的背景颜色
        chart.setBackgroundColor(Color.rgb(60, 65, 82))
        // 禁止图表旋转
        chart.isRotationEnabled = false

        //设置雷达图网格的样式
        chart.description.isEnabled = false
        chart.webLineWidth = 1f
        chart.webColor = Color.LTGRAY
        chart.webLineWidthInner = 1f
        chart.webColorInner = Color.LTGRAY
        chart.webAlpha = 100

        // 设置标识雷达图上各点的数字控件
//        val mv: MarkerView = RadarMarkerView(this@RadarChartActivity, R.layout.marker_view_radar)
//        mv.chartView = chart
//        chart.marker = mv
        initXYAxisStyle()
    }

    private fun initXYAxisStyle() {
        //设置X轴（雷达图的项目点）的样式
        val xAxis = chart.xAxis
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f
        xAxis.valueFormatter = object : ValueFormatter() {
            private val mActivities = arrayOf("Total Score", "Speed", "Difficulty", "Phonetic Transcription", "Translation")
            override fun getFormattedValue(value: Float): String {
                return mActivities[value.toInt() % mActivities.size]
            }
        }
        xAxis.textColor = Color.WHITE

        //设置Y轴（雷达图的分值）的样式
        val yAxis = chart.yAxis
        yAxis.setLabelCount(5, false)
        yAxis.textSize = 9f
        //最小分值
        yAxis.axisMinimum = 0f
        //最大分值
        yAxis.axisMaximum = 80f
        //是否画出分值
        yAxis.setDrawLabels(false)
    }

    /**
     * 初始化图表的 标题 样式
     */
    protected fun initChartLabel() {
        //设置图表数据 标题 的样式
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 5f
        l.textColor = Color.WHITE
    }

    /**
     * 设置图表数据
     *
     * @param count 一组数据的数量
     * @param range
     */
    protected fun setChartData(count: Int, range: Float,total:Float,speed:Float,difficulty:Float,pinyin:Float,meaning:Float) {
        val min = 0f
        val entries1 = ArrayList<RadarEntry>()
//        val entries2 = ArrayList<RadarEntry>()
        //雷达图的数据一般都有最大值，数据在一定范围内
//        for (i in 0 until count) {
//            val val1 = (Math.random() * range).toFloat() + min
//            entries1.add(RadarEntry(val1))
////            val val2 = (Math.random() * range).toFloat() + min
////            entries2.add(RadarEntry(val2))
//        }
        entries1.add(RadarEntry(total))
        entries1.add(RadarEntry(speed))
        entries1.add(RadarEntry(difficulty))
        entries1.add(RadarEntry(pinyin))
        entries1.add(RadarEntry(meaning))

        //设置两组数据的表现样式
        val set1 = RadarDataSet(entries1, "All Data")
        set1.color = Color.rgb(121, 162, 175)
        set1.fillColor = Color.rgb(121, 162, 175)
        set1.setDrawFilled(true)
        set1.fillAlpha = 180
        set1.lineWidth = 2f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)
//        val set2 = RadarDataSet(entries2, "This Week")
//        set2.color = Color.rgb(121, 162, 175)
//        set2.fillColor = Color.rgb(121, 162, 175)
//        set2.setDrawFilled(true)
//        set2.fillAlpha = 180
//        set2.lineWidth = 2f
//        set2.isDrawHighlightCircleEnabled = true
//        set2.setDrawHighlightIndicators(false)

        //最终将两组数据填充进图表中
        val sets = ArrayList<IRadarDataSet>()
        sets.add(set1)
//        sets.add(set2)
        val data = RadarData(sets)
        data.setValueTextSize(8f)
        data.setDrawValues(false)
        data.setValueTextColor(Color.WHITE)
        chart.data = data
        chart.invalidate()
    }
}