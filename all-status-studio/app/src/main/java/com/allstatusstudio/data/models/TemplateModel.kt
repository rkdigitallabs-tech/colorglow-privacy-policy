package com.allstatusstudio.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "templates")
data class TemplateModel(
    @PrimaryKey
    val id: Int,
    val type: String,
    val category: String,
    val path: String,
    val thumbnailPath: String = path,
    val isPremium: Boolean = false
)
