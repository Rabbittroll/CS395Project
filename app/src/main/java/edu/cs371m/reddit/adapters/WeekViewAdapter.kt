package edu.cs371m.reddit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.databinding.RowSubredditBinding
import edu.cs371m.reddit.glide.Glide
import edu.cs371m.reddit.model.Calendar
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.model.Event
import edu.cs371m.reddit.ui.WeekViewFragment

// NB: Could probably unify with PostRowAdapter if we had two
// different VH and override getItemViewType
// https://medium.com/@droidbyme/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
class WeekViewAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Calendar, WeekViewAdapter.VH>(WeekDiff()) {

    class WeekDiff : DiffUtil.ItemCallback<Calendar>() {
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

    // ViewHolder pattern
    inner class VH(val rowSubredditBinding: RowSubredditBinding)
        : RecyclerView.ViewHolder(rowSubredditBinding.root){
            init {
                rowSubredditBinding.root.setOnClickListener {
                    Log.d(null, "here")
                    //viewModel.netPosts()
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowSubredditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowSubredditBinding
    }

    // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // fragmentActivity.supportFragmentManager.popBackStack()
    }

    //EEE // XXX Write me

