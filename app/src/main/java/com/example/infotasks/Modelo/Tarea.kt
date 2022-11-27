package com.example.infotasks.Modelo

import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.TipoTarea
import java.util.*

data class Tarea(
    var id: String? = null,
    var descripcion: String? = null,
    var tipo: TipoTarea? = null, //Instalación, Incidencia
    var prioridad: PrioridadTarea = PrioridadTarea.MEDIA, //Baja,Media,Alta
    var estado: EstadoTarea = EstadoTarea.PENDIENTE, //Pendiente,Pospuesta,Realizada
    var fecha: String? = null,
    var hora: String? = null,
    var observaciones: String? = null,
    var idCliente: String? = null

    )

{
    override fun toString(): String {
        return "ID:${id}, Descripción:${descripcion}, Tipo:${tipo}, Prioridad:${prioridad}, Estado:${estado}, Fecha:${fecha}, Observaciones:${observaciones}"
    }
}
