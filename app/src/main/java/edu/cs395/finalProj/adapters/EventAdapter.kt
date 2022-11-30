package edu.cs395.finalProj.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs395.finalProj.ExcerciseVid
import edu.cs395.finalProj.R
import edu.cs395.finalProj.databinding.RowEventBinding
import edu.cs395.finalProj.model.Event
import edu.cs395.finalProj.ui.AddEventFragment
import edu.cs395.finalProj.ui.EditEventFragment
import edu.cs395.finalProj.ui.MainViewModel
import edu.cs395.finalProj.ui.WeekViewFragment

/**
 * Created by witchel on 8/25/2019
 */

// https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
// Slick adapter that provides submitList, so you don't worry about how to update
// the list, you just submit a new one when you want to change the list and the
// Diff class computes the smallest set of changes that need to happen.
// NB: Both the old and new lists must both be in memory at the same time.
// So you can copy the old list, change it into a new list, then submit the new list.
//
// You can call adapterPosition to get the index of the selected item
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
            rowPostBinding.root.setOnClickListener {
            }
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
                // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
    }
}

