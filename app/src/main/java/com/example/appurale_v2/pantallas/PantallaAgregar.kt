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
import com.example.appurale_v2.viewmodel.TareasViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregar(
    navController: NavController,
    index: Int,
    viewModel: TareasViewModel
) {

    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }

    var inicio by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    var fin by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    var intervalo by remember { mutableStateOf(5L) }

    var tipoDuracion by remember { mutableStateOf("INICIO") }
    var expandDuracion by remember { mutableStateOf(false) }

    var tipoAlarma by remember { mutableStateOf("ComboBox") }
    var expandAlarma by remember { mutableStateOf(false) }

    var descansos by remember { mutableStateOf(false) }

    var duracionExtra by remember { mutableStateOf("") }

    val formato = SimpleDateFormat("HH:mm", Locale.getDefault())

    val opcionesIntervalo = listOf(3L, 5L, 10L, 15L)
    val opcionesDuracion = listOf("INICIO", "FIN")
    val opcionesAlarma = listOf("ComboBox", "Vibración", "Sonido")

    var expandIntervalo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        if (index >= 0) {

            val t = viewModel.tareas[index]

            nombre = t.nombre
            inicio = t.inicio
            fin = t.fin
            intervalo = t.intervaloVibracion
        }
    }

    fun abrirHoraInicio() {

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

    fun abrirHoraFin() {

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

        Text(
            "AGREGAR ACTIVIDAD",
            style = MaterialTheme.typography.headlineMedium
        )

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
                value = tipoDuracion,
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
                            tipoDuracion = it
                            expandDuracion = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expandAlarma,
            onExpandedChange = { expandAlarma = !expandAlarma }
        ) {

            OutlinedTextField(
                value = tipoAlarma,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de alarma") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandAlarma,
                onDismissRequest = { expandAlarma = false }
            ) {

                opcionesAlarma.forEach {

                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            tipoAlarma = it
                            expandAlarma = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        ExposedDropdownMenuBox(
            expanded = expandIntervalo,
            onExpandedChange = {
                expandIntervalo = !expandIntervalo
            }
        ) {

            OutlinedTextField(
                value = "$intervalo minutos",
                onValueChange = {},
                readOnly = true,
                label = { Text("ESTABLECER INTERVALOS") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandIntervalo,
                onDismissRequest = {
                    expandIntervalo = false
                }
            ) {

                opcionesIntervalo.forEach {

                    DropdownMenuItem(
                        text = {
                            Text("$it minutos")
                        },
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
                onCheckedChange = {
                    descansos = it
                }
            )

            Text("Descansos intermedios")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = duracionExtra,
            onValueChange = { duracionExtra = it },
            label = { Text("DURACIÓN") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {

                if (index == -1) {

                    viewModel.agregarTarea(
                        nombre,
                        inicio,
                        fin,
                        intervalo
                    )

                } else {

                    viewModel.actualizarTarea(
                        index,
                        nombre,
                        inicio,
                        fin,
                        intervalo
                    )
                }

                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

    }
}
