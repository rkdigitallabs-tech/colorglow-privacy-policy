package com.allstatusstudio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.allstatusstudio.data.models.SchedulerModel
import com.allstatusstudio.utils.SchedulerUtils

class SchedulerViewModel(application: Application) : AndroidViewModel(application) {

    private val _scheduled = MutableLiveData<List<SchedulerModel>>()
    val scheduled: LiveData<List<SchedulerModel>> = _scheduled

    fun schedulePost(type: String, dateMillis: Long?, hour: Int, minute: Int) {
        when (type) {
            "once" -> {
                dateMillis?.let {
                    SchedulerUtils.scheduleOnce(getApplication(), it, hour, minute)
                }
            }
            "daily" -> {
                SchedulerUtils.scheduleDaily(getApplication(), hour, minute)
            }
            "weekly" -> {
                SchedulerUtils.scheduleWeekly(getApplication(), hour, minute)
            }
        }
        loadScheduledPosts()
    }

    private fun loadScheduledPosts() {
        // Load from database
    }
}
