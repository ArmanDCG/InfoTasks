package com.example.infotasks.Modelo

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.TipoTarea
import com.example.infotasks.Utiles.FechaHora.dateToString
import java.io.Serializable
import java.util.*


data class Tarea(
    var id: String? = null,
    var descripcion: String? = null,
    var tipo: TipoTarea? = null, //Instalación, Incidencia
    var prioridad: PrioridadTarea? = null, //Baja,Media,Alta
    var estado: EstadoTarea = EstadoTarea.PENDIENTE, //Pendiente,Realizada
    var observaciones: String? = null,
    var fechaCreacion: Date? = null,
    var fechaUltimaMod: Date? = null,
    var idCliente: String? = null
    ):Serializable

{

    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return "ID:${id}, Descripción:${descripcion}, Tipo:${tipo}, Prioridad:${prioridad}, Estado:${estado}, Fecha/Hora Creación:${dateToString(fechaCreacion!!) }, Fecha/Hora Última Modificación:${dateToString(fechaUltimaMod!!)} Observaciones:${observaciones}, IdCliente:${idCliente}"
    }
}
