package com.example.appurale_v2.pantallas

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appurale_v2.viewmodel.RutinaViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PantallaCrearRutina(
    navController: NavController,
    viewModel: RutinaViewModel,
    index: Int = -1
) {

    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }

    var inicio by remember { mutableStateOf(System.currentTimeMillis()) }
    var fin by remember { mutableStateOf(System.currentTimeMillis()) }

    val formato = SimpleDateFormat("HH:mm", Locale.getDefault())

    val diasSemana = listOf("D","L","M","X","J","V","S")
    var diasSeleccionados by remember { mutableStateOf(setOf<String>()) }


    var corriendo by remember { mutableStateOf(false) }
    var tiempoRestante by remember { mutableStateOf(0L) }

    LaunchedEffect(corriendo, tiempoRestante) {
        if (corriendo && tiempoRestante > 0) {
            delay(1000)
            tiempoRestante -= 1000
        } else if (tiempoRestante <= 0) {
            corriendo = false
        }
    }

    LaunchedEffect(Unit) {
        if (index >= 0) {
            val rutina = viewModel.lista[index]

            nombre = rutina.nombre
            inicio = rutina.inicio
            fin = rutina.fin
            diasSeleccionados = rutina.dias.toSet()
        }
    }

    fun abrirHora(tipo: String) {
        val cal = Calendar.getInstance()

        TimePickerDialog(
            context,
            { _, h, m ->
                val c = Calendar.getInstance()
                c.set(Calendar.HOUR_OF_DAY, h)
                c.set(Calendar.MINUTE, m)

                if (tipo == "inicio") inicio = c.timeInMillis
                else fin = c.timeInMillis
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF))
    ) {

        // 🔴 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF8f1414), Color(0xFFc41e1e))
                    )
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = if (index == -1) "CREAR RUTINA" else "EDITAR ACTIVIDAD",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                //  BOTÓN PLAY / PAUSA
                IconButton(
                    onClick = {

                        if (!corriendo) {
                            tiempoRestante = fin - inicio
                            corriendo = true
                        } else {
                            corriendo = false
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (corriendo)
                            Icons.Default.Pause
                        else
                            Icons.Default.PlayArrow,
                        contentDescription = "Iniciar",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            //MOSTRAR TIEMPO
            if (corriendo || tiempoRestante > 0) {
                Text(
                    text = "Tiempo: ${tiempoRestante / 1000}s",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 HORA INICIO
            Button(
                onClick = { abrirHora("inicio") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar hora inicio")
            }

            Text("Inicio: ${formato.format(Date(inicio))}")

            Spacer(modifier = Modifier.height(10.dp))

            // 🔹 HORA FIN
            Button(
                onClick = { abrirHora("fin") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar hora fin")
            }

            Text("Fin: ${formato.format(Date(fin))}")

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 DÍAS
            Text("¿REPETIR?", fontWeight = FontWeight.Bold)

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                diasSemana.forEach { dia ->
                    FilterChip(
                        selected = diasSeleccionados.contains(dia),
                        onClick = {
                            diasSeleccionados =
                                if (diasSeleccionados.contains(dia))
                                    diasSeleccionados - dia
                                else
                                    diasSeleccionados + dia
                        },
                        label = { Text(dia) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 IR A ACTIVIDADES
            Button(
                onClick = {
                    navController.navigate("agregarActividad?index=-1")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar actividades a esta rutina")
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔥 GUARDAR
            Button(
                onClick = {

                    if (nombre.isNotBlank()) {

                        if (index == -1) {

                            viewModel.agregar(
                                nombre,
                                diasSeleccionados.toList(),
                                inicio,
                                fin
                            )

                        } else {

                            val rutinaActual = viewModel.lista[index]

                            val nuevaRutina = rutinaActual.copy(
                                nombre = nombre,
                                dias = diasSeleccionados.toList(),
                                inicio = inicio,
                                fin = fin
                            )

                            viewModel.lista[index] = nuevaRutina
                        }

                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8f1414)
                )
            ) {
                Text("Guardar Rutina", color = Color.White)
            }
        }
    }
}