package com.trupercontrolEdwin.app.data.model

import androidx.room.Embedded
import com.trupercontrolEdwin.app.data.entities.Factura

data class FacturaConDetalles(
    @Embedded
    val factura: Factura,
    val folioTruper: String
)
