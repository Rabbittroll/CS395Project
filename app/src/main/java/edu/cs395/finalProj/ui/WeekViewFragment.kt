package edu.cs395.finalProj.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.cs395.finalProj.MainActivity
import edu.cs395.finalProj.R
import edu.cs395.finalProj.adapters.EventAdapter
import edu.cs395.finalProj.adapters.WeekViewAdapter
import edu.cs395.finalProj.databinding.FragmentWeekViewBinding

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

    private fun setDisplayHomeAsUpEnabled(value : Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(value)
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
        setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    // XXX Write me, onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        binding.calNameTV.text = viewModel.getCalName().capitalize()
        binding.addButton.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack("weekViewFrag")
                replace(R.id.main_frame, AddEventFragment.newInstance(), "addEventFrag")
                // TRANSIT_FRAGMENT_FADE calls for the Fragment to fade away
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        binding.backButton.setOnClickListener {
            viewModel.changeWeek(-1)
        }
        binding.forwardButton.setOnClickListener {
            viewModel.changeWeek(1)
        }
        viewModel.observeDays().observe(viewLifecycleOwner){
            weekAdapter.submitList(it)
            binding.monthYearTV.text = it[0].month.toString().take(3) + " " + it[0].year.toString()
        }
        viewModel.observeSelDate().observe(viewLifecycleOwner){
            weekAdapter.notifyDataSetChanged()
            viewModel.clearEx()
            viewModel.setDailyEx()
        }
        viewModel.observeEvents().observe(viewLifecycleOwner){
            eventAdapter.submitList(it)
        }

        viewModel.setDaysInWeek(LocalDate.now())

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Menu is already inflated by main activity
            }
            // XXX Write me, onMenuItemSelected
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Log.d(null,menuItem.itemId.toString())
                Log.d(null, android.R.id.home.toString())
                if(menuItem.itemId == android.R.id.home){
                    activity!!.supportFragmentManager.popBackStack()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}