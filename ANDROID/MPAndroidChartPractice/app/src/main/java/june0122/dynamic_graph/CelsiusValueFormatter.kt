package june0122.dynamic_graph

import com.github.mikephil.charting.formatter.ValueFormatter

class CelsiusValueFormatter : ValueFormatter() {

    private val mFormatter = java.text.DecimalFormat("###,###,##0.0" + " ℃")

    override fun getFormattedValue(value: Float): String {
        return mFormatter.format(value)
    }

}