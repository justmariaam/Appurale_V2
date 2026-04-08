package com.example.appurale_v2.pantallas

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appurale_v2.modelo.Rutina
import com.example.appurale_v2.viewmodel.RutinaViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRutina(
    rutina: Rutina,
    navController: NavController,
    index: Int
) {

    val contexto = LocalContext.current
    val formato = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Estado para la actividad seleccionada
    var actividadSeleccionada by remember { mutableStateOf<Int?>(null) }

    // Estado para el temporizador de la actividad
    var actividadActiva by remember { mutableStateOf<Int?>(null) }
    var tiempoRestante by remember { mutableStateOf(0L) }
    var tiempoTranscurrido by remember { mutableStateOf(0L) }
    var temporizadorActivo by remember { mutableStateOf(false) }

    // Mostrar diálogo de confirmación
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var actividadAEliminar by remember { mutableStateOf<Int?>(null) }

    // Efecto del temporizador
    LaunchedEffect(temporizadorActivo, tiempoRestante) {
        if (temporizadorActivo && tiempoRestante > 0) {
            delay(1000)
            tiempoRestante -= 1000
            tiempoTranscurrido += 1000
        } else if (tiempoRestante <= 0 && temporizadorActivo) {
            temporizadorActivo = false
            actividadActiva = null
            // Mostrar notificación de actividad completada
            android.widget.Toast.makeText(
                contexto,
                "¡Actividad completada!",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Diálogo de confirmación para eliminar
    if (mostrarDialogoEliminar && actividadAEliminar != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar actividad") },
            text = { Text("¿Estás seguro de que quieres eliminar esta actividad?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        actividadAEliminar?.let { idx ->
                            // Eliminar actividad - aquí necesitas acceder al ViewModel
                            // Por ahora mostramos un Toast
                            android.widget.Toast.makeText(
                                contexto,
                                "Actividad eliminada",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                        mostrarDialogoEliminar = false
                        actividadAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(rutina.nombre)
                        if (actividadActiva != null) {
                            Text(
                                text = "Actividad en progreso...",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8f1414),
                    titleContentColor = Color.White
                ),
                actions = {
                    // Botón para detener todo
                    if (actividadActiva != null) {
                        IconButton(
                            onClick = {
                                temporizadorActivo = false
                                actividadActiva = null
                                tiempoRestante = 0
                                tiempoTranscurrido = 0
                                android.widget.Toast.makeText(
                                    contexto,
                                    "Actividad detenida",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = "Detener todo", tint = Color.White)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("agregarActividad?index=$index")
                },
                containerColor = Color(0xFF8f1414)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar actividad", tint = Color.White)
            }
        }
    ) { paddingValues ->

        if (rutina.actividades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.FitnessCenter,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay actividades aún",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                    Text(
                        text = "Presiona el botón + para agregar",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rutina.actividades.size) { idx ->
                    val actividad = rutina.actividades[idx]
                    val isSelected = actividadSeleccionada == idx
                    val isActive = actividadActiva == idx

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF8f1414) else Color(0xFFfcac03)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Cabecera de la actividad
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Checkbox para seleccionar
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = {
                                            actividadSeleccionada = if (isSelected) null else idx
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color.White,
                                            uncheckedColor = Color.White.copy(alpha = 0.7f)
                                        )
                                    )

                                    Column(modifier = Modifier.padding(start = 8.dp)) {
                                        Text(
                                            text = actividad.nombre,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Duración: ${actividad.duracion} min | Intervalo: ${actividad.intervalo} min",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                        Text(
                                            text = "Horario: ${formato.format(Date(actividad.inicio))} - ${formato.format(Date(actividad.fin))}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.9f)
                                        )
                                    }
                                }

                                // Botones de acción rápidos
                                if (isActive) {
                                    Row {
                                        IconButton(
                                            onClick = {
                                                temporizadorActivo = !temporizadorActivo
                                            }
                                        ) {
                                            Icon(
                                                if (temporizadorActivo) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = if (temporizadorActivo) "Pausar" else "Reanudar",
                                                tint = Color.White
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                temporizadorActivo = false
                                                actividadActiva = null
                                                tiempoRestante = 0
                                                tiempoTranscurrido = 0
                                            }
                                        ) {
                                            Icon(Icons.Default.Stop, contentDescription = "Detener", tint = Color.White)
                                        }
                                    }
                                }
                            }

                            // Mostrar progreso si está activa
                            if (isActive) {
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = (tiempoTranscurrido.toFloat() / (actividad.duracion * 60000).toFloat()),
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color.White,
                                    trackColor = Color.White.copy(alpha = 0.3f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Tiempo restante: ${tiempoRestante / 60000}:${(tiempoRestante % 60000) / 1000}",
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                            }

                            // Mostrar botones de edición/eliminación cuando está seleccionada
                            if (isSelected) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Divider(color = Color.White.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Botón Editar
                                    Button(
                                        onClick = {
                                            // Navegar a editar actividad
                                            navController.navigate("editarActividad?rutinaIndex=$index&actividadIndex=$idx")
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF2196F3)
                                        )
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Editar")
                                    }

                                    // Botón Iniciar
                                    Button(
                                        onClick = {
                                            actividadActiva = idx
                                            tiempoRestante = actividad.duracion * 60000L
                                            tiempoTranscurrido = 0
                                            temporizadorActivo = true
                                            actividadSeleccionada = null
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4CAF50)
                                        )
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Iniciar")
                                    }

                                    // Botón Eliminar
                                    Button(
                                        onClick = {
                                            actividadAEliminar = idx
                                            mostrarDialogoEliminar = true
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFF44336)
                                        )
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}