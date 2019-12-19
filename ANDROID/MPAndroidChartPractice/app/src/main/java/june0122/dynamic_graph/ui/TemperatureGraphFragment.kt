package june0122.dynamic_graph.ui

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
import june0122.dynamic_graph.R
import june0122.dynamic_graph.util.CelsiusValueFormatter
import kotlinx.android.synthetic.main.fragment_graph_a.*

class TemperatureGraphFragment : Fragment() {
    private var maxValue: Float? = 0f
    private var minValue: Float? = 0f

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_graph_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChart(setDataList(), "온도 (℃)")
    }

    private fun setDataList(): ArrayList<Entry> {
        val dataList = ArrayList<Entry>()

        for (i in 0..50 step 2) {
            val temperatureValue = Math.random() * 40
            dataList.add(Entry(i.toFloat(), temperatureValue.toFloat()))
        }

        return dataList
    }

    private fun setDataSet(dataList: ArrayList<Entry>, label: String): LineDataSet {
        val lineDataSet = LineDataSet(dataList, label)
        val celsiusValueFormatter = CelsiusValueFormatter()

        maxValue = dataList.maxBy { it.y }?.y
        minValue = dataList.minBy { it.y }?.y

        lineDataSet.apply {
            setDrawValues(false)
            setDrawCircles(false)
            setDrawHighlightIndicators(false)

            color = ContextCompat.getColor(lineChart.context, R.color.denimBlue)
            circleHoleColor = ContextCompat.getColor(lineChart.context, R.color.denimBlue)
            setCircleColor(ContextCompat.getColor(lineChart.context, R.color.denimBlue))
            lineWidth = 2f
            circleRadius = 4f
            valueFormatter = celsiusValueFormatter
        }

        return lineDataSet
    }


    private fun setChart(dataList: ArrayList<Entry>, label: String) {

        lineChart.apply {
            invalidate()
            setScaleEnabled(false)
            animateX(4000, Easing.EaseInOutQuart)

            data = LineData(setDataSet(dataList, label))

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

            axisLeft.apply {
                val upperLimitLine = LimitLine(35f, "max : $maxValue")
                        .apply {
                            lineWidth = 2f
                            lineColor = ContextCompat.getColor(lineChart.context, R.color.pale_gray)
                            enableDashedLine(10f, 15f, 0f)
                            labelPosition = LimitLine.LimitLabelPosition.LEFT_BOTTOM
                            textColor = ContextCompat.getColor(lineChart.context, R.color.gray)
                        }

                val lowerLimitLine = LimitLine(5f, "min : $minValue")
                        .apply {
                            lineWidth = 2f
                            lineColor = ContextCompat.getColor(lineChart.context, R.color.pale_gray)
                            enableDashedLine(10f, 15f, 0f)
                            labelPosition = LimitLine.LimitLabelPosition.LEFT_BOTTOM
                            textColor = ContextCompat.getColor(lineChart.context, R.color.gray)
                        }

                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
                addLimitLine(upperLimitLine)
                addLimitLine(lowerLimitLine)
                setDrawLimitLinesBehindData(true)
                textColor = context?.let { ContextCompat.getColor(it, R.color.gray) } ?: 0
            }

            axisRight.apply {
                isEnabled = false
                setDrawLabels(false)
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }

            description.apply {
                isEnabled = true
                text = label
            }

            legend.apply {
                isEnabled = false
                textSize = 15f
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                orientation = Legend.LegendOrientation.HORIZONTAL
            }
        }
    }
}