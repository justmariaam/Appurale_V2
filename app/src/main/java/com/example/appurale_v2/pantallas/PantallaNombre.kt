package com.example.appurale_v2.pantallas

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaNombre(navController: NavController) {

    val context = LocalContext.current
    var nombre by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Bienvenido a Appurale", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text("¿Cómo quieres que te llamemos?")

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Escribe tu nombre") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (nombre.text.isNotEmpty()) {

                val prefs = context.getSharedPreferences("usuario", Context.MODE_PRIVATE)
                prefs.edit().putString("nombre", nombre.text).apply()

                navController.navigate("inicio") {
                    popUpTo("nombre") { inclusive = true }
                }

            }
        }) {
            Text("Continuar")
        }
    }
}