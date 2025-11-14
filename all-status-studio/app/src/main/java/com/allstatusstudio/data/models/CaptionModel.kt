package com.allstatusstudio.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "captions")
data class CaptionModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val category: String,
    var isFavorite: Boolean = false
)
