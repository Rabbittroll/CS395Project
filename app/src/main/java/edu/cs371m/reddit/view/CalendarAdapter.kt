package edu.cs371m.reddit.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.databinding.RowBinding
import edu.cs371m.reddit.model.Calendar


class CalendarAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Calendar, CalendarAdapter.VH>(Diff()) {
    // This class allows the adapter to compute what has changed
    class Diff : DiffUtil.ItemCallback<Calendar>() {
        override fun areItemsTheSame(oldItem: Calendar, newItem: Calendar): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
        }

        override fun areContentsTheSame(oldItem: Calendar, newItem: Calendar): Boolean {
            return oldItem.firestoreID == newItem.firestoreID
                    && oldItem.Name == newItem.Name
                    && oldItem.Role == newItem.Role
                    && oldItem.Trainer == newItem.Trainer
                    && oldItem.timeStamp == newItem.timeStamp
        }
    }

    inner class VH(private val rowBinding: RowBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        fun bind(holder: VH, position: Int) {
            val cal = viewModel.getCalendar(position)
            //viewModel.glideFetch(photoMeta.uuid, rowBinding.rowImageView)
            holder.rowBinding.userName.text = cal.Name
            holder.rowBinding.userRole.text = cal.Role
            holder.rowBinding.userTrainer.text = cal.Trainer
            //holder.rowBinding.rowSize.text = photoMeta.byteSize.toString()
            // Note to future me: It might be fun to display the date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(holder, position)
    }
}