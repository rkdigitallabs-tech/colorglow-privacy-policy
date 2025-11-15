package com.allsoundspro.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.allsoundspro.domain.model.Sound
import com.allsoundspro.domain.usecase.GetSoundsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSoundsUseCase: GetSoundsUseCase
) : ViewModel() {

    private val _sounds = MutableStateFlow<List<Sound>>(emptyList())
    val sounds: StateFlow<List<Sound>> = _sounds.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSounds()
    }

    private fun loadSounds() {
        viewModelScope.launch {
            _isLoading.value = true
            getSoundsUseCase().collect { soundList ->
                _sounds.value = soundList
                _isLoading.value = false
            }
        }
    }

    fun refreshSounds() {
        loadSounds()
    }
}
