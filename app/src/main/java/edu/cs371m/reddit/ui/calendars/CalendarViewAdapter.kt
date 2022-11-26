package edu.cs371m.reddit.ui.calendars

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.RowSubredditBinding
import edu.cs371m.reddit.glide.Glide
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.ui.ListRowAdapter
import java.time.LocalDate

// NB: Could probably unify with PostRowAdapter if we had two
// different VH and override getItemViewType
// https://medium.com/@droidbyme/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
class CalendarViewAdapter(private val viewModel: MainViewModel,
                          private val fragmentActivity: FragmentActivity,
                          private val days: ArrayList<LocalDate?>?)
    : ListAdapter<RedditPost, CalendarViewAdapter.VH>(ListRowAdapter.RedditDiff()) {

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
        binding.subRowDetails.text = getItem(position).publicDescription
        binding.subRowHeading.text = getItem(position).displayName
        binding.subRowHeading.setOnClickListener {
            //viewModel.setSubreddits(getItem(position).displayName.toString())
            //viewModel.setTitle(getItem(position).displayName.toString())
            //viewModel.netPosts()
            fragmentActivity.supportFragmentManager.popBackStack()
        }
        //Log.d(null,"image " + getItem(position).imageURL.isNullOrEmpty().toString())
        //Log.d(null,"thumb " + getItem(position).thumbnailURL.isNullOrEmpty().toString())
        Glide.glideFetch(getItem(position).iconURL, getItem(position).iconURL, binding.subRowPic)
    }

    // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // fragmentActivity.supportFragmentManager.popBackStack()
    }

    //EEE // XXX Write me

