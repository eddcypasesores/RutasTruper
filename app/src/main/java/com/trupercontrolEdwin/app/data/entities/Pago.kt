package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pagos")
data class Pago(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val facturaId: Long,     // FK → Factura
    val fechaPago: String? = null,
    val monto: Double = 0.0,
    val documentoPagoUri: String? = null,
    val referenciaBanco: String? = null,
    val tipo: String? = null // "TRUPER", "Transferencia"
)
