package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_crear_cliente.*

class CrearCliente : AppCompatActivity() {

    private lateinit var dni:String
    private lateinit var nombre:String
    private lateinit var apellidos:String
    private lateinit var telefono:String
    private lateinit var localidad:String
    private lateinit var domicilio:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cliente)

        btnCrearCliente.setOnClickListener {
            if(obtenerDatos()){
                //CrearCliente
            }
        }
    }

    private fun obtenerDatos(): Boolean {
        var correcto=true

        if(txtCrearDniCliente.text.isNotEmpty()) dni=txtCrearDniCliente.text.toString().trim() else correcto=false; pintarError(txtCrearDniCliente)
        if(txtCrearNombreCliente.text.isNotEmpty()) nombre=txtCrearNombreCliente.text.toString().trim() else correcto=false; pintarError(txtCrearNombreCliente)
        if(txtCrearApellidosCliente.text.isNotEmpty()) apellidos=txtCrearApellidosCliente.text.toString().trim() else correcto=false; pintarError(txtCrearApellidosCliente)
        if(txtCrearTlfCliente.text.isNotEmpty()) telefono=txtCrearTlfCliente.text.toString().trim() else correcto=false; pintarError(txtCrearTlfCliente)
        if(txtCrearLocalidadCliente.text.isNotEmpty()) localidad=txtCrearLocalidadCliente.text.toString().trim() else correcto=false; pintarError(txtCrearLocalidadCliente)
        if(txtCrearDomicilioCliente.text.isNotEmpty()) domicilio=txtCrearDomicilioCliente.text.toString().trim() else correcto=false; pintarError(txtCrearDomicilioCliente)

        return correcto
    }

    private fun pintarError(txtError: TextView?) {
        txtError!!.visibility= View.VISIBLE
    }
}