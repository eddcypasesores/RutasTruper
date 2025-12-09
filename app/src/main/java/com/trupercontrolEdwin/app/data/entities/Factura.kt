package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "facturas",
    foreignKeys = [ForeignKey(
        entity = Folio::class,
        parentColumns = ["id"],
        childColumns = ["folioId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["folioId"])]
)
data class Factura(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val folioId: Long,
    val folioFactura: String, 
    val subtotal: Double,
    val iva: Double,
    val total: Double,
    var estado: String, // "Pendiente", "Pagado", "Cancelado"
    var motivoCancelacion: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)
