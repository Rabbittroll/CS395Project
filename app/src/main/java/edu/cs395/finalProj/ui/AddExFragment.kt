package edu.cs395.finalProj.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.cs395.finalProj.MainActivity
import edu.cs395.finalProj.R
import edu.cs395.finalProj.adapters.EventAdapter
import edu.cs395.finalProj.adapters.VideoAdapter
import edu.cs395.finalProj.adapters.WeekViewAdapter
import edu.cs395.finalProj.databinding.FragmentAddEventBinding
import edu.cs395.finalProj.databinding.FragmentAddExerciseBinding
import edu.cs395.finalProj.model.Video

import java.time.LocalDate


class AddExFragment : Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentAddExerciseBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter : VideoAdapter
    //lateinit var eventAdapter : EventAdapter

    companion object {
        fun newInstance(): AddExFragment {
            return AddExFragment()
        }
    }

    private fun setDisplayHomeAsUpEnabled(value : Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

    private fun String.capitalizeWords(): String = split(" ").map { it.toLowerCase().capitalize() }.joinToString(" ")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExerciseBinding.inflate(inflater, container, false)
        Log.d(null, viewModel.getAllEx().toString())
        val exList = viewModel.getAllEx()
        adapter = VideoAdapter(viewModel)
        val exLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.videoListView.adapter = adapter
        binding.videoListView.layoutManager = exLayoutManager

        setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    // XXX Write me, onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")

        viewModel.observeVids().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        binding.submitBut.setOnClickListener {
            val selVid = viewModel.getSelVid()
            val name = binding.exerciseNameET.text.toString()
            if (!name.isNullOrEmpty()) {
                if (selVid != null) {
                    if (!viewModel.checkEx(name.capitalizeWords())) {
                        viewModel.pushVid(name.capitalizeWords(), selVid!!.getId())
                        viewModel.clearSelVid()
                        viewModel.fetchExUrl()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }

        }
        binding.searchBut.setOnClickListener {
            var searchTerm = binding.searchET.text.toString()
            if(!searchTerm.isNullOrEmpty()) {
                viewModel.getVideoList(searchTerm)
            } else {
                searchTerm = binding.exerciseNameET.text.toString()
                if (!searchTerm.isNullOrEmpty()) {
                    viewModel.getVideoList(searchTerm)
                }
            }

        }

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