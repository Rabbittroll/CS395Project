package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.cs371m.reddit.MainActivity
import edu.cs371m.reddit.R
import edu.cs371m.reddit.adapters.EventAdapter
import edu.cs371m.reddit.adapters.WeekViewAdapter
import edu.cs371m.reddit.databinding.FragmentHomeBinding
import edu.cs371m.reddit.databinding.FragmentWeekViewBinding

import java.time.LocalDate


class WeekViewFragment : Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentWeekViewBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var weekAdapter : WeekViewAdapter
    lateinit var eventAdapter : EventAdapter

    companion object {
        fun newInstance(): WeekViewFragment {
            return WeekViewFragment()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeekViewBinding.inflate(inflater, container, false)
        weekAdapter = WeekViewAdapter(viewModel)
        eventAdapter = EventAdapter(viewModel)
        val weekLayoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
        val eventLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.calendarRecyclerView.layoutManager = weekLayoutManager
        binding.calendarRecyclerView.adapter = weekAdapter
        binding.eventListView.layoutManager = eventLayoutManager
        binding.eventListView.adapter = eventAdapter
        return binding.root
    }

    // XXX Write me, onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        viewModel.observeDays().observe(viewLifecycleOwner){
            weekAdapter.submitList(it)
        }
        viewModel.observeEvents().observe(viewLifecycleOwner){
            eventAdapter.submitList(it)
        }

        viewModel.setDaysInWeek(LocalDate.now())
        viewModel.addEvent("finish", LocalDate.now())
        // XXX Write me

        Log.d(null, "in home fragment")
        //adapter.submitList(viewModel.ge)
        /*viewModel.observeSubs().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }*/
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}