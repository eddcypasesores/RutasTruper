package com.trupercontrolEdwin.app.ui.screens

import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.utils.BackupManager
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    var carpetaActual by remember {
        mutableStateOf(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                .absolutePath + "/Rotulaciones_Truper"
        )
    }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Carpeta actual:", style = MaterialTheme.typography.titleSmall)
            Text(carpetaActual, Modifier.padding(bottom = 16.dp))

            Button(onClick = {
                // aquí podrías abrir un selector de carpetas (Storage Access Framework)
                // de momento solo cambiamos a otra carpeta por demo
                carpetaActual = carpetaActual + "_2"
            }) {
                Text("Cambiar carpeta")
            }

            Spacer(Modifier.height(24.dp))

            Button(onClick = {
                scope.launch {
                    val backupFile = File(carpetaActual, "backup_control_rotulaciones.json")
                    backupFile.parentFile?.mkdirs()
                    val ok = BackupManager(context, db).exportar(backupFile)
                    mensaje = if (ok) "Respaldo creado en: ${backupFile.absolutePath}" else "Error al crear respaldo"
                }
            }) {
                Text("Exportar respaldo")
            }

            if (mensaje.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(mensaje, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
