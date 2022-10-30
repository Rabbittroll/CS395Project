package edu.cs371m.reddit.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.reddit.MainActivity
import edu.cs371m.reddit.OnePost
import edu.cs371m.reddit.R
import edu.cs371m.reddit.api.RedditPost
import edu.cs371m.reddit.databinding.RowPostBinding
import edu.cs371m.reddit.databinding.RowSubredditBinding
import edu.cs371m.reddit.glide.Glide
import edu.cs371m.reddit.ui.subreddits.SubredditListAdapter

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
class PostRowAdapter(private val viewModel: MainViewModel)
    : ListAdapter<RedditPost, PostRowAdapter.VH>(RedditDiff()) {
    inner class VH(val rowPostBinding: RowPostBinding)
        : RecyclerView.ViewHolder(rowPostBinding.root){
        init {
            rowPostBinding.root.setOnClickListener {
            }
        }
    }

    class RedditDiff : DiffUtil.ItemCallback<RedditPost>() {
        override fun areItemsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return oldItem.key == newItem.key
        }
        override fun areContentsTheSame(oldItem: RedditPost, newItem: RedditPost): Boolean {
            return RedditPost.spannableStringsEqual(oldItem.title, newItem.title) &&
                    RedditPost.spannableStringsEqual(oldItem.selfText, newItem.selfText) &&
                    RedditPost.spannableStringsEqual(oldItem.publicDescription, newItem.publicDescription) &&
                    RedditPost.spannableStringsEqual(oldItem.displayName, newItem.displayName)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostRowAdapter.VH {
        val rowBinding = RowPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowPostBinding
        val curPost = getItem(position)
        binding.title.text = getItem(position).title
        binding.title.setOnClickListener {
            val intent = Intent(binding.title.context, OnePost::class.java)
            intent.putExtra("title", getItem(position).title.toString())
            intent.putExtra("selfText", getItem(position).selfText.toString())
            intent.putExtra("imageURL",getItem(position).imageURL.toString())
            intent.putExtra("imageURL",getItem(position).thumbnailURL.toString())
            startActivity(binding.title.context, intent, null)
        }
        binding.comments.text = getItem(position).commentCount.toString()
        binding.score.text = getItem(position).score.toString()
        if(!viewModel.getFavs().isNullOrEmpty()){
            if (viewModel.getFavs()!!.contains(getItem(position))){
                binding.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                binding.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
        } else {
            binding.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
        binding.rowFav.setOnClickListener {
            if(!viewModel.getFavs().isNullOrEmpty()){
                if (viewModel.getFavs()!!.contains(curPost)){
                    viewModel.removeFavs(curPost)
                    binding.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                } else {
                    viewModel.setFavs(getItem(position))
                    binding.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
                }
            } else {
                viewModel.setFavs(getItem(position))
                binding.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
            }
        }
        binding.selfText.text = getItem(position).selfText
        Glide.glideFetch(getItem(position).imageURL, getItem(position).thumbnailURL, binding.image)
    }
}

