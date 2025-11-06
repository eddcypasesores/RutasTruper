package com.trupercontrolEdwin.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.trupercontrolEdwin.app.ui.screens.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "inicio") {
        composable("inicio") { InicioScreen(navController) }
        composable("rutas") { RutasScreen(navController) }
        composable("folios") { FoliosScreen(navController) }
        composable("facturas") { FacturasScreen(navController) }
        composable("pagos") { PagosScreen(navController) }
        composable("reportes") { ReportesScreen(navController) }
        composable("config") { ConfiguracionScreen(navController) }
    }
}
