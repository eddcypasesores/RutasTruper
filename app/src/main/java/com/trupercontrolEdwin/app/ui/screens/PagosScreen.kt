package com.trupercontrolEdwin.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.trupercontrolEdwin.app.data.database.AppDatabase
import com.trupercontrolEdwin.app.data.model.FacturaConDetalles
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun PagosScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { AppDatabase.get(context) }
    val scope = rememberCoroutineScope()

    val todasLasFacturas by db.facturaDao().getAllFacturasConDetalles().collectAsState(initial = emptyList())

    val facturasPendientes = remember(todasLasFacturas) {
        todasLasFacturas.filter { it.factura.estado == "Pendiente" }
    }
    val facturasPagadas = remember(todasLasFacturas) {
        todasLasFacturas.filter { it.factura.estado == "Pagado" }
    }
    val facturasCanceladas = remember(todasLasFacturas) {
        todasLasFacturas.filter { it.factura.estado == "Cancelado" }
    }

    val pagerState = rememberPagerState()
    val tabs = listOf("Pendientes", "Pagadas", "Canceladas")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Facturas y Pagos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        onClick = { 
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                count = tabs.size,
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) {
                page ->
                when (page) {
                    0 -> FacturasList(facturas = facturasPendientes)
                    1 -> FacturasList(facturas = facturasPagadas)
                    2 -> FacturasList(facturas = facturasCanceladas)
                }
            }
        }
    }
}

@Composable
fun FacturasList(facturas: List<FacturaConDetalles>) {
    if (facturas.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("No hay facturas en esta categoría.")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(facturas) { facturaDetalle ->
                FacturaItem(facturaDetalle = facturaDetalle)
            }
        }
    }
}

@Composable
fun FacturaItem(facturaDetalle: FacturaConDetalles) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = "Factura: ${facturaDetalle.factura.folioFactura}", 
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Fachada: ${facturaDetalle.folioTruper}", 
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:")
                Text("$${String.format("%,.2f", facturaDetalle.factura.total)}", fontWeight = FontWeight.Bold)
            }
        }
    }
}
