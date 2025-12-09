package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "pagos",
    foreignKeys = [ForeignKey(
        entity = Factura::class,
        parentColumns = ["id"],
        childColumns = ["facturaId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Pago(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val facturaId: Long,
    val monto: Double,
    val fecha: Long = System.currentTimeMillis()
)
