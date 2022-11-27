package com.example.infotasks.Modelo

import com.google.android.gms.maps.model.LatLng


data class Cliente(
    var dni: String? = null,
    var nombre: String? = null,
    var apellidos: String? = null,
    var telefono: String? = null,
    var direccion: String? = null,
    var latlng: LatLng? = null

    )
{
    override fun toString(): String {
        return "ID:${dni}, Nombre:${nombre}, Apellidos:${apellidos}, Telefono:${telefono}, Dirección:${direccion}, LatLng:${latlng}"
    }
}
