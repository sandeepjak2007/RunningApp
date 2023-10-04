package code.sandeep.runningapp.other

import android.content.Context
import android.view.View
import code.sandeep.runningapp.databinding.MarkerViewBinding
import code.sandeep.runningapp.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
    private val runs: List<Run>, private val c: Context, private val layoutId: Int
) : MarkerView(c, layoutId) {
    private lateinit var binding: MarkerViewBinding

    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }


    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        binding = MarkerViewBinding.bind(View.inflate(c, layoutId, null))
        super.refreshContent(e, highlight)
        if (e == null) {
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]

        val calender = Calendar.getInstance().apply {
            timeInMillis = run.timeStamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(calender.time)
        binding.tvAvgSpeed.text = buildString {
            append(run.avgSpeedInKMPH)
            append("km/h")
        }
        binding.tvDistance.text = buildString {
            append(run.distanceInMeters / 1000f)
            append("km")
        }
        binding.tvDuration.text = buildString {
            append(TrackingUtility.getFormattedStopWatchTime(run.timeInMillis))
        }
        binding.tvCaloriesBurned.text = buildString {
            append(run.caloriesBurned)
            append("kcal")
        }

    }
}