package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotasks.Adaptadores.AdaptadorClientes
import com.example.infotasks.Modelo.Cliente
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.activity_lista_clientes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaClientes : Fragment() {
    private lateinit var adaptadorClientes:AdaptadorClientes
    private lateinit var clientes:ArrayList<Cliente>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_lista_clientes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.e("ListaClientes", "abierta")
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
        recyclerClientes.layoutManager = LinearLayoutManager(this.context)
        adaptadorClientes = AdaptadorClientes(this.requireContext(), clientes )
        recyclerClientes.adapter=adaptadorClientes
    }
}