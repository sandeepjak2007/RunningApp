package code.sandeep.runningapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import code.sandeep.runningapp.R
import code.sandeep.runningapp.db.Run
import code.sandeep.runningapp.other.TrackingUtility
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(view: View) : RecyclerView.ViewHolder(view)

    val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_run, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(findViewById(R.id.ivRunImage))
            val calender = Calendar.getInstance().apply {
                timeInMillis = run.timeStamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            findViewById<TextView>(R.id.tvDate).text = dateFormat.format(calender.time)
            findViewById<TextView>(R.id.tvAvgSpeed).text = buildString {
                append(run.avgSpeedInKMPH)
                append("km/h")
            }
            findViewById<TextView>(R.id.tvDistance).text = buildString {
                append(run.distanceInMeters / 1000f)
                append("km")
            }
            findViewById<TextView>(R.id.tvTime).text = buildString {
                append(TrackingUtility.getFormattedStopWatchTime(run.timeInMillis))
            }
            findViewById<TextView>(R.id.tvCalories).text = buildString {
                append(run.caloriesBurned)
                append("kcal")
            }
        }
    }
}