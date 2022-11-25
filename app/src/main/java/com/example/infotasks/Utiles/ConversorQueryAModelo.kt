package com.example.infotasks.Utiles

import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Modelo.Usuario
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

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

    fun queryATareas(dato: QuerySnapshot?) {
        for(dc: DocumentChange in dato?.documentChanges!!) {
            if (dc.type == DocumentChange.Type.ADDED) {

            }
        }
        arrayListOf<Tarea>()
    }
}