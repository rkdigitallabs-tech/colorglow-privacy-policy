package com.allsoundspro.domain.model

/**
 * Domain model representing a sound item
 */
data class Sound(
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val category: String = "",
    val duration: Int = 0, // Duration in seconds
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
