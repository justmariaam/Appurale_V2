package com.example.appurale_v2.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.appurale_v2.modelo.Rutina
import com.example.appurale_v2.modelo.Actividad

class RutinaViewModel : ViewModel() {

    var lista = mutableStateListOf<Rutina>()
        private set

    fun agregar(
        nombre: String,
        dias: List<String>,
        inicio: Long,
        fin: Long
    ) {
        lista.add(
            Rutina(
                id = lista.size + 1,
                nombre = nombre,
                actividades = emptyList(),
                dias = dias,
                inicio = inicio,
                fin = fin
            )
        )
    }

    fun agregarActividadARutina(index: Int, actividad: Actividad) {
        if (index in lista.indices) {
            val rutinaActual = lista[index]
            val nuevasActividades = rutinaActual.actividades + actividad
            val nuevaRutina = rutinaActual.copy(
                actividades = nuevasActividades
            )
            lista[index] = nuevaRutina
        }
    }

    fun eliminarActividadDeRutina(rutinaIndex: Int, actividadIndex: Int) {
        if (rutinaIndex in lista.indices) {
            val rutinaActual = lista[rutinaIndex]
            if (actividadIndex in rutinaActual.actividades.indices) {
                val nuevasActividades = rutinaActual.actividades.toMutableList()
                nuevasActividades.removeAt(actividadIndex)
                val rutinaActualizada = rutinaActual.copy(actividades = nuevasActividades)
                lista[rutinaIndex] = rutinaActualizada
            }
        }
    }

    fun actualizarActividadEnRutina(rutinaIndex: Int, actividadIndex: Int, nuevaActividad: Actividad) {
        if (rutinaIndex in lista.indices) {
            val rutinaActual = lista[rutinaIndex]
            if (actividadIndex in rutinaActual.actividades.indices) {
                val nuevasActividades = rutinaActual.actividades.toMutableList()
                nuevasActividades[actividadIndex] = nuevaActividad
                val rutinaActualizada = rutinaActual.copy(actividades = nuevasActividades)
                lista[rutinaIndex] = rutinaActualizada
            }
        }
    }
}