package com.allstatusstudio.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_posts")
data class SchedulerModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val filePath: String,
    val scheduleType: String, // once, daily, weekly
    val scheduledTime: Long,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
