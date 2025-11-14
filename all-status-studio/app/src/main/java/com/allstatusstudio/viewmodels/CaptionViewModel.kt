package com.allstatusstudio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allstatusstudio.data.models.CaptionModel
import com.allstatusstudio.utils.JsonUtils
import kotlinx.coroutines.launch
import kotlin.random.Random

class CaptionViewModel(application: Application) : AndroidViewModel(application) {

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _captions = MutableLiveData<List<String>>()
    val captions: LiveData<List<String>> = _captions

    private val _randomCaption = MutableLiveData<String>()
    val randomCaption: LiveData<String> = _randomCaption

    private var allCaptions = mapOf<String, List<String>>()

    init {
        loadCaptions()
    }

    private fun loadCaptions() {
        viewModelScope.launch {
            allCaptions = JsonUtils.loadCaptions(getApplication())
            _categories.value = allCaptions.keys.toList()
            _captions.value = allCaptions.values.flatten()
        }
    }

    fun filterByCategory(category: String) {
        _captions.value = allCaptions[category] ?: emptyList()
    }

    fun generateRandomCaption() {
        val prefixes = listOf("✨", "💫", "🌟", "⭐", "🔥", "💎")
        val emojis = listOf("😎", "💪", "🎯", "🚀", "💯", "🌈")

        val allCaps = allCaptions.values.flatten()
        if (allCaps.isNotEmpty()) {
            val randomText = allCaps[Random.nextInt(allCaps.size)]
            val prefix = prefixes[Random.nextInt(prefixes.size)]
            val emoji = emojis[Random.nextInt(emojis.size)]

            _randomCaption.value = "$prefix $randomText $emoji"
        }
    }
}
