package com.trupercontrolEdwin.app.data.model

import androidx.room.ColumnInfo

data class PagoConFactura(
    val folioFactura: String,
    @ColumnInfo(name = "monto") val montoPago: Double,
    @ColumnInfo(name = "fecha") val fechaPago: Long
)
