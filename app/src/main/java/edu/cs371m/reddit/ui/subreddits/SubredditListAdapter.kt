package edu.cs371m.reddit.ui.subreddits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.RowSubredditBinding
import edu.cs371m.reddit.glide.Glide
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.ui.PostRowAdapter

// NB: Could probably unify with PostRowAdapter if we had two
// different VH and override getItemViewType
// https://medium.com/@droidbyme/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
class SubredditListAdapter(private val viewModel: MainViewModel,
                           private val fragmentActivity: FragmentActivity )
    : ListAdapter<RedditPost, SubredditListAdapter.VH>(PostRowAdapter.RedditDiff()) {

    // ViewHolder pattern
    inner class VH(val rowSubredditBinding: RowSubredditBinding)
        : RecyclerView.ViewHolder(rowSubredditBinding.root)

        // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // fragmentActivity.supportFragmentManager.popBackStack()
    }

    //EEE // XXX Write me


}
