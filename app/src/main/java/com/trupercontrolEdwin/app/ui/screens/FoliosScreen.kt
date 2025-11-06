package com.trupercontrolEdwin.app.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.entities.Folio
import com.trupercontrolEdwin.app.data.entities.Ruta
import com.trupercontrolEdwin.app.utils.CalculoRotulacion
import com.trupercontrolEdwin.app.utils.ExcelGenerator
import com.trupercontrolEdwin.app.utils.FolioParser
import com.trupercontrolEdwin.app.utils.PdfReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectAsState
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun FoliosScreen(navController: NavController, rutaId: Long) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    var ruta by remember { mutableStateOf<Ruta?>(null) }
    val folios by db.folioDao().getByRuta(rutaId).collectAsState(initial = emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    var mostrarDialogoCambio by remember { mutableStateOf(false) }
    var textoCambio by remember { mutableStateOf("") }
    var folioEnEdicion by remember { mutableStateOf<Folio?>(null) }
    var mensajeValidacion by remember { mutableStateOf<String?>(null) }
    var folioParaDocumento by remember { mutableStateOf<Folio?>(null) }

    LaunchedEffect(rutaId) {
        ruta = db.rutaDao().getById(rutaId)
    }

    val solicitudLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            scope.launch {
                procesarSolicitud(context, db, rutaId, ruta, uri, snackbarHostState)
            }
        }
    }

    val documentoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        val folioTarget = folioParaDocumento
        if (uri != null && folioTarget != null) {
            scope.launch {
                procesarDocumento(context, db, folioTarget, uri, snackbarHostState)
            }
        }
        folioParaDocumento = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Folios") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { solicitudLauncher.launch(arrayOf("application/pdf")) }) {
                        Icon(Icons.Default.Add, contentDescription = "Importar solicitud")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            ruta?.let {
                RutaResumen(it)
                Spacer(Modifier.height(16.dp))
            }

            Row(Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = { mostrarDialogoCambio = true }) {
                    Text("Registrar cambio")
                }
                Button(onClick = {
                    scope.launch {
                        if (folios.isEmpty()) {
                            snackbarHostState.showSnackbar("No hay folios para generar Excel")
                        } else {
                            val archivo = generarExcel(context, folios)
                            folios.forEach { folio ->
                                val nuevoEstado = if (folio.estado == "Validado") "En facturación" else folio.estado
                                db.folioDao().update(folio.copy(facturacionExcelUri = archivo.absolutePath, estado = nuevoEstado))
                            }
                            snackbarHostState.showSnackbar("Excel generado: ${archivo.name}")
                        }
                    }
                }) {
                    Text("Generar Excel")
                }
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn(Modifier.fillMaxSize()) {
                items(folios) { folio ->
                    FolioCard(
                        folio = folio,
                        ruta = ruta,
                        onEditar = { folioEnEdicion = folio },
                        onGenerarValidacion = {
                            val mensaje = FolioParser.generarMensajeValidacion(folio, ruta)
                            mensajeValidacion = mensaje
                            scope.launch {
                                db.folioDao().update(folio.copy(validacionMensaje = mensaje, estado = "En validación"))
                            }
                        },
                        onFacturas = {
                            navController.navigate("facturas/${folio.id}")
                        },
                        onProcesarDocumento = {
                            folioParaDocumento = folio
                            documentoLauncher.launch(arrayOf("application/pdf", "application/xml", "text/xml"))
                        },
                        onMarcarValidado = {
                            scope.launch {
                                db.folioDao().update(folio.copy(estado = "Validado"))
                                snackbarHostState.showSnackbar("Folio ${folio.folioTruper} validado")
                            }
                        }
                    )
                }
            }
        }
    }

    if (mostrarDialogoCambio) {
        DialogCambioFolio(
            onDismiss = { mostrarDialogoCambio = false },
            onProcesar = { texto ->
                scope.launch {
                    val cambio = FolioParser.parseCambio(texto)
                    if (cambio == null) {
                        snackbarHostState.showSnackbar("No se pudo interpretar el texto del cambio")
                    } else {
                        val folio = db.folioDao().findByFolioTruper(cambio.folio)
                        if (folio == null) {
                            snackbarHostState.showSnackbar("No existe un folio ${cambio.folio} en la ruta")
                        } else {
                            val actualizado = folio.copy(
                                m2Reportados = cambio.metrosSolicitud ?: folio.m2Reportados,
                                m2Final = cambio.metrosFinales ?: folio.m2Final,
                                cambioTexto = texto,
                                observaciones = listOfNotNull(folio.observaciones, cambio.comentarios)
                                    .filter { it.isNotBlank() }
                                    .joinToString("\n")
                                    .ifBlank { null },
                                estado = "Cambio reportado"
                            )
                            db.folioDao().update(actualizado)
                            snackbarHostState.showSnackbar("Cambio registrado para folio ${folio.folioTruper}")
                        }
                    }
                    mostrarDialogoCambio = false
                    textoCambio = ""
                }
            },
            textoInicial = textoCambio,
            onTextoChange = { textoCambio = it }
        )
    }

    if (folioEnEdicion != null) {
        DialogEditarFolio(
            folio = folioEnEdicion!!,
            onDismiss = { folioEnEdicion = null },
            onSave = { actualizado ->
                scope.launch {
                    db.folioDao().update(actualizado)
                    folioEnEdicion = null
                    snackbarHostState.showSnackbar("Folio actualizado")
                }
            }
        )
    }

    if (mensajeValidacion != null) {
        DialogMensajeValidacion(
            mensaje = mensajeValidacion!!,
            onDismiss = { mensajeValidacion = null }
        )
    }
}

