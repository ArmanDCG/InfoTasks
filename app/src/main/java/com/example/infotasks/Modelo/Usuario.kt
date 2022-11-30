package com.example.infotasks.Modelo

import com.example.infotasks.Constantes.RolUsuario
import java.io.Serializable

data class Usuario(
    var mail: String? = null, //ID
    var nombre: String? = null,
    var apellidos: String? = null,
    var rol: RolUsuario? = null, //Acministrador, Tecnico
    var activo: Boolean = false
    ):Serializable
{
    override fun toString(): String {
        return "Mail:${mail}, Nombre:${nombre}, Apellidos:${apellidos}, Rol:${rol}, Activo:${activo}"
    }
}
