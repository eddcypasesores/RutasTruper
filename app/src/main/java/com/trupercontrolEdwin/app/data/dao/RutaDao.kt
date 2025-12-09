package com.trupercontrolEdwin.app.data.dao

import androidx.room.*
import com.trupercontrolEdwin.app.data.entities.Ruta
import kotlinx.coroutines.flow.Flow

@Dao
interface RutaDao {
    @Query("SELECT * FROM rutas ORDER BY fecha DESC")
    fun getAll(): Flow<List<Ruta>>

    @Query("SELECT * FROM rutas")
    suspend fun getAllSimple(): List<Ruta>

    @Query("SELECT * FROM rutas WHERE id = :id")
    suspend fun getById(id: Long): Ruta?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ruta: Ruta): Long

    @Update
    suspend fun update(ruta: Ruta)

    @Delete
    suspend fun delete(ruta: Ruta)
}
