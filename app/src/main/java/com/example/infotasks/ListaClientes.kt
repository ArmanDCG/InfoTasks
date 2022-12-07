package com.example.infotasks


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Adaptadores.AdaptadorClientes
import com.example.infotasks.Modelo.Cliente
import kotlinx.android.synthetic.main.activity_lista_clientes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaClientes : Fragment()   {
    private lateinit var adaptadorClientes: AdaptadorClientes
    private lateinit var clientes:ArrayList<Cliente>
    private lateinit var listaClientesFiltrado:ArrayList<Cliente>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_lista_clientes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptadorClientes.filter.filter(newText)
                return true
            }

        })
    }

    override fun onStart() {
        super.onStart()
        obtenerListaClientes()
        lanzarAdaptador()
    }

    private fun obtenerListaClientes() {
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                clientes = FB.obtenerClientes()
            }
            job.join()
        }
        listaClientesFiltrado=clientes

    }

    private fun lanzarAdaptador(){
        recyclerClientes.setHasFixedSize(true)
        recyclerClientes.layoutManager = LinearLayoutManager(this.context)
        adaptadorClientes = AdaptadorClientes(this.requireContext(), clientes,true)
        recyclerClientes.adapter=adaptadorClientes
    }
}