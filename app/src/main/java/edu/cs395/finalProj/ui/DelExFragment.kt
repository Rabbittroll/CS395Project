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
import edu.cs395.finalProj.adapters.WeekViewAdapter
import edu.cs395.finalProj.databinding.FragmentAddEventBinding
import edu.cs395.finalProj.databinding.FragmentDelExerciseBinding

import java.time.LocalDate


class DelExFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentDelExerciseBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter : ArrayAdapter<String>

    companion object {
        fun newInstance(): DelExFragment {
            return DelExFragment()
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
        _binding = FragmentDelExerciseBinding.inflate(inflater, container, false)
        Log.d(null, viewModel.getAllEx().toString())
        val exList = viewModel.getAllEx()
        val aa = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, exList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.exerciseSP.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                Log.d(null, "pos $position")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d(null, "onNothingSelected")
            }
        }
        // Set Adapter to Spinner
        binding.exerciseSP.adapter = aa
        // Set initial value of spinner to medium
        val initialSpinner = 1
        val calName = viewModel.getCalName().capitalize()
        val date = viewModel.getSelDate().toString()
        binding.exerciseSP.setSelection(initialSpinner)
        binding.submitBut.setOnClickListener {
            viewModel.delEx(binding.exerciseSP.selectedItem.toString())
            //viewModel.setSelDate(viewModel.getSelDate())
            requireActivity().supportFragmentManager.popBackStack()
        }
        setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")

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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}