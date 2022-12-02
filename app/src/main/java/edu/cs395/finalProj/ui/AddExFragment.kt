package edu.cs395.finalProj.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import edu.cs395.finalProj.MainActivity
import edu.cs395.finalProj.adapters.VideoAdapter
import edu.cs395.finalProj.databinding.FragmentAddExerciseBinding


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

    private fun isLoading(boolean: Boolean){
        binding.PBV.isVisible = boolean
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
        binding.videoListView.addItemDecoration(
            DividerItemDecoration(
                this.context,
                exLayoutManager.orientation
            )
        )

        setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    // XXX Write me, onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")

        viewModel.observeVids().observe(viewLifecycleOwner){
            adapter.submitList(it)
            viewModel.setVideoLoad(false)
        }
        binding.submitBut.setOnClickListener {
            val selVid = viewModel.getSelVid()
            val name = binding.exerciseNameET.text.toString()
            viewModel.setVideoLoad(true)
            if (!name.isNullOrEmpty()) {
                if (selVid != null) {
                    if (!viewModel.checkEx(name.capitalizeWords())) {
                        viewModel.pushVid(name.capitalizeWords(), selVid!!.getId())
                        viewModel.clearSelVid()
                        viewModel.fetchExUrl()
                        requireActivity().supportFragmentManager.popBackStack()
                    }else {
                        Toast.makeText(this.context,"Please Enter a New Exercise",
                            Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this.context,"Please Select a Video",Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this.context,"Please Name your Exercise",Toast.LENGTH_LONG).show()
            }

        }
        binding.searchBut.setOnClickListener {
            var searchTerm = binding.searchET.text.toString()
            if(!searchTerm.isNullOrEmpty()) {
                viewModel.setVideoLoad(true)
                viewModel.getVideoList(searchTerm)
            } else {
                searchTerm = binding.exerciseNameET.text.toString()
                if (!searchTerm.isNullOrEmpty()) {
                    viewModel.setVideoLoad(true)
                    viewModel.getVideoList(searchTerm)
                }
            }

        }

        viewModel.observeVideoLoad().observe(viewLifecycleOwner){
            isLoading(it)
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