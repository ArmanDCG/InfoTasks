package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotasks.Adaptadores.AdaptadorClientes
import com.example.infotasks.Adaptadores.AdaptadorTareas
import com.example.infotasks.Modelo.Cliente
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.activity_lista_clientes.*
import kotlinx.android.synthetic.main.activity_tareas.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaClientes : AppCompatActivity() {
    private lateinit var adaptadorClientes:AdaptadorClientes
    private lateinit var clientes:ArrayList<Cliente>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_clientes)

    }

    override fun onStart() {
        super.onStart()
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                clientes = FB.obtenerClientes()
            }
            job.join()
        }
        lanzarAdaptador()
    }

    private fun lanzarAdaptador(){
        recyclerClientes.setHasFixedSize(true)
        recyclerClientes.layoutManager = LinearLayoutManager(this)
        adaptadorClientes = AdaptadorClientes(this, clientes )
        recyclerClientes.adapter=adaptadorClientes
    }
}