package edu.cs395.finalProj.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.cs395.finalProj.api.VideoYtModel
import edu.cs395.finalProj.databinding.RowVideoBinding
//import edu.cs395.finalProj.glide.Glide
import edu.cs395.finalProj.classes.Video
import edu.cs395.finalProj.ui.MainViewModel

class VideoAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Video, VideoAdapter.VH>(VidDiff()) {

    class VidDiff : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.eventsList == newItem.eventsList
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.getName() == newItem.getName()
                    && oldItem.getId() == newItem.getId()

        }
    }
    private var oldItems = ArrayList<VideoYtModel.VideoItem>()

    inner class VH(val rowExerciseBinding: RowVideoBinding)
        : RecyclerView.ViewHolder(rowExerciseBinding.root){
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowExerciseBinding = RowVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(rowExerciseBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowExerciseBinding
        val video = viewModel.getVideos(position)
        val selVid = viewModel.getSelVid()
        val title = video.getName()
        val url = video.getThumbnail()
        binding.videoNameTV.text = title
        binding.root.setOnClickListener {
            viewModel.setSelVid(video)
            it.setBackgroundColor(Color.LTGRAY)
            this.notifyDataSetChanged()
        }
        if (video == selVid){
            binding.root.setBackgroundColor(Color.LTGRAY)
        } else {
            binding.root.setBackgroundColor(Color.TRANSPARENT)
        }
        Glide.with(binding.root.context)
            .load(video.getThumbnail())
            .into(binding.tnIV)


        //Glide.glideFetch(url, url, binding.tnIV)
        Log.d(null, url)
    }


}