package com.trupercontrolEdwin.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rutas")
data class Ruta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val fecha: String? = null,
    val fotoListadoUri: String? = null,
    val foliosEsperados: String? = null,
    val notas: String? = null
)
