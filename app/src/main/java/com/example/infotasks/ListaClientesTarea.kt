package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Adaptadores.AdaptadorClientes
import com.example.infotasks.Modelo.Cliente
import kotlinx.android.synthetic.main.activity_lista_clientes.*
import kotlinx.android.synthetic.main.activity_lista_clientes_tarea.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaClientesTarea : AppCompatActivity() {

    private lateinit var adaptadorClientes:AdaptadorClientes
    private lateinit var clientes:ArrayList<Cliente>

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


    private fun lanzarAdaptador(){
        recyclerClientes.setHasFixedSize(true)
        recyclerClientes.layoutManager = LinearLayoutManager(this)
        adaptadorClientes = AdaptadorClientes(this, clientes, true )
        recyclerClientes.adapter=adaptadorClientes
    }
}