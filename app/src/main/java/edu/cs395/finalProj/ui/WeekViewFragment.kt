package edu.cs395.finalProj.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
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
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentWeekViewBinding? = null
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

    private fun isLoading(boolean: Boolean){
        binding.PBW.isVisible = boolean
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
        binding.PBW.isVisible = false
        Log.d(javaClass.simpleName, "onViewCreated")
        binding.calNameTV.text = viewModel.getCalName().capitalize()
        binding.addButton.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack("weekViewFrag")
                replace(R.id.main_frame, AddEventFragment.newInstance(), "addEventFrag")
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }
        binding.newButton.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack("weekViewFrag")
                replace(R.id.main_frame, AddExFragment.newInstance(), "addExFrag")
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            }
        }

        binding.delButton.setOnClickListener {
            activity?.supportFragmentManager?.commit {
                addToBackStack("weekViewFrag")
                replace(R.id.main_frame, DelExFragment.newInstance(), "delExFrag")
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
            viewModel.setWeekLoad(true)
            viewModel.setDailyEx()
        }
        viewModel.observeEvents().observe(viewLifecycleOwner){
            eventAdapter.submitList(it)
        }
        viewModel.observeWeekLoad().observe(viewLifecycleOwner){
            isLoading(it)
        }

        viewModel.setDaysInWeek(LocalDate.now())

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Menu is already inflated by main activity
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(menuItem.itemId == android.R.id.home){
                    activity!!.supportFragmentManager.popBackStack()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /*parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 1) {
                viewModel.setSelDate(viewModel.getSelDate())
            }
        }*/
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}