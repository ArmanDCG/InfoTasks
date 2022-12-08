package com.example.infotasks

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Adaptadores.AdaptadorClientes
import com.example.infotasks.Adaptadores.AdaptadorUsuarios
import com.example.infotasks.Modelo.Usuario
import kotlinx.android.synthetic.main.activity_lista_clientes.*
import kotlinx.android.synthetic.main.activity_lista_tecnicos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaUsuarios : Fragment() {
    lateinit var adaptadorUsuarios:AdaptadorUsuarios
    lateinit var usuarios:ArrayList<Usuario>
    lateinit var contexto: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_lista_tecnicos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contexto=this.requireActivity()


        search_tecnicos.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptadorUsuarios.filter.filter(newText)
                return true
            }

        })
    }

    override fun onStart() {
        super.onStart()
        cargarUsuarios()
        lanzarAdaptador()

    }

    private fun cargarUsuarios() {
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                usuarios= FB.obtenerUsuarios()
            }
            job.join()
        }
    }

    private fun lanzarAdaptador(){
        recyclerTecnicos.setHasFixedSize(true)
        recyclerTecnicos.layoutManager = LinearLayoutManager(contexto)
        adaptadorUsuarios = AdaptadorUsuarios(contexto, usuarios )
        recyclerTecnicos.adapter=adaptadorUsuarios
    }
}