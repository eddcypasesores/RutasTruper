package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "folios",
    foreignKeys = [ForeignKey(
        entity = Ruta::class,
        parentColumns = ["id"],
        childColumns = ["rutaId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("rutaId"), Index("folioTruper")]
)
data class Folio(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rutaId: Long,
    val folioTruper: String,
    var nombreEstablecimiento: String? = null,
    var direccion: String? = null,
    var tipoFachada: String? = null,
    var m2Reportados: Double? = null,
    var m2Final: Double? = null,
    var figuras: Int? = null,
    var tarifaTipo: String? = null, // "1-100", "101-300", "301+"
    var estado: String, // "En listado", "Sobrante", "Coincide", "Falta en Tabla", "Falta en Excel", "Cambio reportado", "Validado"
    var observaciones: String? = null,
    var cambioTexto: String? = null, // Almacena el texto del último reporte de cambio
    val tipoSolicitud: String? = null // "MS" o "MY"
)
