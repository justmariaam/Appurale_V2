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
import com.example.appurale_v2.viewmodel.TareasViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregar(
    navController: NavController,
    index: Int,
    viewModel: RutinaViewModel
) {

    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }

    var inicio by remember { mutableStateOf(System.currentTimeMillis()) }
    var fin by remember { mutableStateOf(System.currentTimeMillis()) }

    var intervalo by remember { mutableStateOf(5L) }

    var modoDuracion by remember { mutableStateOf("INICIO/FIN") }
    var expandDuracion by remember { mutableStateOf(false) }

    var tipoAlarma by remember { mutableStateOf("ComboBox") }
    var expandAlarma by remember { mutableStateOf(false) }

    var descansos by remember { mutableStateOf(false) }

    var duracionDescanso by remember { mutableStateOf("") }

    val opcionesDuracion = listOf("INICIO/FIN", "TIEMPO")
    val opcionesIntervalo = listOf(3L, 5L, 10L, 15L)
    val opcionesTiempo = listOf(30, 60, 90, 120)

    var expandIntervalo by remember { mutableStateOf(false) }
    var expandTiempo by remember { mutableStateOf(false) }

    var tiempoSeleccionado by remember { mutableStateOf(60) }

    val formato = SimpleDateFormat("HH:mm", Locale.getDefault())

    fun seleccionarHoraInicio() {

        val cal = Calendar.getInstance()

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

        val cal = Calendar.getInstance()

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

        Text("AGREGAR ACTIVIDAD", style = MaterialTheme.typography.headlineMedium)

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
                label = { Text("DURACIÓN") },
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
                    label = { Text("Tiempo") },
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
                label = { Text("ESTABLECER INTERVALOS") },
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

        Spacer(modifier = Modifier.height(10.dp))

        Row {

            Checkbox(
                checked = descansos,
                onCheckedChange = { descansos = it }
            )

            Text("Descansos intermedios")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = duracionDescanso,
            onValueChange = { duracionDescanso = it },
            enabled = descansos,
            label = { Text("DURACIÓN DESCANSO") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {

                val inicioFinal: Long
                val finFinal: Long

                if (modoDuracion == "TIEMPO") {
                    inicioFinal = System.currentTimeMillis()
                    finFinal = inicioFinal + tiempoSeleccionado * 60000
                } else {
                    inicioFinal = inicio
                    finFinal = fin
                }

                val actividad = Actividad(
                    id = System.currentTimeMillis().toInt(),
                    nombre = nombre,
                    duracion = tiempoSeleccionado,
                    intervalo = intervalo.toInt(),
                    inicio = inicioFinal,
                    fin = finFinal
                )

                viewModel.agregarActividadARutina(index, actividad)

                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

    }
}
