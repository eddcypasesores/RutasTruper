package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "facturas")
data class Factura(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val folioId: Long,                // FK → Folio
    val folioFactura: String,         // EZ1, EZ7...
    val folioFacturaNum: Int? = null, // 1, 7...
    val fechaEmision: String? = null,
    val subtotal: Double = 0.0,
    val iva: Double = 0.0,
    val total: Double = 0.0,
    val estatus: String = "Pendiente", // Pendiente, Pagada, Cancelada
    val motivoCancelacion: String? = null,
    val folioReemplaza: String? = null,
    val pdfUri: String? = null,
    val xmlUri: String? = null,
    val observaciones: String? = null
)
