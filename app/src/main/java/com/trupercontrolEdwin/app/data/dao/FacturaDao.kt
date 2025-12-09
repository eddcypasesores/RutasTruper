package com.trupercontrolEdwin.app.data.dao

import androidx.room.*
import com.trupercontrolEdwin.app.data.entities.Factura
import kotlinx.coroutines.flow.Flow

@Dao
interface FacturaDao {
    @Insert
    suspend fun insert(factura: Factura)

    @Update
    suspend fun update(factura: Factura)

    @Delete
    suspend fun delete(factura: Factura)

    @Query("SELECT * FROM facturas WHERE folioId = :folioId")
    fun getByFolio(folioId: Long): Flow<List<Factura>>

    @Query("SELECT * FROM facturas")
    suspend fun getAllSimple(): List<Factura>
    
    @Query("SELECT * FROM facturas WHERE estado = 'Pendiente'")
    suspend fun getPendientes(): List<Factura>

    @Query("SELECT SUM(subtotal) FROM facturas WHERE fechaCreacion BETWEEN :inicio AND :fin AND estado != 'Cancelado'")
    suspend fun getSumSubtotalInPeriod(inicio: Long, fin: Long): Double?

    @Query("SELECT SUM(iva) FROM facturas WHERE fechaCreacion BETWEEN :inicio AND :fin AND estado != 'Cancelado'")
    suspend fun getSumIvaInPeriod(inicio: Long, fin: Long): Double?

    @Query("SELECT * FROM facturas WHERE folioFactura LIKE '%' || :query || '%'")
    suspend fun buscarPorFolioFactura(query: String): List<Factura>

    @Query("SELECT * FROM facturas WHERE id = :facturaId LIMIT 1")
    fun getById(facturaId: Long): Flow<Factura?>
}
