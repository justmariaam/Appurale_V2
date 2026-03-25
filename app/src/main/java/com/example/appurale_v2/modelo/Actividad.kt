package com.example.appurale_v2.modelo

data class Actividad(
    val id: Int,
    val nombre: String,
    val duracion: Int,
    val intervalo: Int,
    val inicio: Long,
    val fin: Long
)
