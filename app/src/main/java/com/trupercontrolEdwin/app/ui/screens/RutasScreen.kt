package com.trupercontrolEdwin.app.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Ruta
import com.trupercontrolEdwin.app.utils.FolioParser
import com.trupercontrolEdwin.app.utils.OcrProcessor
import com.trupercontrolEdwin.app.utils.PdfReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutasScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    val rutas by db.rutaDao().getAll().collectAsState(initial = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialogoRuta by remember { mutableStateOf(false) }
    var rutaSeleccionada by remember { mutableStateOf<Ruta?>(null) }

    val tablaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val ruta = rutaSeleccionada
        if (uri != null && ruta != null) {
            scope.launch {
                snackbarHostState.showSnackbar("Procesando tabla...")
                val bitmap = decodeBitmap(context, uri)
                if (bitmap == null) {
                    snackbarHostState.showSnackbar("No se pudo procesar la imagen")
                    return@launch
                }
                val texto = OcrProcessor.procesarImagen(bitmap)
                val folios = OcrProcessor.extraerFolios(texto)

                withContext(Dispatchers.IO) {
                    folios.forEach {
                        db.folioDao().insert(Folio(rutaId = ruta.id, folioTruper = it, estado = "En listado"))
                    }
                }

                snackbarHostState.showSnackbar("Tabla procesada: ${folios.size} folios creados")
            }
        }
    }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val ruta = rutaSeleccionada
        if (uris.isNotEmpty() && ruta != null) {
            scope.launch {
                snackbarHostState.showSnackbar("Procesando ${uris.size} PDFs...")
                val processedCount = withContext(Dispatchers.IO) {
                    val solicitudes = uris.mapNotNull {
                        val texto = PdfReader.leerPdf(context, it)
                        if (texto.isNotBlank()) FolioParser.parseSolicitud(texto) else null
                    }

                    if (solicitudes.isEmpty()) {
                        return@withContext 0
                    }

                    solicitudes.forEach { solicitud ->
                        val folioExistente = db.folioDao().findByFolioTruper(solicitud.folio)
                        if (folioExistente != null) {
                            db.folioDao().update(folioExistente.copy(
                                estado = "Coincide",
                                nombreEstablecimiento = solicitud.nombreNegocio,
                                direccion = solicitud.direccion,
                                m2Reportados = solicitud.metrosReportados
                            ))
                        } else {
                            db.folioDao().insert(Folio(
                                rutaId = ruta.id,
                                folioTruper = solicitud.folio,
                                estado = "Sobrante",
                                nombreEstablecimiento = solicitud.nombreNegocio,
                                direccion = solicitud.direccion,
                                m2Reportados = solicitud.metrosReportados
                            ))
                        }
                    }
                    
                    solicitudes.size
                }

                if (processedCount > 0) {
                    snackbarHostState.showSnackbar("$processedCount PDFs procesados y guardados.")
                } else {
                    snackbarHostState.showSnackbar("No se encontraron datos de solicitud en los PDFs.")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rutas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogoRuta = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar ruta")
            }
        }
    ) { padding ->
        if (rutas.isEmpty()) {
            Column(
                modifier = Modifier.padding(padding).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No hay rutas registradas todavía")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(rutas) { ruta ->
                    RutaCard(
                        ruta = ruta,
                        onCargarTabla = {
                            rutaSeleccionada = ruta
                            tablaLauncher.launch("image/*")
                        },
                        onCargarPdfs = {
                            rutaSeleccionada = ruta
                            pdfLauncher.launch("application/pdf")
                        },
                        onAbrirFolios = { navController.navigate("folios/${ruta.id}") }
                    )
                }
            }
        }
    }

    if (mostrarDialogoRuta) {
        DialogNuevaRuta(
            onDismiss = { mostrarDialogoRuta = false },
            onSave = { nombre, fecha, notas ->
                scope.launch {
                    db.rutaDao().insert(Ruta(nombre = nombre, fecha = fecha, notas = notas))
                    mostrarDialogoRuta = false
                }
            }
        )
    }
}

@Composable
private fun RutaCard(
    ruta: Ruta,
    onCargarTabla: () -> Unit,
    onCargarPdfs: () -> Unit,
    onAbrirFolios: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(ruta.nombre, style = MaterialTheme.typography.titleMedium)
            ruta.fecha?.let { Text("Fecha: $it") }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onCargarTabla) { Text("Cargar Tabla") }
                Button(onClick = onCargarPdfs) { Text("Cargar PDFs") }
                Button(onClick = onAbrirFolios) { Text("Abrir Folios") } // Corregido a Button
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogNuevaRuta(
    onDismiss: () -> Unit,
    onSave: (String, String?, String?) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf<String?>(null) }
    var notas by remember { mutableStateOf("") }
    var mostrarDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    if (mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        fecha = sdf.format(Date(it))
                    }
                    mostrarDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva ruta") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = { mostrarDatePicker = true }) {
                    Text(fecha ?: "Seleccionar fecha (opcional)")
                }
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas iniciales") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nombre.isNotBlank()) onSave(nombre.trim(), fecha, notas.takeIf { it.isNotBlank() })
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

private fun getFileName(context: Context, uri: Uri): String? {
    // Try to get the display name from the content resolver first
    if (uri.scheme == "content") {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    return cursor.getString(displayNameIndex)
                }
            }
        }
    }
    // As a fallback, get the last path segment
    return uri.path?.let { path ->
        val cut = path.lastIndexOf('/')
        if (cut != -1) {
            path.substring(cut + 1)
        } else {
            path // The path itself might be the filename
        }
    }
}

private suspend fun decodeBitmap(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: Exception) {
        null
    }
}
