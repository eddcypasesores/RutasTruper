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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Ruta
import com.trupercontrolEdwin.app.utils.CalculoRotulacion
import com.trupercontrolEdwin.app.utils.ExcelReader
import com.trupercontrolEdwin.app.utils.FolioParser
import com.trupercontrolEdwin.app.utils.OcrProcessor
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

    // Estado para eliminación
    var showDeleteDialog by remember { mutableStateOf(false) }
    var rutaToDelete by remember { mutableStateOf<Ruta?>(null) }
    
    // Estado para edición
    var rutaToEdit by remember { mutableStateOf<Ruta?>(null) }
    
    // Estado para reporte de coincidencia
    var reporteCoincidencia by remember { mutableStateOf<String?>(null) }

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
                val foliosDetectados = OcrProcessor.extraerFolios(texto)

                if (foliosDetectados.isEmpty()) {
                     snackbarHostState.showSnackbar("No se encontraron folios en la imagen")
                     return@launch
                }

                withContext(Dispatchers.IO) {
                    val foliosEnRuta = db.folioDao().getByRutaSimple(ruta.id).map { it.folioTruper }.toSet()
                    
                    var insertados = 0
                    foliosDetectados.forEach { folioStr ->
                        val folioLimpio = folioStr.trim()
                        if (folioLimpio !in foliosEnRuta) {
                             db.folioDao().insert(Folio(
                                 rutaId = ruta.id, 
                                 folioTruper = folioLimpio, 
                                 estado = "En listado"
                             ))
                             insertados++
                        }
                    }
                    db.rutaDao().update(ruta.copy(tablaCargada = true))
                }

                snackbarHostState.showSnackbar("Tabla procesada: ${foliosDetectados.size} folios leídos")
            }
        }
    }

    val excelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val ruta = rutaSeleccionada
        if (uri != null && ruta != null) {
            scope.launch {
                snackbarHostState.showSnackbar("Analizando Excel...")
                val solicitudesExcel = withContext(Dispatchers.IO) {
                    ExcelReader.leerExcelSolicitudes(context, uri)
                }

                if (solicitudesExcel.isEmpty()) {
                    snackbarHostState.showSnackbar("No se encontraron datos en el Excel")
                    return@launch
                }
                
                // Lógica de Comparación
                val reporte = withContext(Dispatchers.IO) {
                    val foliosActuales = db.folioDao().getByRutaSimple(ruta.id)
                    
                    // Sets para comparación rápida (Normalizamos con trim)
                    val foliosTablaSet = foliosActuales.map { it.folioTruper.trim() }.toSet()
                    val foliosExcelSet = solicitudesExcel.map { it.folio.trim() }.toSet()
                    
                    val faltanEnTabla = foliosExcelSet - foliosTablaSet
                    val faltanEnExcel = foliosTablaSet - foliosExcelSet
                    val coincidencias = foliosExcelSet.intersect(foliosTablaSet)
                    
                    // 1. Actualizar SOLAMENTE los que coinciden (Intersección)
                    solicitudesExcel.filter { it.folio.trim() in coincidencias }.forEach { sol ->
                        val existente = foliosActuales.find { it.folioTruper.trim() == sol.folio.trim() }
                        existente?.let {
                            db.folioDao().update(it.copy(
                                estado = "Coincide",
                                nombreEstablecimiento = sol.nombreNegocio,
                                direccion = sol.direccion,
                                tipoFachada = sol.tipoFachada,
                                m2Reportados = sol.metrosReportados,
                                tipoSolicitud = sol.extraInfo // Guardamos MS/MY
                            ))
                        }
                    }
                    
                    // 3. Marcar los que faltan en Excel como "Falta en Excel"
                    foliosActuales.filter { it.folioTruper.trim() in faltanEnExcel }.forEach { f ->
                         db.folioDao().update(f.copy(estado = "Falta en Excel"))
                    }
                    
                    // Marcamos que se intentó cargar información
                    db.rutaDao().update(ruta.copy(pdfsCargados = true)) 
                    
                    // Construir Reporte
                    buildString {
                        if (faltanEnTabla.isEmpty() && faltanEnExcel.isEmpty()) {
                            append("Datos cargados con éxito\n100% de coincidencia")
                        } else {
                            appendLine("⚠️ Reporte de Discrepancias")
                            
                            if (faltanEnExcel.isNotEmpty()) {
                                appendLine("\nError: ${faltanEnExcel.size} folios no coinciden (Faltan en el Excel):")
                                appendLine(faltanEnExcel.sorted().joinToString(", "))
                            }
                            
                            if (faltanEnTabla.isNotEmpty()) {
                                appendLine("\nError: ${faltanEnTabla.size} folios no coinciden (Faltan en Paso 1 - Imagen):")
                                appendLine(faltanEnTabla.sorted().joinToString(", "))
                                appendLine("\n(Estos folios NO se han cargado a la ruta)")
                            }
                        }
                    }
                }
                
                reporteCoincidencia = reporte
                snackbarHostState.showSnackbar("Análisis completado")
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
                    // Cargar folios para estadísticas
                    val folios by db.folioDao().getByRuta(ruta.id).collectAsState(initial = emptyList())
                    
                    RutaCard(
                        ruta = ruta,
                        folios = folios,
                        onCargarTabla = {
                            rutaSeleccionada = ruta
                            tablaLauncher.launch("image/*")
                        },
                        onCargarExcel = {
                            rutaSeleccionada = ruta
                            excelLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        },
                        onAbrirFolios = { navController.navigate("folios/${ruta.id}") },
                        onDeleteRuta = {
                            rutaToDelete = ruta
                            showDeleteDialog = true
                        },
                        onEditRuta = {
                            rutaToEdit = ruta
                        }
                    )
                }
            }
        }
    }

    // Diálogo de Reporte de Coincidencia
    if (reporteCoincidencia != null) {
        AlertDialog(
            onDismissRequest = { reporteCoincidencia = null },
            title = { 
                val titulo = if (reporteCoincidencia!!.contains("100%")) "Éxito" else "Discrepancias detectadas"
                Text(titulo) 
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(reporteCoincidencia ?: "")
                }
            },
            confirmButton = {
                TextButton(onClick = { reporteCoincidencia = null }) {
                    Text("Aceptar")
                }
            }
        )
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

    // Diálogo de Edición de Ruta
    if (rutaToEdit != null) {
        DialogEditarRuta(
            ruta = rutaToEdit!!,
            onDismiss = { rutaToEdit = null },
            onSave = { rutaEditada ->
                scope.launch {
                    db.rutaDao().update(rutaEditada)
                    rutaToEdit = null
                    snackbarHostState.showSnackbar("Ruta actualizada")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Deseas eliminar la ruta '${rutaToDelete?.nombre}'?") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch {
                        rutaToDelete?.let { db.rutaDao().delete(it) }
                        showDeleteDialog = false
                        rutaToDelete = null
                    }
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    rutaToDelete = null
                }) { Text("Cancelar") }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RutaCard(
    ruta: Ruta,
    folios: List<Folio>,
    onCargarTabla: () -> Unit,
    onCargarExcel: () -> Unit,
    onAbrirFolios: () -> Unit,
    onDeleteRuta: () -> Unit,
    onEditRuta: () -> Unit
) {
    // Cálculos de estadísticas
    val foliosActivos = folios.filter { it.estado != "Cancelado" }
    val totalFolios = folios.size
    val pagados = foliosActivos.count { it.estado == "Pagado" }
    val pendientes = foliosActivos.count { it.estado != "Pagado" }
    
    val (totalSubtotal, _, totalImporte) = foliosActivos.fold(Triple(0.0, 0.0, 0.0)) { acc, f ->
        val m2 = f.m2Final ?: f.m2Reportados ?: 0.0
        val tarifa = f.tarifaTipo ?: "1-100"
        val figs = f.figuras ?: 0
        val (sub, iva, total) = CalculoRotulacion.calcular(m2, tarifa, figs)
        Triple(acc.first + sub, acc.second + iva, acc.third + total)
    }

    val (pagadoSubtotal, _, pagadoImporte) = foliosActivos.filter { it.estado == "Pagado" }.fold(Triple(0.0, 0.0, 0.0)) { acc, f ->
        val m2 = f.m2Final ?: f.m2Reportados ?: 0.0
        val tarifa = f.tarifaTipo ?: "1-100"
        val figs = f.figuras ?: 0
        val (sub, iva, total) = CalculoRotulacion.calcular(m2, tarifa, figs)
        Triple(acc.first + sub, acc.second + iva, acc.third + total)
    }

    // Si ambos pasos (tabla y excel) están completos, la tarjeta es clickeable
    val procesoCompleto = ruta.tablaCargada && ruta.pdfsCargados
    
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .then(
                if (procesoCompleto) {
                    Modifier.combinedClickable(
                        onClick = onAbrirFolios,
                        onLongClick = { menuExpanded = true }
                    )
                } else {
                    Modifier
                }
            )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(ruta.nombre, style = MaterialTheme.typography.titleMedium)
                    ruta.fecha?.let { Text("Fecha: $it") }
                }
            }

            // Menú contextual (Dropdown)
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar Ruta") },
                    onClick = {
                        menuExpanded = false
                        onEditRuta()
                    },
                    leadingIcon = { Icon(Icons.Default.Edit, null) }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar Ruta") },
                    onClick = {
                        menuExpanded = false
                        onDeleteRuta()
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, null) }
                )
            }
            
            if (!procesoCompleto) {
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = onCargarTabla, enabled = !ruta.tablaCargada) { 
                        Text(if(ruta.tablaCargada) "Tabla Cargada" else "1. Cargar Tabla") 
                    }
                    
                    Button(onClick = onCargarExcel, enabled = ruta.tablaCargada) { 
                         Text("2. Cargar Excel") 
                    }
                }
            }
            
            // Contadores Globales
            if (totalFolios > 0) {
                Spacer(Modifier.height(12.dp))
                Divider()
                Spacer(Modifier.height(8.dp))
                
                Text("Estadísticas:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Fachadas: $totalFolios")
                        Text("Pagadas: $pagados", color = MaterialTheme.colorScheme.primary)
                        Text("Pendientes: $pendientes", color = MaterialTheme.colorScheme.error)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                         Text("Global sin IVA:", style = MaterialTheme.typography.bodyMedium)
                         Text("$${"%,.2f".format(totalSubtotal)}", fontWeight = FontWeight.Bold)
                         
                         Text("Global con IVA:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                         Text("$${"%,.2f".format(totalImporte)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                         
                         Spacer(Modifier.height(8.dp))

                         Text("Pagado sin IVA:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                         Text("$${"%,.2f".format(pagadoSubtotal)}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)

                         Text("Pagado con IVA:", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(top = 4.dp))
                         Text("$${"%,.2f".format(pagadoImporte)}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
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
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DialogEditarRuta(
    ruta: Ruta,
    onDismiss: () -> Unit,
    onSave: (Ruta) -> Unit
) {
    var nombre by remember { mutableStateOf(ruta.nombre) }
    var fecha by remember { mutableStateOf(ruta.fecha) }
    var notas by remember { mutableStateOf(ruta.notas ?: "") }
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
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
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
        title = { Text("Editar Ruta") },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                Spacer(Modifier.height(16.dp))
                OutlinedButton(onClick = { mostrarDatePicker = true }) {
                    Text(fecha ?: "Seleccionar fecha")
                }
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (nombre.isNotBlank()) {
                    onSave(ruta.copy(nombre = nombre.trim(), fecha = fecha, notas = notas.ifBlank { null }))
                }
            }) { Text("Guardar Cambios") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

private fun getFileName(context: Context, uri: Uri): String? {
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
    return uri.path?.let { path ->
        val cut = path.lastIndexOf('/')
        if (cut != -1) {
            path.substring(cut + 1)
        } else {
            path
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
