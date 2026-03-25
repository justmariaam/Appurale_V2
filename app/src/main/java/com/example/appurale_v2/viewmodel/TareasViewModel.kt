package com.example.appurale_v2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.appurale_v2.modelo.Actividad

class TareasViewModel : ViewModel() {

    val tareas = mutableStateListOf<Actividad>()

    private var contadorId = 0

    fun agregarTarea(
        nombre: String,
        inicio: Long,
        fin: Long,
        intervalo: Long
    ) {

        val duracion = ((fin - inicio) / 60000).toInt()

        tareas.add(
            Actividad(
                id = contadorId++,
                nombre = nombre,
                duracion = duracion,
                intervalo = intervalo.toInt(),
                inicio = inicio,
                fin = fin
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

            val duracion = ((fin - inicio) / 60000).toInt()

            tareas[index] = Actividad(
                id = tareas[index].id,
                nombre = nombre,
                duracion = duracion,
                intervalo = intervalo.toInt(),
                inicio = inicio,
                fin = fin
            )
        }
    }

    fun obtenerTarea(index: Int): Actividad? {

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
