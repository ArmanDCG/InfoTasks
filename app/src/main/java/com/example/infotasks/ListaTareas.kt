package com.example.infotasks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotasks.Adaptadores.AdaptadorTareas
import com.example.infotasks.Modelo.Tarea
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.activity_lista_tareas.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaTareas : Fragment() {
    private lateinit var adaptadorTareas:AdaptadorTareas
    private lateinit var tareas:ArrayList<Tarea>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_tareas)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_lista_tareas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAñadirTarea.setOnClickListener{
            val intentCrearTarea= Intent(this.context, CrearTarea::class.java )
            startActivity(intentCrearTarea)
        }
    }

    override fun onStart() {
        super.onStart()
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                tareas = FB.obtenerTareas()
            }
            job.join()
        }
        lanzarAdaptador()
    }

    private fun lanzarAdaptador(){
        recyclerTareas.setHasFixedSize(true)
        recyclerTareas.layoutManager = LinearLayoutManager(this.context)
        adaptadorTareas = AdaptadorTareas(this.requireContext(), tareas )
        recyclerTareas.adapter=adaptadorTareas
    }
}