package com.trupercontrolEdwin.app.data.dao

import androidx.room.*
import com.trupercontrolEdwin.app.data.entities.Folio
import kotlinx.coroutines.flow.Flow

@Dao
interface FolioDao {

    @Query("SELECT * FROM folios WHERE rutaId = :rutaId ORDER BY folioTruper ASC")
    fun getByRuta(rutaId: Long): Flow<List<Folio>>

    @Query("SELECT * FROM folios WHERE folioTruper = :folioTruper LIMIT 1")
    suspend fun findByFolioTruper(folioTruper: String): Folio?

    @Query("SELECT * FROM folios WHERE id = :folioId LIMIT 1")
    suspend fun getById(folioId: Long): Folio?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folio: Folio): Long

    @Update
    suspend fun update(folio: Folio)

    @Query("UPDATE folios SET estado = :estado WHERE id = :folioId")
    suspend fun updateEstado(folioId: Long, estado: String)
}
