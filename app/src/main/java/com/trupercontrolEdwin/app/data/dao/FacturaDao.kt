package com.trupercontrolEdwin.app.data.dao

import androidx.room.*
import com.trupercontrolEdwin.app.data.entities.Factura
import kotlinx.coroutines.flow.Flow

@Dao
interface FacturaDao {

    @Query("SELECT * FROM facturas WHERE folioId = :folioId")
    fun getByFolio(folioId: Long): Flow<List<Factura>>

    @Query("SELECT * FROM facturas")
    suspend fun getAllSimple(): List<Factura>

    @Query("SELECT * FROM facturas WHERE estatus = 'Pendiente'")
    fun getPendientes(): Flow<List<Factura>>

    @Query("SELECT * FROM facturas WHERE folioFactura = :folioFactura LIMIT 1")
    suspend fun getByFolioFactura(folioFactura: String): Factura?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(factura: Factura): Long

    @Update
    suspend fun update(factura: Factura)

    @Query("UPDATE facturas SET estatus = :estatus WHERE id = :facturaId")
    suspend fun updateEstatus(facturaId: Long, estatus: String)
}
