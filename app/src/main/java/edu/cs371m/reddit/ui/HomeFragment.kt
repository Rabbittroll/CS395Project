package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.cs371m.reddit.databinding.FragmentHomeBinding
import edu.cs371m.reddit.ui.calendars.CalendarListAdapter
import edu.cs371m.reddit.view.CalendarAdapter


// XXX Write most of this file
class HomeFragment: Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter : ListRowAdapter

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    // Set up the adapter
    private fun initAdapter(binding: FragmentHomeBinding): ListRowAdapter {
        val adapter = ListRowAdapter(viewModel)
        binding.calendarsRV.adapter = adapter
        return adapter
    }

    /*private fun notifyWhenFragmentForegrounded(postRowAdapter: PostRowAdapter) {
        // When we return to our fragment, notifyDataSetChanged
        // to pick up modifications to the favorites list.  Maybe do more.
        viewModel.setHomeFrag(true)
        initSwipeLayout(binding.swipeRefreshLayout)
        viewModel.setTitleToSubreddit()
        postRowAdapter.notifyDataSetChanged()
    }

    private fun initSwipeLayout(swipe : SwipeRefreshLayout) {
        swipe.setOnRefreshListener {
            viewModel.netPosts()
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        //Log.d(null, "on create view")
        //viewModel.setHomeFrag(true)
        adapter = initAdapter(binding)
        val layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.calendarsRV.layoutManager = layoutManager
        binding.calendarsRV.adapter = adapter
        //initSwipeLayout(binding.swipeRefreshLayout)
        viewModel.fetchDone.observe(viewLifecycleOwner) {
            //binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        _binding = FragmentHomeBinding.bind(view)
        val adapter = CalendarAdapter(viewModel)
        val rv = binding.calendarsRV
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)
        // XXX Write me
        //val adapter = initAdapter(binding)

        //Log.d(null, "in home fragment")
        //adapter.submitList(viewModel.ge)
        viewModel.observeCals().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 0) {
                //notifyWhenFragmentForegrounded(adapter)
            }
        }

    }
}