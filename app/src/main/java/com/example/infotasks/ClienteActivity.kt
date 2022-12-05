package com.example.infotasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.infotasks.Modelo.Cliente
import kotlinx.android.synthetic.main.activity_cliente.*
import kotlinx.android.synthetic.main.activity_tarea.*
import kotlinx.android.synthetic.main.activity_tarea.txtVistaNombreCliTarea
import java.io.Serializable

class ClienteActivity : AppCompatActivity() {
   private lateinit var cliente: Cliente
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        cliente=intent.getSerializableExtra("cliente") as Cliente
        mostrarDatos()
    }

    private fun mostrarDatos() {
        txtVistaNombreCliente.text=cliente.nombre
        txtVistaApellidosCliTarea.text=cliente.apellidos
        txtVistaTlfCliente.text=cliente.telefono.toString()
        txtVistaLocalidadCliente.text=cliente.localidad
        txtVistaDomicilioCliente.text=cliente.domicilio
    }
}