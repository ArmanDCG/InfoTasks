package com.example.infotasks.Utiles

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.google.firebase.firestore.core.Query


import com.google.type.LatLngOrBuilder
import java.util.*
import kotlin.collections.ArrayList

object ConversorQueryAModelo {

    /**
     * Metodo para convertir una Query de un usuario a un modelo Usuario
     */
    fun queryAUsuario(dato: QueryDocumentSnapshot?): Usuario {

        return Usuario(
            dato!!.get("mail").toString(),
            dato.get("nombre").toString(),
            dato.get("apellidos").toString(),
            dato.get("telefono").toString().toInt(),
            RolUsuario.valueOf(dato.get("rol").toString()),
            dato.get("activo") as Boolean
        )
    }
    fun queryAUsuarios(dato:QuerySnapshot?):ArrayList<Usuario>{
        var usuarios= arrayListOf<Usuario>()
        for (dc:DocumentChange in dato?.documentChanges!!){
            if (dc.type == DocumentChange.Type.ADDED){
                usuarios.add(queryAUsuario(dc.document))
            }
        }
        return usuarios
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun queryATareas(dato: QuerySnapshot?):ArrayList<Tarea> {
        var tareas= arrayListOf<Tarea>()
        for(dc: DocumentChange in dato?.documentChanges!!) {
            if (dc.type == DocumentChange.Type.ADDED) {
                tareas.add(
                    Tarea(
                        dc.document.get("id").toString(),
                        dc.document.get("descripcion").toString(),
                        TipoTarea.valueOf(dc.document.get("tipo").toString()),
                        PrioridadTarea.valueOf(dc.document.get("prioridad").toString()),
                        EstadoTarea.valueOf(dc.document.get("estado").toString()),
                        dc.document.get("observaciones").toString(),
                        dc.document.getTimestamp("fechaCreacion")!!.toDate(),
                        dc.document.getTimestamp("fechaUltimaMod")!!.toDate(),
                        dc.document.get("idCliente").toString(),
                    )
                )


                 
            }
        }
        return tareas
    }

    fun queryAClientes(dato: QuerySnapshot?): ArrayList<Cliente> {
        var clientes = arrayListOf<Cliente>()
        for (dc:DocumentChange in dato?.documentChanges!!){
            if (dc.type==DocumentChange.Type.ADDED){
                clientes.add(queryACliente(dc.document)!!)
            }
        }
        return clientes
    }

     fun queryACliente(dato: QueryDocumentSnapshot?): Cliente? {
        return Cliente(
            dato!!["dni"].toString(),
            dato["nombre"].toString(),
            dato["apellidos"].toString(),
            dato["telefono"].toString().toInt(),
            dato["localidad"].toString(),
            dato["domicilio"].toString(),
        )
    }




}