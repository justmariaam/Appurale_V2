package com.example.appurale_v2.modelo

data class Rutina(
    val id: Int,
    val nombre: String,
    val actividades: List<Actividad>,
    val dias: List<String>,
    val inicio: Long,
    val fin: Long
)