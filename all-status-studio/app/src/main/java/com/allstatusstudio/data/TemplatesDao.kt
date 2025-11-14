package com.allstatusstudio.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.allstatusstudio.data.models.TemplateModel

@Dao
interface TemplatesDao {

    @Query("SELECT * FROM templates ORDER BY id ASC")
    fun getAllTemplates(): LiveData<List<TemplateModel>>

    @Query("SELECT * FROM templates WHERE category = :category")
    fun getTemplatesByCategory(category: String): LiveData<List<TemplateModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(templates: List<TemplateModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(template: TemplateModel)

    @Delete
    suspend fun delete(template: TemplateModel)

    @Query("SELECT DISTINCT category FROM templates")
    fun getCategories(): LiveData<List<String>>
}
