package com.example.infotasks.Modelo

import java.util.*

data class Tarea(
    var id: String? = null,
    var descripcion: String? = null,
    var tipo: String? = null, //Instalación, Incidencia
    var prioridad: String = "Normal", //Baja,Normal,Alta
    var estado: String = "Pendiente", //Pendiente,Pospuesta,Realizada
    var fecha: Date? = null,
    var observaciones: String? = null,
    var cliente: Cliente? = null

    )

{
    override fun toString(): String {
        return "ID:${id}, Descripción:${descripcion}, Tipo:${tipo}, Prioridad:${prioridad}, Estado:${estado}, Fecha:${fecha}, Observaciones:${observaciones}"
    }
}
