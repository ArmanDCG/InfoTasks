package com.example.infotasks.Utiles

import android.util.Log
import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Constantes.TipoTarea
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Modelo.Usuario
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


import com.google.type.LatLngOrBuilder

object ConversorQueryAModelo {

    /**
     * Metodo para convertir una Query de un usuario a un modelo Usuario
     */
    fun queryAUsuario(dato: QueryDocumentSnapshot?): Usuario {
        return Usuario(
            dato!!.get("mail").toString(),
            dato.get("nombre").toString(),
            dato.get("apellidos").toString(),
            RolUsuario.valueOf(dato.get("rol").toString()),
            dato.get("activo") as Boolean
        )
    }

    fun queryATareas(dato: QuerySnapshot?):ArrayList<Tarea> {
        var tareas= arrayListOf<Tarea>()
        for(dc: DocumentChange in dato?.documentChanges!!) {
            if (dc.type == DocumentChange.Type.ADDED) {
                Log.e("Nueva tarea", dc.document.toString())
                tareas.add(
                    Tarea(
                        dc.document.get("id").toString(),
                        dc.document.get("descripcion").toString(),
                        TipoTarea.valueOf(dc.document.get("tipo").toString()),
                        PrioridadTarea.valueOf(dc.document.get("prioridad").toString()),
                        EstadoTarea.valueOf(dc.document.get("estado").toString()),
                        dc.document.get("fecha").toString(),
                        dc.document.get("hora").toString(),
                        dc.document.get("observaciones").toString(),
                        dc.document.get("idCliente").toString()

                    )
                )
                Log.e("Tarea desglosada", tareas.last().toString())

                 
            }
        }
        return tareas

    }

     fun queryACliente(dato: QueryDocumentSnapshot?): Cliente? {
        return Cliente(
            dato!!["dni"].toString(),
            dato["nombre"].toString(),
            dato["apellidos"].toString(),
            dato["telefono"].toString(),
            dato["direccion"].toString(),
            estraerLatLng(dato["latlng"] as HashMap<String, Double>),

        )
    }

    private fun estraerLatLng(hmd: HashMap<String, Double>): LatLng? {
        return LatLng(hmd["latitude"]!!.toDouble(), hmd["latitude"]!!.toDouble())
    }
}