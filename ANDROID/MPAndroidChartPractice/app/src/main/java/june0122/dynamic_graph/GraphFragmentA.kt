package june0122.dynamic_graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_graph_a.*

class GraphFragmentA : Fragment() {
    private val entries: ArrayList<Entry> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        addEntry()
        return inflater.inflate(R.layout.fragment_graph_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChart()
    }

    private fun setChart() {
        val lineData = LineData(createSet())
        val xAxis = lineChart.xAxis
        val yAxisLeft = lineChart.axisLeft
        val yAxisRight = lineChart.axisRight

        val upperLimitLine = LimitLine(35f, "max")
                .apply {
                    lineWidth = 2f
                    lineColor = context?.let { ContextCompat.getColor(it, R.color.pale_gray) } ?: 0
                    enableDashedLine(10f, 15f, 0f)
                    labelPosition = LimitLine.LimitLabelPosition.LEFT_BOTTOM
                    textColor = context?.let { ContextCompat.getColor(it, R.color.gray) } ?: 0
                }

        val lowerLimitLine = LimitLine(5f, "min")
                .apply {
                    lineWidth = 2f
                    lineColor = context?.let { ContextCompat.getColor(it, R.color.pale_gray) } ?: 0
                    enableDashedLine(10f, 15f, 0f)
                    labelPosition = LimitLine.LimitLabelPosition.LEFT_BOTTOM
                    textColor = context?.let { ContextCompat.getColor(it, R.color.gray) } ?: 0
                }

        xAxis.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            textSize = 10f
            granularity = 1f // 기본 단위
            axisMinimum = 0f
            isGranularityEnabled = true
        }


        yAxisLeft.apply {
            axisMinimum = 0f
            setDrawGridLines(false)
            addLimitLine(upperLimitLine)
            addLimitLine(lowerLimitLine)
            setDrawLimitLinesBehindData(true)
            textColor = context?.let { ContextCompat.getColor(it, R.color.gray) } ?: 0
        }

        yAxisRight.apply {
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        lineChart.apply {
            setScaleEnabled(false)
            animateX(4000, Easing.EaseInOutQuart)

            axisRight.isEnabled = false
            axisLeft.axisMaximum = 40f
            description.isEnabled = false

            legend.apply {
                isEnabled = false
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
        }

        lineChart.data = lineData

        lineChart.invalidate()
    }


    private fun addEntry() {
        for (i in 0..50 step 2) {
            val temperatureValue = Math.random() * 40

            entries.add(Entry(i.toFloat(), temperatureValue.toFloat()))
        }

    }

    private fun createSet(): LineDataSet {
        val lineDataSet = LineDataSet(entries, "온도 (℃)")

        val celsiusValueFormatter = CelsiusValueFormatter()

        lineDataSet.apply {
            setDrawValues(false)
            setDrawCircles(false)
            setDrawHighlightIndicators(false)

            color = context?.let { ContextCompat.getColor(it, R.color.denimBlue) }
                    ?: R.color.denimBlue
            circleHoleColor = context?.let { ContextCompat.getColor(it, R.color.denimBlue) }
                    ?: R.color.denimBlue
            setCircleColor(context?.let { ContextCompat.getColor(it, R.color.denimBlue) }
                    ?: R.color.denimBlue)
            lineWidth = 2f
            circleRadius = 4f
            valueFormatter = celsiusValueFormatter
        }
        return lineDataSet
    }
}