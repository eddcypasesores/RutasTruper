package com.trupercontrolEdwin.app.data.dao

import androidx.room.*
import com.trupercontrolEdwin.app.data.entities.Ruta
import kotlinx.coroutines.flow.Flow

@Dao
interface RutaDao {

    @Query("SELECT * FROM rutas ORDER BY fecha DESC")
    fun getAll(): Flow<List<Ruta>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ruta: Ruta): Long

    @Delete
    suspend fun delete(ruta: Ruta)
}
