package com.allstatusstudio.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.allstatusstudio.databinding.ActivitySchedulerBinding
import com.allstatusstudio.viewmodels.SchedulerViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class SchedulerActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySchedulerBinding
    private lateinit var viewModel: SchedulerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SchedulerViewModel::class.java]

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnScheduleOnce.setOnClickListener {
            showDateTimePicker("once")
        }

        binding.btnScheduleDaily.setOnClickListener {
            showTimePicker("daily")
        }

        binding.btnScheduleWeekly.setOnClickListener {
            showWeeklyPicker()
        }
    }

    private fun showDateTimePicker(type: String) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()

        datePicker.addOnPositiveButtonClickListener { dateMillis ->
            showTimePicker(type, dateMillis)
        }

        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun showTimePicker(type: String, dateMillis: Long? = null) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setTitleText("Select time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            viewModel.schedulePost(type, dateMillis, hour, minute)
        }

        timePicker.show(supportFragmentManager, "TIME_PICKER")
    }

    private fun showWeeklyPicker() {
        val days = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val checkedItems = booleanArrayOf(false, false, false, false, false, false, false)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Select Days")
            .setMultiChoiceItems(days, checkedItems) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("Next") { _, _ ->
                val selectedDays = mutableListOf<Int>()
                checkedItems.forEachIndexed { index, checked ->
                    if (checked) selectedDays.add(index + 2) // Calendar.MONDAY = 2
                }
                showTimePicker("weekly")
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.scheduled.observe(this) { scheduled ->
            // Update UI with scheduled posts
        }
    }
}
