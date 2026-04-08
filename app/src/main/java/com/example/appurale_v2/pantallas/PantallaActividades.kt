package com.example.appurale_v2.pantallas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.appurale_v2.modelo.Actividad
import com.example.appurale_v2.modelo.Rutina
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRutina(rutina: Rutina, navController: NavHostController, index: Int) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(rutina.nombre)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8f1414), // fondo
                    titleContentColor = Color.White     // texto
                )


            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(rutina.actividades) { actividad ->
                ActividadItemUI(actividad)
            }
        }
    }
}

@Composable
fun ActividadItemUI(actividad: Actividad) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFfcac03)
        ),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = false,
                onCheckedChange = {},
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF8f1414),
                    uncheckedColor = Color.White
                )

            )

            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {

                Text(
                    text = actividad.nombre,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Duración: ${actividad.duracion}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewPantallaRutina() {

    val rutinaFake = Rutina(
        id = 1,
        nombre = "Rutina de Ejemplo",
        actividades = listOf(
            Actividad(1, "Correr", 30, 10, 10, 11),
            Actividad(2, "Caminar", 15, 5, 11, 12),
            Actividad(3, "Estiramiento", 10, 3, 14, 15)
        ),
        dias = listOf("L", "M"),
        inicio = 10,
        fin = 16
    )

    val navController = rememberNavController()

    PantallaRutina(
        rutina = rutinaFake,
        navController = navController,
        index = 0
    )
}
