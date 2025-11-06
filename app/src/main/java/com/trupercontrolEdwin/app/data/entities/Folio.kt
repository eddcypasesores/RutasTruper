package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folios")
data class Folio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rutaId: Long,
    val folioTruper: String,
    val nombreEstablecimiento: String? = null,
    val direccion: String? = null,
    val tipoFachada: String? = null,
    val m2Reportados: Double? = null,
    val m2Final: Double? = null,
    val figuras: Int? = null,
    val tarifaTipo: String? = null,  // "1-100", "101-300", "301+"
    val estado: String = "Recibido",
    val observaciones: String? = null
)
