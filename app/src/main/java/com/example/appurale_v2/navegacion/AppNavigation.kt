package com.example.appurale_v2.navegacion

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.appurale_v2.pantallas.*
import com.example.appurale_v2.viewmodel.RutinaViewModel
import com.example.appurale_v2.viewmodel.TareasViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    val prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
    val nombre = prefs.getString("nombre", "")

    val startDestination = if (nombre.isNullOrEmpty()) "nombre" else "inicio"

    val rutinaViewModel: RutinaViewModel = viewModel()
    val tareasViewModel: TareasViewModel = viewModel()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("nombre") {
            PantallaNombre(navController)
        }

        composable("inicio") {
            PantallaInicio(navController)
        }

        composable("rutinas") {
            PantallaRutinas(navController, rutinaViewModel)
        }

        composable("crearRutina?index={index}") { backStackEntry ->

            val index = backStackEntry.arguments
                ?.getString("index")
                ?.toInt() ?: -1

            PantallaCrearRutina(
                navController,
                rutinaViewModel,
                index
            )
        }

        composable("detalleRutina/{index}") { backStackEntry ->

            val index = backStackEntry.arguments
                ?.getString("index")
                ?.toInt() ?: 0

            val rutina = rutinaViewModel.lista[index]

            PantallaRutina(rutina)
        }
        composable("agregarActividad?index={index}") { backStackEntry ->

            val index = backStackEntry.arguments
                ?.getString("index")
                ?.toInt() ?: -1

            PantallaAgregar(
                navController,
                index,
                rutinaViewModel
            )
        }
    }
}