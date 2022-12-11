package com.example.infotasks.VistaModelos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.example.infotasks.CrearEditarModelos.CrearCliente
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.R
import kotlinx.android.synthetic.main.activity_cliente.*

class ClienteActivity : AppCompatActivity() {
    private val REQUEST_CODE=1
    private lateinit var cliente: Cliente
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Descripción Cliente"

        cliente=intent.getSerializableExtra("cliente") as Cliente
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
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