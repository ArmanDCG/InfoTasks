package com.example.infotasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotasks.Adaptadores.AdaptadorTareas
import com.example.infotasks.Modelo.Tarea
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.activity_tareas.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaTareasActivity : AppCompatActivity() {
    private lateinit var adaptadorTareas:AdaptadorTareas
    private lateinit var tareas:ArrayList<Tarea>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                tareas = FB.obtenerTareas()
            }
            job.join()
        }
        lanzarAdaptador()

        btnAñadirTarea.setOnClickListener{
            val intentCrearTarea= Intent(this, CrearTareaActivity::class.java )
            startActivity(intentCrearTarea)
        }
    }

    private fun lanzarAdaptador(){
        recyclerTareas.setHasFixedSize(true)
        recyclerTareas.layoutManager = LinearLayoutManager(this)
        adaptadorTareas = AdaptadorTareas(this, tareas )
        recyclerTareas.adapter=adaptadorTareas
    }
}