package edu.cs371m.reddit.ui.calendars

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.firebase.ui.auth.AuthUI.getApplicationContext
import edu.cs371m.reddit.databinding.FragmentWeekViewBinding
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.ui.events.Event

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class Calendars : Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentWeekViewBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    lateinit var adapter : CalendarViewAdapter
    lateinit var selectedDate: LocalDate
    var calendarRecyclerView = binding.calendarRecyclerView
    var monthYearText = binding.monthYearTV
    var eventListView = binding.eventListView

    companion object {
        fun newInstance(): Calendars {
            return Calendars()
        }
    }

    fun formattedDate(date: LocalDate): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return date.format(formatter)
    }

    fun formattedTime(time: LocalTime): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
        return time.format(formatter)
    }

    fun monthYearFromDate(date: LocalDate): String? {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    fun daysInMonthArray(date: LocalDate?): ArrayList<LocalDate?>? {
        val daysInMonthArray: ArrayList<LocalDate?> = ArrayList()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
        val dayOfWeek: Int = firstOfMonth.getDayOfWeek().getValue()
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) daysInMonthArray.add(null) else daysInMonthArray.add(
                LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek)
            )
        }
        return daysInMonthArray
    }

    fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate?>? {
        val days: ArrayList<LocalDate?> = ArrayList()
        var current: LocalDate? = sundayForDate(selectedDate)
        val endDate: LocalDate = current!!.plusWeeks(1)
        while (current!!.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    private fun sundayForDate(current: LocalDate): LocalDate? {
        var current: LocalDate = current
        val oneWeekAgo: LocalDate = current.minusWeeks(1)
        while (current.isAfter(oneWeekAgo)) {
            if (current.getDayOfWeek() === DayOfWeek.SUNDAY) return current
            current = current.minusDays(1)
        }
        return null
    }

    private fun setWeekView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val days = daysInWeekArray(selectedDate)
        val calendarAdapter = CalendarViewAdapter(viewModel, this.requireActivity(), days)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(this.context, 7)
        calendarRecyclerView.setLayoutManager(layoutManager)
        calendarRecyclerView.setAdapter(calendarAdapter)
        setEventAdapater()
    }

    fun previousWeekAction(view: View?) {
        selectedDate = selectedDate.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View?) {
        selectedDate =
           selectedDate.plusWeeks(1)
        setWeekView()
    }

    fun onItemClick(position: Int, date: LocalDate) {
        selectedDate = date
        setWeekView()
    }

   override fun onResume() {
        super.onResume()
        setEventAdapater()
    }

    private fun setEventAdapater() {
        val dailyEvents: ArrayList<Event> =
            Event.eventsForDate(selectedDate)
        val eventAdapter = EventAdapter(this.context, dailyEvents)
        eventListView.setAdapter(eventAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewModel.setTitlePick()
        //viewModel.setHomeFrag(false)
        Log.d(null,"in subreddit")
        //viewModel.netSubreddits()
        _binding = FragmentWeekViewBinding.inflate(inflater, container, false)
        var days = daysInWeekArray(selectedDate)
        adapter = CalendarViewAdapter(viewModel, this.requireActivity(), days)
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = adapter
        binding.monthYearTV.text = monthYearFromDate(selectedDate)
        return binding.root
    }

    // XXX Write me, onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        // XXX Write me

        Log.d(null, "in home fragment")
        //adapter.submitList(viewModel.ge)
        /*viewModel.observeSubs().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }*/
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}