package edu.cs371m.reddit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.databinding.RowBinding
import edu.cs371m.reddit.model.Calendar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import edu.cs371m.reddit.ui.calendars.Calendars


class CalendarListAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Calendar, CalendarListAdapter.VH>(Diff()) {
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
            itemView.setOnClickListener {
                val activity  = it.context as? AppCompatActivity
                activity?.supportFragmentManager?.commit {
                    addToBackStack("homeFrag")
                    add(R.id.main_frame, Calendars.newInstance(), "calendarViewFrag")
                    // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                }
            }

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