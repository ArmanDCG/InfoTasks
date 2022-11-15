package com.example.infotasks.Modelo

data class Usuario(
    var identificador: String? = null,
    var nombre: String? = null,
    var apellidos: String? = null,
    var rol: String? = null, //Acministrador, Tecnico
    var activo: Boolean = false)
{
    override fun toString(): String {
        return "ID:${identificador}, Nombre:${nombre}, Apellidos:${apellidos}, Rol:${rol}, Activo:${activo}"
    }
}
