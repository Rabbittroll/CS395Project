package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.cs371m.reddit.databinding.FragmentRvBinding


// XXX Write most of this file
class HomeFragment: Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRvBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter : PostRowAdapter

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    // Set up the adapter
    private fun initAdapter(binding: FragmentRvBinding): PostRowAdapter {
        val adapter = PostRowAdapter(viewModel)
        binding.recyclerView.adapter = adapter
        return adapter
    }

    private fun notifyWhenFragmentForegrounded(postRowAdapter: PostRowAdapter) {
        // When we return to our fragment, notifyDataSetChanged
        // to pick up modifications to the favorites list.  Maybe do more.
        Log.d(null, "backstack changed")
        viewModel.setHomeFrag(true)
        //viewModel.setTitle()
        //postRowAdapter.submitList(viewModel.searchPosts())
        viewModel.setTitleToSubreddit()
        postRowAdapter.notifyDataSetChanged()
    }

    private fun initSwipeLayout(swipe : SwipeRefreshLayout) {
        swipe.setOnRefreshListener {
            viewModel.netPosts()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRvBinding.inflate(inflater, container, false)
        //Log.d(null, "on create view")
        viewModel.setHomeFrag(true)
        adapter = initAdapter(binding)
        val layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        initSwipeLayout(binding.swipeRefreshLayout)
        viewModel.fetchDone.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        // XXX Write me
        //val adapter = initAdapter(binding)

        //Log.d(null, "in home fragment")
        //adapter.submitList(viewModel.ge)
        viewModel.observePosts().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 0) {
                notifyWhenFragmentForegrounded(adapter)
            }
        }

    }
}