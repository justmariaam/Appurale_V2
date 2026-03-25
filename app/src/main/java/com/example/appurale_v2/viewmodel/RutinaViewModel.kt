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

    fun agregarActividadARutina(rutinaId: Int, actividad: Actividad) {
        val index = lista.indexOfFirst { it.id == rutinaId }

        if (index != -1) {
            val rutinaActual = lista[index]

            val nuevaRutina = rutinaActual.copy(
                actividades = rutinaActual.actividades + actividad
            )

            lista[index] = nuevaRutina
        }
    }
}