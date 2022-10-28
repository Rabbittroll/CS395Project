package edu.cs371m.reddit.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.viewModels
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.cs371m.reddit.MainActivity
import edu.cs371m.reddit.databinding.FragmentRvBinding

class Favorites: Fragment() {
    // XXX initialize viewModel
    private var _binding: FragmentRvBinding? = null
    private val viewModel :MainViewModel by activityViewModels()
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter : PostRowAdapter

    companion object {
        fun newInstance(): Favorites {
            return Favorites()
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
        _binding = FragmentRvBinding.inflate(inflater)
        adapter = PostRowAdapter(viewModel)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(javaClass.simpleName, "onViewCreated")
        setDisplayHomeAsUpEnabled(true)
        // XXX Write me
        // Setting itemAnimator = null on your recycler view might get rid of an annoying
        // flicker
        viewModel.observeFavs().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        viewModel.startFavs()
        // Add to menu
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Menu is already inflated by main activity
            }
            // XXX Write me, onMenuItemSelected
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Log.d(null, "in menu item sel")
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
        // XXX Write me
        // Don't let back to home button stay when we exit favorites
        super.onDestroyView()
    }
}