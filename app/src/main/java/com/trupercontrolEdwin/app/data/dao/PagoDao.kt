package com.trupercontrolEdwin.app.data.dao

import androidx.room.*
import com.trupercontrolEdwin.app.data.entities.Pago
import com.trupercontrolEdwin.app.data.model.PagoConFactura
import kotlinx.coroutines.flow.Flow

@Dao
interface PagoDao {

    @Insert
    suspend fun insert(pago: Pago): Long

    @Query("SELECT * FROM pagos WHERE facturaId = :facturaId ORDER BY fecha DESC")
    fun getPagosByFactura(facturaId: Long): Flow<List<Pago>>

    @Query("SELECT * FROM pagos")
    suspend fun getAllSimple(): List<Pago>

    @Query("""
        SELECT f.folioFactura, p.monto, p.fecha 
        FROM pagos p 
        INNER JOIN facturas f ON p.facturaId = f.id 
        ORDER BY p.fecha DESC
    """)
    fun getPagosConFactura(): Flow<List<PagoConFactura>>
}
