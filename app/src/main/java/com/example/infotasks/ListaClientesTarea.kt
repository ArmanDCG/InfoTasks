package com.example.infotasks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotasks.Adaptadores.AdaptadorClientes
import com.example.infotasks.Modelo.Cliente
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.activity_lista_clientes.*
import kotlinx.android.synthetic.main.activity_lista_clientes_tarea.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaClientesTarea : AppCompatActivity() {

    private lateinit var adaptadorClientes:AdaptadorClientes
    private lateinit var clientes:ArrayList<Cliente>
    private lateinit var clieteSeleccionado:Cliente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_clientes_tarea)
    }
    override fun onStart() {
        super.onStart()
        Log.e("ListaClientes", "abierta")
        obtenerListaClientes()
        lanzarAdaptador()
    }

    private fun obtenerListaClientes(){
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                clientes = FB.obtenerClientes()
            }
            job.join()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clieteSeleccionado=adaptadorClientes.obtenerClienteSeleccionado()
        val intentCrearCliente= Intent()
            .putExtra("clienteSeleccionado", clieteSeleccionado)
        setResult(Activity.RESULT_OK, intentCrearCliente)

    }
    private fun lanzarAdaptador(){
        recyclerClienteTarea.setHasFixedSize(true)
        recyclerClienteTarea.layoutManager = LinearLayoutManager(this)
        adaptadorClientes = AdaptadorClientes(this, clientes, true )
        recyclerClienteTarea.adapter=adaptadorClientes
    }
}