package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.utils.ReportesManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Date
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val reportesManager = remember { ReportesManager(db) }
    val scope = rememberCoroutineScope()

    // Estados para los selectores y resultados
    var tipoReporte by remember { mutableStateOf("Mensual") }
    var subtotal by remember { mutableStateOf(0.0) }
    var iva by remember { mutableStateOf(0.0) }

    // Lógica para reporte Mensual
    val calendar = Calendar.getInstance()
    val aniosDisponibles = (calendar.get(Calendar.YEAR) downTo 2020).toList()
    val mesesDisponibles = (0..11).map { 
        val cal = Calendar.getInstance(); cal.set(Calendar.MONTH, it);
        SimpleDateFormat("MMMM", Locale.getDefault()).format(cal.time)
    }
    var anioSeleccionado by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var mesSeleccionado by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }

    // Lógica para reporte por Rango
    var fechaInicio by remember { mutableStateOf<Long?>(null) }
    var fechaFin by remember { mutableStateOf<Long?>(null) }
    var mostrarDatePickerInicio by remember { mutableStateOf(false) }
    var mostrarDatePickerFin by remember { mutableStateOf(false) }

    fun calcularReporte() {
        scope.launch {
            val (sub, iv) = if (tipoReporte == "Mensual") {
                reportesManager.getTotalesPorMes(mesSeleccionado, anioSeleccionado)
            } else {
                if (fechaInicio != null && fechaFin != null) {
                    reportesManager.getTotalesPorRango(fechaInicio!!, fechaFin!!)
                } else Pair(0.0, 0.0)
            }
            subtotal = sub
            iva = iv
        }
    }

    // Recalcular cuando cambien los parámetros
    LaunchedEffect(tipoReporte, anioSeleccionado, mesSeleccionado, fechaInicio, fechaFin) {
        calcularReporte()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reporte de Facturación") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            // Selector de Tipo de Reporte
            SegmentedButtonRow(tipoReporte) { tipoReporte = it }
            Spacer(Modifier.height(16.dp))

            // Filtros
            if (tipoReporte == "Mensual") {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DropdownSelector(label = "Año", selected = anioSeleccionado.toString(), options = aniosDisponibles.map { it.toString() }, onSelected = { anioSeleccionado = it.toInt() }, modifier = Modifier.weight(1f))
                    DropdownSelector(label = "Mes", selected = mesesDisponibles[mesSeleccionado], options = mesesDisponibles, onSelected = { mesSeleccionado = mesesDisponibles.indexOf(it) }, modifier = Modifier.weight(1f))
                }
            } else { // Rango
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { mostrarDatePickerInicio = true }, modifier = Modifier.weight(1f)) {
                        Text(fechaInicio?.toFormattedDate() ?: "Fecha Inicio")
                    }
                    Button(onClick = { mostrarDatePickerFin = true }, modifier = Modifier.weight(1f)) {
                        Text(fechaFin?.toFormattedDate() ?: "Fecha Fin")
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            // Resultados
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Totales del Periodo", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal:", style = MaterialTheme.typography.bodyLarge)
                        Text("$${"%,.2f".format(subtotal)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }
                     Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("IVA:", style = MaterialTheme.typography.bodyLarge)
                        Text("$${"%,.2f".format(iva)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    }
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total General:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("$${"%,.2f".format(subtotal + iva)}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }

    // Date Pickers
    val datePickerStateInicio = rememberDatePickerState()
    if (mostrarDatePickerInicio) {
        DatePickerDialog(onDismissRequest = { mostrarDatePickerInicio = false }, confirmButton = { TextButton(onClick = { fechaInicio = datePickerStateInicio.selectedDateMillis; mostrarDatePickerInicio = false }) { Text("OK") } }) {
             DatePicker(state = datePickerStateInicio)
        }
    }
    val datePickerStateFin = rememberDatePickerState()
    if (mostrarDatePickerFin) {
        DatePickerDialog(onDismissRequest = { mostrarDatePickerFin = false }, confirmButton = { TextButton(onClick = { fechaFin = datePickerStateFin.selectedDateMillis; mostrarDatePickerFin = false }) { Text("OK") } }) {
             DatePicker(state = datePickerStateFin)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SegmentedButtonRow(selected: String, onSelected: (String) -> Unit) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            selected = selected == "Mensual",
            onClick = { onSelected("Mensual") },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
        ) {
            Text("Mensual")
        }
        SegmentedButton(
            selected = selected == "Rango",
            onClick = { onSelected("Rango") },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
        ) {
            Text("Rango")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return sdf.format(Date(this))
}
