package edu.cs395.finalProj.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs395.finalProj.glide.Glide
import edu.cs395.finalProj.ExcerciseVid
import edu.cs395.finalProj.R
import edu.cs395.finalProj.api.VideoYtModel
import edu.cs395.finalProj.databinding.FragmentAddExerciseBinding
import edu.cs395.finalProj.databinding.RowEventBinding
import edu.cs395.finalProj.databinding.RowExerciseBinding
import edu.cs395.finalProj.glide.Glide.glideFetch
import edu.cs395.finalProj.model.Event
import edu.cs395.finalProj.model.Video
import edu.cs395.finalProj.ui.EditEventFragment
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

    inner class VH(val rowExerciseBinding: RowExerciseBinding)
        : RecyclerView.ViewHolder(rowExerciseBinding.root){
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowExerciseBinding = RowExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(rowExerciseBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowExerciseBinding
        val video = viewModel.getVideos(position)
        val title = video.getName()
        val url = video.getThumbnail()
        binding.videoNameTV.text = title
        Glide.glideFetch(video.getThumbnail(), video.getThumbnail(), binding.tnIV)
    }


}