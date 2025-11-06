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
    val observaciones: String? = null,
    val solicitudPdfUri: String? = null,
    val facturaPdfUri: String? = null,
    val facturaXmlUri: String? = null,
    val validacionMensaje: String? = null,
    val validacionFotosUris: String? = null,
    val cambioTexto: String? = null,
    val facturacionExcelUri: String? = null,
    val acuseCancelacionUri: String? = null,
    val documentoPagoUri: String? = null,
    val listadoCoincide: Boolean? = null
)
