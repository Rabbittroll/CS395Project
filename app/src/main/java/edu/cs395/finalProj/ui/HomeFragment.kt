package edu.cs395.finalProj.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
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

class HomeFragment: Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
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


    private fun initAdapter(binding: FragmentHomeBinding): EventAdapter {
        val adapter = EventAdapter(viewModel)
        binding.calendarsRV.adapter = adapter
        return adapter
    }

    private fun setDisplayHomeAsUpEnabled(value : Boolean) {
        val mainActivity = requireActivity() as MainActivity
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

    private fun isLoading(boolean: Boolean){
        binding.PB.isVisible = boolean
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        adapter = initAdapter(binding)
        val layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        binding.calendarsRV.layoutManager = layoutManager
        binding.calendarsRV.adapter = adapter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.PB.isVisible = false
        Log.d(javaClass.simpleName, "onViewCreated")
        _binding = FragmentHomeBinding.bind(view)
        val adapter = CalendarListAdapter(viewModel)
        val rv = binding.calendarsRV
        val itemDecor = DividerItemDecoration(rv.context, LinearLayoutManager.VERTICAL)
        rv.addItemDecoration(itemDecor)
        binding.userNameTV.text = ""
        binding.loginBut.setOnClickListener {
            AuthInit(viewModel, signInLauncher)
        }
        binding.logoutBut.setOnClickListener {
            viewModel.signOut()
        }
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(rv.context)
        viewModel.observeEmail().observe(viewLifecycleOwner){
            Log.d(null, "in observe email")
            viewModel.fetchCalendar()
            Toast.makeText(this.context,"Welcome to your CalendR ${viewModel.getUser()}",
                Toast.LENGTH_LONG).show()

        }
        viewModel.observeCals().observe(viewLifecycleOwner){
            adapter.submitList(it)
            binding.userNameTV.text = viewModel.findName()
            viewModel.setHomeLoad(false)
        }
        viewModel.observeHomeLoad().observe(viewLifecycleOwner){
            isLoading(it)
        }
        parentFragmentManager.addOnBackStackChangedListener {
            if (parentFragmentManager.backStackEntryCount == 0) {
                setDisplayHomeAsUpEnabled(false)
            }
        }
        AuthInit(viewModel, signInLauncher)

    }
}