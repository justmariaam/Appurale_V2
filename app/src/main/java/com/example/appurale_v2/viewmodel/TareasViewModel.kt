package com.example.appurale_v2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Tarea(
    val nombre: String,
    val inicio: Long,
    val fin: Long,
    val intervaloVibracion: Long
)

class TareasViewModel : ViewModel() {

    val tareas = mutableStateListOf<Tarea>()

    fun agregarTarea(
        nombre: String,
        inicio: Long,
        fin: Long,
        intervalo: Long
    ) {

        tareas.add(
            Tarea(
                nombre,
                inicio,
                fin,
                intervalo
            )
        )
    }

    fun actualizarTarea(
        index: Int,
        nombre: String,
        inicio: Long,
        fin: Long,
        intervalo: Long
    ) {

        if (index in tareas.indices) {

            tareas[index] = Tarea(
                nombre,
                inicio,
                fin,
                intervalo
            )
        }
    }

    fun obtenerTarea(index: Int): Tarea? {

        return if (index in tareas.indices) {
            tareas[index]
        } else {
            null
        }
    }

    fun tareaValida(index: Int): Boolean {

        return index in tareas.indices
    }
}
