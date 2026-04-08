package com.example.appurale_v2.pantallas

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appurale_v2.modelo.Actividad
import com.example.appurale_v2.viewmodel.RutinaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEditarActividad(
    navController: NavController,
    rutinaViewModel: RutinaViewModel,
    rutinaIndex: Int,
    actividadIndex: Int
) {

    val context = LocalContext.current
    val rutina = rutinaViewModel.lista.getOrNull(rutinaIndex)
    val actividad = rutina?.actividades?.getOrNull(actividadIndex)

    if (rutina == null || actividad == null) {
        // Si no existe, regresar
        androidx.compose.runtime.LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    var nombre by remember { mutableStateOf(actividad.nombre) }
    var inicio by remember { mutableStateOf(actividad.inicio) }
    var fin by remember { mutableStateOf(actividad.fin) }
    var intervalo by remember { mutableStateOf(actividad.intervalo.toLong()) }

    var modoDuracion by remember { mutableStateOf(if (actividad.duracion > 0) "TIEMPO" else "INICIO/FIN") }
    var expandDuracion by remember { mutableStateOf(false) }

    var tiempoSeleccionado by remember { mutableStateOf(actividad.duracion) }

    val opcionesDuracion = listOf("INICIO/FIN", "TIEMPO")
    val opcionesIntervalo = listOf(3L, 5L, 10L, 15L)
    val opcionesTiempo = listOf(30, 60, 90, 120)

    var expandIntervalo by remember { mutableStateOf(false) }
    var expandTiempo by remember { mutableStateOf(false) }

    val formato = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun seleccionarHoraInicio() {
        val cal = Calendar.getInstance().apply { timeInMillis = inicio }
        TimePickerDialog(
            context,
            { _, h, m ->
                val c = Calendar.getInstance()
                c.set(Calendar.HOUR_OF_DAY, h)
                c.set(Calendar.MINUTE, m)
                inicio = c.timeInMillis
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun seleccionarHoraFin() {
        val cal = Calendar.getInstance().apply { timeInMillis = fin }
        TimePickerDialog(
            context,
            { _, h, m ->
                val c = Calendar.getInstance()
                c.set(Calendar.HOUR_OF_DAY, h)
                c.set(Calendar.MINUTE, m)
                fin = c.timeInMillis
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
            .padding(20.dp)
    ) {

        Text("EDITAR ACTIVIDAD", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("NOMBRE") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expandDuracion,
            onExpandedChange = { expandDuracion = !expandDuracion }
        ) {
            OutlinedTextField(
                value = modoDuracion,
                onValueChange = {},
                readOnly = true,
                label = { Text("TIPO DE DURACIÓN") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandDuracion,
                onDismissRequest = { expandDuracion = false }
            ) {
                opcionesDuracion.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            modoDuracion = it
                            expandDuracion = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (modoDuracion == "INICIO/FIN") {
            Button(
                onClick = { seleccionarHoraInicio() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Inicio: ${formato.format(Date(inicio))}")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = { seleccionarHoraFin() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fin: ${formato.format(Date(fin))}")
            }
        }

        if (modoDuracion == "TIEMPO") {
            ExposedDropdownMenuBox(
                expanded = expandTiempo,
                onExpandedChange = { expandTiempo = !expandTiempo }
            ) {
                OutlinedTextField(
                    value = "$tiempoSeleccionado minutos",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Duración") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandTiempo,
                    onDismissRequest = { expandTiempo = false }
                ) {
                    opcionesTiempo.forEach {
                        DropdownMenuItem(
                            text = { Text("$it minutos") },
                            onClick = {
                                tiempoSeleccionado = it
                                expandTiempo = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expandIntervalo,
            onExpandedChange = { expandIntervalo = !expandIntervalo }
        ) {
            OutlinedTextField(
                value = "$intervalo minutos",
                onValueChange = {},
                readOnly = true,
                label = { Text("INTERVALO") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandIntervalo,
                onDismissRequest = { expandIntervalo = false }
            ) {
                opcionesIntervalo.forEach {
                    DropdownMenuItem(
                        text = { Text("$it minutos") },
                        onClick = {
                            intervalo = it
                            expandIntervalo = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val inicioFinal: Long
                    val finFinal: Long

                    if (modoDuracion == "TIEMPO") {
                        inicioFinal = actividad.inicio // Mantener el inicio original
                        finFinal = inicioFinal + tiempoSeleccionado * 60000L
                    } else {
                        inicioFinal = inicio
                        finFinal = fin
                    }

                    val actividadEditada = Actividad(
                        id = actividad.id,
                        nombre = nombre,
                        duracion = if (modoDuracion == "TIEMPO") tiempoSeleccionado else ((finFinal - inicioFinal) / 60000).toInt(),
                        intervalo = intervalo.toInt(),
                        inicio = inicioFinal,
                        fin = finFinal
                    )

                    // Actualizar la actividad en la rutina
                    val nuevasActividades = rutina.actividades.toMutableList()
                    nuevasActividades[actividadIndex] = actividadEditada
                    val rutinaActualizada = rutina.copy(actividades = nuevasActividades)
                    rutinaViewModel.lista[rutinaIndex] = rutinaActualizada

                    navController.popBackStack()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Guardar cambios", color = Color.White)
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("Cancelar", color = Color.White)
            }
        }
    }
}