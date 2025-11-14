package com.allstatusstudio.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.allstatusstudio.data.models.TemplateModel
import com.allstatusstudio.utils.JsonUtils
import kotlinx.coroutines.launch

class TemplatesViewModel(application: Application) : AndroidViewModel(application) {

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>> = _categories

    private val _templates = MutableLiveData<List<TemplateModel>>()
    val templates: LiveData<List<TemplateModel>> = _templates

    private var allTemplates = listOf<TemplateModel>()

    init {
        loadTemplates()
    }

    private fun loadTemplates() {
        viewModelScope.launch {
            allTemplates = JsonUtils.loadTemplates(getApplication())
            _templates.value = allTemplates

            val cats = allTemplates.map { it.category }.distinct()
            _categories.value = cats
        }
    }

    fun filterByCategory(category: String) {
        _templates.value = allTemplates.filter { it.category == category }
    }
}
