package com.trupercontrolEdwin.app.data.dao

import androidx.room.*
import com.trupercontrolEdwin.app.data.entities.Pago
import kotlinx.coroutines.flow.Flow

@Dao
interface PagoDao {

    @Query("SELECT * FROM pagos WHERE facturaId = :facturaId")
    fun getByFactura(facturaId: Long): Flow<List<Pago>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pago: Pago): Long
}
