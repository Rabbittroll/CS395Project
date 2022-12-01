package edu.cs395.finalProj.ui

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
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import edu.cs395.finalProj.AuthInit
import edu.cs395.finalProj.MainActivity
import edu.cs395.finalProj.databinding.FragmentHomeBinding
import edu.cs395.finalProj.adapters.CalendarListAdapter
import edu.cs395.finalProj.adapters.EventAdapter


// XXX Write most of this file
class HomeFragment: Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter : EventAdapter

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) {
            viewModel.updateUser()
        }


    // Set up the adapter
    private fun initAdapter(binding: FragmentHomeBinding): EventAdapter {
        val adapter = EventAdapter(viewModel)
        binding.calendarsRV.adapter = adapter
        return adapter
    }

    private fun setDisplayHomeAsUpEnabled(value : Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(value)
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
        val adapter = CalendarListAdapter(viewModel)
        val rv = binding.calendarsRV
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        binding.userNameTV.text = viewModel.findName()
        binding.loginBut.setOnClickListener {
            // XXX Write me.
            AuthInit(viewModel, signInLauncher)
            //viewModel.fetchCalendar()
        }
        binding.logoutBut.setOnClickListener {
            // XXX Write me.
            viewModel.signOut()
        }
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)
        // XXX Write me
        //val adapter = initAdapter(binding)

        //Log.d(null, "in home fragment")
        //adapter.submitList(viewModel.ge)
        viewModel.observeEmail().observe(viewLifecycleOwner){
            Log.d(null, "in observe email")
            viewModel.fetchCalendar()
        }
        viewModel.observeCals().observe(viewLifecycleOwner){
            adapter.submitList(it)
            binding.userNameTV.text = viewModel.findName()
        }
        parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 0) {
                setDisplayHomeAsUpEnabled(false)
                //notifyWhenFragmentForegrounded(adapter)
            }
        }
        AuthInit(viewModel, signInLauncher)

    }
}