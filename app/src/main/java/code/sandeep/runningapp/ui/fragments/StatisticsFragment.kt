package code.sandeep.runningapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import code.sandeep.runningapp.R
import code.sandeep.runningapp.databinding.FragmentStatisticsBinding
import code.sandeep.runningapp.other.CustomMarkerView
import code.sandeep.runningapp.other.TrackingUtility
import code.sandeep.runningapp.ui.viewmodels.StatisticViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticViewModel by viewModels()

    private lateinit var binding: FragmentStatisticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }

    private fun setupBarChart() {
        binding.barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        binding.barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    private fun subscribeToObservers() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner) {
            it?.let {
                val totalTimeRun = TrackingUtility.getFormattedStopWatchTime(it)
                binding.tvTotalTime.text = totalTimeRun
            }
        }
        viewModel.totalDistance.observe(viewLifecycleOwner) {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                binding.tvTotalDistance.text = buildString {
                    append(totalDistance)
                    append("km")
                }
            }
        }
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner) {
            it?.let {
                val avgSpeed = round(it * 10f) / 10f
                binding.tvAverageSpeed.text = buildString {
                    append(avgSpeed)
                    append("km/h")
                }
            }

            viewModel.totalCaloriesBurned.observe(viewLifecycleOwner) { cal ->
                cal?.let {
                    binding.tvTotalCalories.text = buildString {
                        append(it)
                        append("kcal")
                    }
                }
            }
            viewModel.runsSortedByDate.observe(viewLifecycleOwner) { runs ->
                runs?.let { runsList ->
                    val allAvgSpeeds = runsList.indices.map { i ->
                        BarEntry(
                            i.toFloat(),
                            runsList[i].avgSpeedInKMPH
                        )
                    }
                    val barDataSet = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply {
                        valueTextColor = Color.WHITE
                        color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                    }
                    binding.barChart.data = BarData(barDataSet)
                    binding.barChart.marker = CustomMarkerView(
                        runsList.reversed(),
                        requireContext(),
                        R.layout.marker_view

                    )
                    binding.barChart.invalidate()
                }
            }
        }
    }
}