package edu.cs395.finalProj.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs395.finalProj.ui.MainViewModel
import edu.cs395.finalProj.databinding.RowBinding
import edu.cs395.finalProj.model.Calendar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import edu.cs395.finalProj.R
import edu.cs395.finalProj.ui.WeekViewFragment


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
                viewModel.setCalName(cal.Name.lowercase())
                val activity  = it.context as? AppCompatActivity
                activity?.supportFragmentManager?.commit {
                    addToBackStack("homeFrag")
                    replace(R.id.main_frame, WeekViewFragment.newInstance(), "weekViewFrag")
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