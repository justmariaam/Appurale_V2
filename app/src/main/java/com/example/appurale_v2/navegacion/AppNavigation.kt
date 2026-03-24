package com.example.appurale_v2.navegacion

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.appurale_v2.pantallas.PantallaInicio
import com.example.appurale_v2.pantallas.PantallaNombre

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val context = LocalContext.current

    val prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
    val nombre = prefs.getString("nombre", "")

    val startDestination = if (nombre.isNullOrEmpty()) "nombre" else "inicio"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("nombre") {
            PantallaNombre(navController)
        }

        composable("inicio") {
            PantallaInicio(navController)
        }
    }
}