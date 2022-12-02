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
import edu.cs395.finalProj.MainActivity
import edu.cs395.finalProj.databinding.FragmentEditEventBinding
import edu.cs395.finalProj.classes.Event


class EditEventFragment : Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentEditEventBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter : ArrayAdapter<String>
    lateinit var event: Event
    //lateinit var eventAdapter : EventAdapter


    companion object {
        fun newInstance(): EditEventFragment {
            return EditEventFragment()
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
        _binding = FragmentEditEventBinding.inflate(inflater, container, false)
        Log.d(null, viewModel.getAllEx().toString())
        val exList = viewModel.getAllEx()
        val aa = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, exList)
        // Set layout to use when the list of choices appear
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
        event = viewModel.getEditEvent()
        val allEx = viewModel.getAllEx()
        val exName = event.getName()
        val initialSpinner = allEx.indexOf(exName)

        val calName = viewModel.getCalName().capitalize()
        val date = event.getDate().toString()
        binding.exerciseSP.setSelection(initialSpinner)
        binding.calNameTV.text = calName
        binding.dateTV.text = date
        binding.setRepsET.setText(event.getSetRep())
        binding.submitBut.setOnClickListener {
            viewModel.removeEx(calName, date,exName)
            viewModel.pushEx(calName, date,binding.exerciseSP.selectedItem.toString(),binding.setRepsET.text.toString() )
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.removeBut.setOnClickListener {
            viewModel.removeEx(calName, date,exName)
            requireActivity().supportFragmentManager.popBackStack()
        }
        setDisplayHomeAsUpEnabled(true)
        return binding.root
    }

    // XXX Write me, onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")

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