package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rutas")
data class Ruta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val fecha: String? = null,
    val fotoListadoUri: String? = null,
    val foliosEsperados: String? = null, // Folios de la tabla de imagen
    val foliosRecibidosPdf: String? = null, // Folios de los PDFs
    val notas: String? = null,
    val tablaCargada: Boolean = false,
    val pdfsCargados: Boolean = false
)
