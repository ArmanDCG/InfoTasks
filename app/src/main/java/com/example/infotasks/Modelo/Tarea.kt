package com.example.infotasks.Modelo

import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.TipoTarea
import java.io.Serializable
import java.util.*

data class Tarea(
    var id: String? = null,
    var descripcion: String? = null,
    var tipo: TipoTarea? = null, //Instalación, Incidencia
    var prioridad: PrioridadTarea? = null, //Baja,Media,Alta
    var estado: EstadoTarea = EstadoTarea.PENDIENTE, //Pendiente,Realizada
    var observaciones: String? = null,
    var fechaHoraCreacion: String? = null,
    var fechaHoraUltimaMod: String? = null,
    var idCliente: String? = null,
    var idUsuario: String? = null

    ):Serializable

{
    //Por defecto



    override fun toString(): String {
        return "ID:${id}, Descripción:${descripcion}, Tipo:${tipo}, Prioridad:${prioridad}, Estado:${estado}, Fecha/Hora Creación:${fechaHoraCreacion}, Fecha/Hora Última Modificación:${fechaHoraUltimaMod} Observaciones:${observaciones}"
    }
}
