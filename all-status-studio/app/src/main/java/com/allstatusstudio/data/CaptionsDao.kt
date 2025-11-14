package com.allstatusstudio.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.allstatusstudio.data.models.CaptionModel

@Dao
interface CaptionsDao {

    @Query("SELECT * FROM captions ORDER BY id ASC")
    fun getAllCaptions(): LiveData<List<CaptionModel>>

    @Query("SELECT * FROM captions WHERE category = :category")
    fun getCaptionsByCategory(category: String): LiveData<List<CaptionModel>>

    @Query("SELECT * FROM captions WHERE isFavorite = 1")
    fun getFavoriteCaptions(): LiveData<List<CaptionModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(captions: List<CaptionModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(caption: CaptionModel)

    @Update
    suspend fun update(caption: CaptionModel)

    @Delete
    suspend fun delete(caption: CaptionModel)

    @Query("SELECT DISTINCT category FROM captions")
    fun getCategories(): LiveData<List<String>>
}
