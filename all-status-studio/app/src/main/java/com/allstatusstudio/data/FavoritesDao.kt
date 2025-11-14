package com.allstatusstudio.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.allstatusstudio.data.models.StatusModel

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): LiveData<List<StatusModel>>

    @Query("SELECT * FROM favorites WHERE isFavorite = 1")
    fun getFavorites(): LiveData<List<StatusModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: StatusModel)

    @Delete
    suspend fun delete(status: StatusModel)

    @Query("UPDATE favorites SET isFavorite = :isFavorite WHERE path = :path")
    suspend fun updateFavorite(path: String, isFavorite: Boolean)

    @Query("DELETE FROM favorites WHERE path = :path")
    suspend fun deleteByPath(path: String)
}
