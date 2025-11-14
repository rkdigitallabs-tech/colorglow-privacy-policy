package com.allstatusstudio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allstatusstudio.utils.JsonUtils
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _dailyPack = MutableLiveData<DailyPack>()
    val dailyPack: LiveData<DailyPack> = _dailyPack

    data class DailyPack(
        val caption: String,
        val templateId: Int
    )

    fun loadDailyPack() {
        viewModelScope.launch {
            val packs = JsonUtils.loadDailyPacks(getApplication())
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val pack = packs[today]

            if (pack != null) {
                _dailyPack.value = DailyPack(
                    caption = pack.caption,
                    templateId = pack.templateId
                )
            } else {
                _dailyPack.value = DailyPack(
                    caption = "Welcome to All Status Studio!",
                    templateId = 1
                )
            }
        }
    }
}
