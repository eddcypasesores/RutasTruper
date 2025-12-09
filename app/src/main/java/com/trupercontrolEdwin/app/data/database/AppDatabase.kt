package com.trupercontrolEdwin.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trupercontrolEdwin.app.data.dao.*
import com.trupercontrolEdwin.app.data.entities.*

@Database(
    entities = [Ruta::class, Folio::class, Factura::class, Pago::class],
    version = 8, // Se incrementó la versión para la tabla Pagos
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun rutaDao(): RutaDao
    abstract fun folioDao(): FolioDao
    abstract fun facturaDao(): FacturaDao
    abstract fun pagoDao(): PagoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "control_rotulaciones.db"
                )
                    // Esta opción destruye y recrea la base de datos si la versión cambia,
                    // evitando el "crash" cuando se agregan o modifican columnas.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
