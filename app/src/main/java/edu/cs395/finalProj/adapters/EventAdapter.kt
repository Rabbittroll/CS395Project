package edu.cs395.finalProj.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs395.finalProj.ExcerciseVid
import edu.cs395.finalProj.R
import edu.cs395.finalProj.databinding.RowEventBinding
import edu.cs395.finalProj.classes.Event
import edu.cs395.finalProj.ui.EditEventFragment
import edu.cs395.finalProj.ui.MainViewModel

class EventAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Event, EventAdapter.VH>(ListDiff()) {

    class ListDiff : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.eventsList == newItem.eventsList
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.getName() == newItem.getName()
                    && oldItem.getDate() == newItem.getDate()

        }
    }

    inner class VH(val rowPostBinding: RowEventBinding)
        : RecyclerView.ViewHolder(rowPostBinding.root){
        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowPostBinding
        val event = viewModel.getEvent(position)
        val title = viewModel.getEvent(position).getName()
        val url = viewModel.getEvent(position).getUrl()
        viewModel.setWeekLoad(false)
        binding.eventNameTV.text = viewModel.getEvent(position).getName() + " :"
        binding.eventSetRepTV.text = viewModel.getEvent(position).getSetRep()
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, ExcerciseVid::class.java)
            intent.putExtra("title", title)
            intent.putExtra("url", url)
            startActivity(binding.root.context, intent, null)
        }
        binding.editButton.setOnClickListener {
            viewModel.clearEditEvent()
            viewModel.setEditEvent(event)
            val activity  = it.context as? AppCompatActivity
            activity?.supportFragmentManager?.commit {
                addToBackStack("weekViewFrag")
                replace(R.id.main_frame, EditEventFragment.newInstance(), "editEventFrag")
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
    }
}

