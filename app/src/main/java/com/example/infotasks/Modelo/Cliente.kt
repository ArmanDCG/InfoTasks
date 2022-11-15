package com.example.infotasks.Modelo

import com.google.type.LatLng

data class Cliente(
    var nombre: String? = null,
    var apellidos: String? = null,
    var telefono: String? = null,
    var dirección: LatLng? = null,

)
