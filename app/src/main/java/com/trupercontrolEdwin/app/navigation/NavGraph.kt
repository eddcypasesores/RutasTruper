package com.trupercontrolEdwin.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.trupercontrolEdwin.app.ui.screens.*

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "inicio") {
        composable("inicio") { InicioScreen(navController) }
        composable("rutas") { RutasScreen(navController) }
        composable(
            route = "folios/{rutaId}",
            arguments = listOf(navArgument("rutaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val rutaId = backStackEntry.arguments?.getLong("rutaId") ?: 0L
            FoliosScreen(navController, rutaId)
        }
        composable(
            route = "facturas/{folioId}",
            arguments = listOf(navArgument("folioId") { type = NavType.LongType })
        ) { backStackEntry ->
            val folioId = backStackEntry.arguments?.getLong("folioId") ?: 0L
            FacturasScreen(navController, folioId)
        }
        composable("pagos") { PagosScreen(navController) }
        composable("reportes") { ReportesScreen(navController) }
        composable("config") { ConfiguracionScreen(navController) }
    }
}
