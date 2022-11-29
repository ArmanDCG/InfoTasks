package com.example.infotasks.Modelo

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable


data class Cliente(
    var dni: String? = null,
    var nombre: String? = null,
    var apellidos: String? = null,
    var telefono: String? = null,
    var localidad: String? = null,
    var domicilio: String? = null


    ):Serializable
{
    override fun toString(): String {
        return "ID:${dni}, Nombre:${nombre}, Apellidos:${apellidos}, Telefono:${telefono}, Dirección:${"$localidad, $domicilio"}"
    }
}