@Composable
private fun RutaResumen(ruta: Ruta) {
    Card(Modifier.padding(horizontal = 16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(ruta.nombre, style = MaterialTheme.typography.titleMedium)
            ruta.fecha?.let { Text("Fecha: $it") }
            ruta.foliosEsperados?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(8.dp))
                val lista = it.split(',').map { folio -> folio.trim() }.filter { folio -> folio.isNotEmpty() }
                Text("Folios esperados (${lista.size}): ${lista.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
            }
            ruta.notas?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(8.dp))
                Text("Notas:\n$it")
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun FolioCard(
    folio: Folio,
    ruta: Ruta?,
    onEditar: () -> Unit,
    onGenerarValidacion: () -> Unit,
    onFacturas: () -> Unit,
    onProcesarDocumento: () -> Unit,
    onMarcarValidado: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Folio TRUPER: ${folio.folioTruper}", style = MaterialTheme.typography.titleMedium)
            folio.nombreEstablecimiento?.let { Text("Cliente: $it") }
            folio.direccion?.let { Text("Dirección: $it") }
            folio.tipoFachada?.let { Text("Tipo de fachada: $it") }
            folio.listadoCoincide?.let {
                val etiqueta = if (it) "Coincide con listado" else "No estaba en listado"
                Text(etiqueta, color = if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            }
            Spacer(Modifier.height(8.dp))
            val metros = folio.m2Reportados ?: 0.0
            val finales = folio.m2Final ?: metros
            val figuras = folio.figuras ?: 0
            val tarifa = folio.tarifaTipo ?: "1-100"
            val (subtotal, iva, total) = CalculoRotulacion.calcular(finales, tarifa, figuras)
            Text("m² solicitud: ${"%.2f".format(metros)}")
            Text("m² finales: ${"%.2f".format(finales)}")
            Text("Figuras: $figuras | Tarifa: $tarifa")
            Text("Subtotal: $${"%,.2f".format(subtotal)} | IVA: $${"%,.2f".format(iva)} | Total: $${"%,.2f".format(total)}")
            Spacer(Modifier.height(8.dp))
            Text("Estado: ${folio.estado}", style = MaterialTheme.typography.titleSmall)
            folio.observaciones?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(4.dp))
                Text("Notas: $it")
            }
            Spacer(Modifier.height(12.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onEditar) { Text("Editar") }
                OutlinedButton(onClick = onGenerarValidacion) { Text("Generar validación") }
                OutlinedButton(onClick = onMarcarValidado) { Text("Marcar validado") }
                OutlinedButton(onClick = onProcesarDocumento) { Text("Procesar PDF/XML") }
                TextButton(onClick = onFacturas) { Text("Facturas") }
            }
        }
    }
}

