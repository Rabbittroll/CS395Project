package edu.cs395.finalProj.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs395.finalProj.databinding.RowDayBinding
import edu.cs395.finalProj.ui.MainViewModel
import java.time.LocalDate

// NB: Could probably unify with PostRowAdapter if we had two
// different VH and override getItemViewType
// https://medium.com/@droidbyme/android-recyclerview-with-multiple-view-type-multiple-view-holder-af798458763b
class WeekViewAdapter(private val viewModel: MainViewModel)
    : ListAdapter<LocalDate, WeekViewAdapter.VH>(WeekDiff()) {

    class WeekDiff : DiffUtil.ItemCallback<LocalDate>() {
        override fun areItemsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: LocalDate, newItem: LocalDate): Boolean {
            return false

        }
    }

    // ViewHolder pattern
    inner class VH(val rowDayBinding: RowDayBinding)
        : RecyclerView.ViewHolder(rowDayBinding.root){
            init {
                rowDayBinding.root.setOnClickListener {
                    Log.d(null, "here")
                    //viewModel.netPosts()
                }

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val rowBinding = RowDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(rowBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val binding = holder.rowDayBinding
        val day = viewModel.getDay(position)
        val selDate = viewModel.getSelDate()
        binding.dayTitle.text = day.dayOfMonth.toString()
        if (day == selDate){
            binding.root.setBackgroundColor(Color.DKGRAY)
            binding.dayTitle.setTextColor(Color.BLACK)
        } else {
            binding.root.setBackgroundColor(Color.TRANSPARENT)
            binding.dayTitle.setTextColor(Color.BLACK)
        }
        binding.root.setOnClickListener {
            viewModel.setSelDate(day)
        }

    }

    // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // fragmentActivity.supportFragmentManager.popBackStack()
    }

    //EEE // XXX Write me

