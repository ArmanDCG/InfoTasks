package com.example.infotasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.infotasks.Modelo.Cliente
import kotlinx.android.synthetic.main.activity_cliente.*
import kotlinx.android.synthetic.main.activity_tarea.*
import kotlinx.android.synthetic.main.activity_tarea.txtVistaNombreCliTarea
import java.io.Serializable

class ClienteActivity : AppCompatActivity() {
    private val REQUEST_CODE=1
    private lateinit var cliente: Cliente
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        cliente=intent.getSerializableExtra("cliente") as Cliente
        Log.e("Vista cliente", cliente.toString())
        mostrarDatos()

        btnEditCliente.setOnClickListener {
            val intentCliente=Intent(this, CrearCliente::class.java)
                .putExtra("accion", "editar")
                .putExtra("cliente", cliente)
            startActivityForResult(intentCliente, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE && resultCode== RESULT_OK){
            cliente=data?.getSerializableExtra("clienteEditado") as Cliente
            mostrarDatos()
        }
    }

    private fun mostrarDatos() {
        txtVistaDniCliente.text=cliente.dni
        txtVistaNombreCliente.text=cliente.nombre
        txtVistaApellidosCliente.text=cliente.apellidos
        txtVistaTlfCliente.text=cliente.telefono.toString()
        txtVistaLocalidadCliente.text=cliente.localidad
        txtVistaDomicilioCliente.text=cliente.domicilio
    }
}