package com.example.infotasks.Utiles

import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Modelo.Usuario
import com.google.firebase.firestore.QueryDocumentSnapshot

object ConversorQueryAModelo {

    /**
     * Metodo para convertir una Query de un usuario a un modelo Usuario
     */
    fun queryAUsuario(dato: QueryDocumentSnapshot?): Usuario {
        return Usuario(
            dato!!.get("mail").toString(),
            dato.get("nombre").toString(),
            dato.get("apellidos").toString(),
            dato.get("rol") as RolUsuario,
            dato.get("activo") as Boolean
        )
    }
}