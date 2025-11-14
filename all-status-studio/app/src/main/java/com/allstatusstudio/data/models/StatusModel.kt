package com.allstatusstudio.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class StatusModel(
    @PrimaryKey
    val path: String,
    val name: String,
    val isVideo: Boolean,
    val size: Long,
    val timestamp: Long,
    var isFavorite: Boolean = false,
    var isEdited: Boolean = false,
    var category: String = ""
)
