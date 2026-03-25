package com.example.appurale_v2.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RutinaCard(
    nombre: String,
    dias: List<String>
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFfcac03)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = nombre,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {

                dias.forEach { dia ->

                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFF8f1414),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = dia,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}