@Composable
private fun DialogCambioFolio(
    onDismiss: () -> Unit,
    onProcesar: (String) -> Unit,
    textoInicial: String,
    onTextoChange: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar cambio de metros") },
        text = {
            OutlinedTextField(
                value = textoInicial,
                onValueChange = onTextoChange,
                label = { Text("Texto del reporte") },
                minLines = 6
            )
        },
        confirmButton = {
            TextButton(onClick = { onProcesar(textoInicial) }) { Text("Procesar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DialogEditarFolio(
    folio: Folio,
    onDismiss: () -> Unit,
    onSave: (Folio) -> Unit
) {
    var nombre by remember { mutableStateOf(folio.nombreEstablecimiento.orEmpty()) }
    var direccion by remember { mutableStateOf(folio.direccion.orEmpty()) }
    var tipo by remember { mutableStateOf(folio.tipoFachada.orEmpty()) }
    var m2Reportados by remember { mutableStateOf(folio.m2Reportados?.toString().orEmpty()) }
    var m2Finales by remember { mutableStateOf(folio.m2Final?.toString().orEmpty()) }
    var figuras by remember { mutableStateOf(folio.figuras?.toString().orEmpty()) }
    var tarifa by remember { mutableStateOf(folio.tarifaTipo ?: "1-100") }
    var estado by remember { mutableStateOf(folio.estado) }
    var notas by remember { mutableStateOf(folio.observaciones.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar folio ${folio.folioTruper}") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre del establecimiento") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo de fachada") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = m2Reportados, onValueChange = { m2Reportados = it }, label = { Text("m² solicitados") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = m2Finales, onValueChange = { m2Finales = it }, label = { Text("m² finales") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = figuras, onValueChange = { figuras = it }, label = { Text("Figuras/Cajones") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = tarifa, onValueChange = { tarifa = it }, label = { Text("Tarifa (1-100 / 101-300 / 301+)") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = notas, onValueChange = { notas = it }, label = { Text("Notas u observaciones") }, minLines = 2)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val m2R = m2Reportados.toDoubleOrNull()
                val m2F = m2Finales.toDoubleOrNull()
                val figs = figuras.toIntOrNull()
                onSave(
                    folio.copy(
                        nombreEstablecimiento = nombre.ifBlank { null },
                        direccion = direccion.ifBlank { null },
                        tipoFachada = tipo.ifBlank { null },
                        m2Reportados = m2R,
                        m2Final = m2F,
                        figuras = figs,
                        tarifaTipo = tarifa.ifBlank { "1-100" },
                        estado = estado.ifBlank { folio.estado },
                        observaciones = notas.ifBlank { null }
                    )
                )
            }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun DialogMensajeValidacion(
    mensaje: String,
    onDismiss: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Mensaje de validación") },
        text = { Text(mensaje) },
        confirmButton = {
            TextButton(onClick = {
                clipboard.setText(AnnotatedString(mensaje))
                onDismiss()
            }) { Text("Copiar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

private suspend fun procesarSolicitud(
    context: Context,
    db: AppDatabase,
    rutaId: Long,
    ruta: Ruta?,
    uri: Uri,
    snackbarHostState: SnackbarHostState
) {
    withContext(Dispatchers.IO) {
        try {
            val texto = PdfReader.leerPdf(context, uri)
            val solicitud = FolioParser.parseSolicitud(texto)
            if (solicitud == null) {
                withContext(Dispatchers.Main) {
                    snackbarHostState.showSnackbar("No se identificó un folio en el PDF")
                }
                return@withContext
            }
            val coincide = ruta?.foliosEsperados
                ?.split(',')
                ?.map { it.trim() }
                ?.any { it == solicitud.folio } ?: false
            val existente = db.folioDao().findByFolioTruper(solicitud.folio)
            if (existente == null) {
                val nuevo = Folio(
                    rutaId = rutaId,
                    folioTruper = solicitud.folio,
                    nombreEstablecimiento = solicitud.nombreNegocio,
                    direccion = solicitud.direccion,
                    tipoFachada = solicitud.tipoFachada,
                    m2Reportados = solicitud.metrosReportados,
                    m2Final = solicitud.metrosReportados,
                    tarifaTipo = "1-100",
                    solicitudPdfUri = uri.toString(),
                    listadoCoincide = coincide,
                    estado = "Recibido"
                )
                db.folioDao().insert(nuevo)
            } else {
                val actualizado = existente.copy(
                    rutaId = rutaId,
                    nombreEstablecimiento = solicitud.nombreNegocio ?: existente.nombreEstablecimiento,
                    direccion = solicitud.direccion ?: existente.direccion,
                    tipoFachada = solicitud.tipoFachada ?: existente.tipoFachada,
                    m2Reportados = solicitud.metrosReportados ?: existente.m2Reportados,
                    m2Final = existente.m2Final ?: solicitud.metrosReportados,
                    solicitudPdfUri = uri.toString(),
                    listadoCoincide = coincide,
                    estado = if (existente.estado == "Recibido") "Por validar" else existente.estado
                )
                db.folioDao().update(actualizado)
            }
            withContext(Dispatchers.Main) {
                snackbarHostState.showSnackbar("Solicitud procesada para folio ${solicitud.folio}")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                snackbarHostState.showSnackbar("Error al procesar PDF: ${e.localizedMessage}")
            }
        }
    }
}

private suspend fun procesarDocumento(
    context: Context,
    db: AppDatabase,
    folio: Folio,
    uri: Uri,
    snackbarHostState: SnackbarHostState
) {
    withContext(Dispatchers.IO) {
        try {
            val type = context.contentResolver.getType(uri) ?: ""
            val actualizado = if (type.contains("xml")) {
                folio.copy(facturaXmlUri = uri.toString(), estado = "Pendiente de pago")
            } else {
                val texto = PdfReader.leerPdf(context, uri)
                when {
                    PdfReader.esAvisoPago(texto) -> folio.copy(documentoPagoUri = uri.toString(), estado = "Pagado")
                    PdfReader.esAcuseCancelacion(texto) -> folio.copy(acuseCancelacionUri = uri.toString(), estado = "Cancelado")
                    else -> folio.copy(facturaPdfUri = uri.toString(), estado = "Pendiente de pago")
                }
            }
            db.folioDao().update(actualizado)
            val mensaje = when (actualizado.estado) {
                "Pagado" -> "Documento de pago registrado"
                "Cancelado" -> "Acuse de cancelación registrado"
                "Pendiente de pago" -> "Factura registrada"
                else -> "Documento cargado"
            }
            withContext(Dispatchers.Main) {
                snackbarHostState.showSnackbar("${folio.folioTruper}: $mensaje")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                snackbarHostState.showSnackbar("Error al procesar documento: ${e.localizedMessage}")
            }
        }
    }
}

private suspend fun generarExcel(context: Context, folios: List<Folio>): File = withContext(Dispatchers.IO) {
    val carpetaDestino = File(context.getExternalFilesDir(null), "facturacion")
    ExcelGenerator.generarExcelParaFolios(context, folios, carpetaDestino)
}
