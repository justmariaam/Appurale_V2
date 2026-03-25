package com.example.appurale_v2.pantallas

import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appurale_v2.viewmodel.RutinaViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PantallaRutinas(
    navController: NavController,
    viewModel: RutinaViewModel
) {

    val formato = SimpleDateFormat("HH:mm", Locale.getDefault())

    var rutinaActiva by remember { mutableStateOf<Int?>(null) }
    var tiempoRestante by remember { mutableStateOf(0L) }

    // 🔥 TEMPORIZADOR
    LaunchedEffect(tiempoRestante) {
        if (tiempoRestante > 0) {
            delay(1000)
            tiempoRestante -= 1000
        } else {
            rutinaActiva = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column {

            // HEADER
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
                Text(
                    "RUTINAS",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                itemsIndexed(viewModel.lista) { index, rutina ->

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFfcac03)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column(Modifier.padding(16.dp)) {

                            Text(
                                rutina.nombre,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Días: ${rutina.dias.joinToString()}",
                                color = Color.White
                            )

                            Text(
                                "Inicio: ${formato.format(Date(rutina.inicio))}",
                                color = Color.White
                            )

                            Text(
                                "Fin: ${formato.format(Date(rutina.fin))}",
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                // 🔥 INICIAR
                                Button(
                                    onClick = {
                                        rutinaActiva = index
                                        tiempoRestante = rutina.fin - rutina.inicio
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White
                                    )
                                ) {
                                    Text("Iniciar", color = Color.Black)
                                }

                                // 🔥 EDITAR
                                IconButton(
                                    onClick = {
                                        navController.navigate("crearRutina?index=$index")
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar",
                                        tint = Color.White
                                    )
                                }
                            }

                            // 🔥 TEMPORIZADOR
                            if (rutinaActiva == index) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Tiempo: ${tiempoRestante / 1000}s",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate("crearRutina?index=-1")
            },
            containerColor = Color(0xFF8f1414),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
        }
    }
}