package com.example.infotasks

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infotasks.Adaptadores.AdaptadorTareas
import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.TipoTarea
import com.example.infotasks.Modelo.Tarea
import com.example.salidadeportiva.ConexionBD.FB
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_lista_tareas.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaTareas : Fragment() {
    private lateinit var adaptadorTareas:AdaptadorTareas
    private lateinit var tareas:ArrayList<Tarea>

    //Variables de los filtros. Por defecto = 0 = ningún filtro
    private var numPrioridad:Int = 0 // 1=Baja, 2=Media, 3=Alta
    private var numTipo:Int =0 // 1=Incidencia, 2=Instalación
    private var numEstado:Int=0 //1=Pendiente, 2=Realizada
    private var numFechaCreacion=1 //1=Descendente, 2=Ascendente
    private var numFechaUltimaMod=0 //1=Descendente, 2=Ascendente



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_tareas)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_lista_tareas, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contexto=requireActivity().applicationContext


        btnAñadirTarea.setOnClickListener{
            val intentCrearTarea= Intent(contexto, CrearTarea::class.java )
            startActivity(intentCrearTarea)
        }

        //Botones Filtros de Ordenación
        btnOrdPrioridad.setOnClickListener {
            // 1=Baja, 2=Media, 3=Alta
            numPrioridad++
            when(numPrioridad){
                0->{
                    obtenerTareasPorDefecto()

                }
                1->{
                    obtenerTareasPorPrioridad(PrioridadTarea.BAJA.toString())
                    btnOrdPrioridad.text="Prioridad\nBaja"
                }
                2->{
                    obtenerTareasPorPrioridad(PrioridadTarea.MEDIA.toString())
                    btnOrdPrioridad.text="Prioridad\nMedia"
                }
                3->{
                    obtenerTareasPorPrioridad(PrioridadTarea.ALTA.toString())
                    btnOrdPrioridad.text="Prioridad\nAlta"
                    numPrioridad=-1
                }
            }
        }
        btnOrdTipo.setOnClickListener {
            //1=Incidencia, 2=Instalación
            numTipo++
            when(numTipo){
                0->{
                    obtenerTareasPorDefecto()
                }
                1->{
                    obtenerTareasPorTipo(TipoTarea.INCIDENCIA.toString())
                    btnOrdTipo.text="Tipo\nIncidencia"
                }
                2->{
                    obtenerTareasPorTipo(TipoTarea.INSTALACION.toString())
                    btnOrdTipo.text="Tipo\nInstalar"
                    numTipo=-1
                }
            }
        }

        btnOrdEstado.setOnClickListener {
            //1=Pendiente, 2=Realizada
            numEstado++
            when(numEstado){
                0->{
                     obtenerTareasPorDefecto()
                }
                1->{
                    obtenerTareasPorEstado(EstadoTarea.PENDIENTE.toString())
                    btnOrdEstado.text="Estado\nPendiente"
                }
                2->{
                    obtenerTareasPorEstado(EstadoTarea.REALIZADA.toString())
                    btnOrdEstado.text="Estado\nRealizada"
                    numEstado=-1
                }
            }
        }
        btnOrdFechCreacion.setOnClickListener {
            //Por defecto Descendente
            //1=Descendente, 2=Ascendente
            numFechaCreacion++
            var icono:Drawable
            when(numFechaCreacion){
                1->{
                    obtenerTareasPorFecha(Query.Direction.DESCENDING)
                    icono= requireContext().resources?.getDrawable(R.drawable.menor_primero)!!
                    btnOrdFechCreacion.setCompoundDrawables(null, null, icono, null)
                }
                2->{
                    obtenerTareasPorFecha(Query.Direction.ASCENDING)
                    icono= requireContext().resources?.getDrawable(R.drawable.mayor_primero)!!
                    btnOrdFechCreacion.setCompoundDrawables(null, null, icono, null)
                    numFechaCreacion=0
                }
            }
        }
        btnOrdUltimaMod.setOnClickListener {
            //1=Descendente, 2=Ascendente
            numFechaUltimaMod++
            var icono:Drawable
            when(numFechaUltimaMod){
                0->{
                    obtenerTareasPorDefecto()
                    btnOrdUltimaMod.setCompoundDrawables(null, null, null, null)
                }
                1->{
                    obtenerTareasPorFecha(Query.Direction.DESCENDING)
                    icono= requireContext().resources?.getDrawable(R.drawable.menor_primero)!!
                    btnOrdFechCreacion.setCompoundDrawables(null, null, icono, null)
                }
                2->{
                    obtenerTareasPorFecha(Query.Direction.ASCENDING)
                    icono= requireContext().resources?.getDrawable(R.drawable.mayor_primero)!!
                    btnOrdFechCreacion.setCompoundDrawables(null, null, icono, null)
                    numFechaUltimaMod=-1
                }

            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        obtenerTareasPorDefecto()
        lanzarAdaptador()
    }

    private fun lanzarAdaptador(){
        recyclerTareas.setHasFixedSize(true)
        recyclerTareas.layoutManager = LinearLayoutManager(this.context)
        adaptadorTareas = AdaptadorTareas(this.requireContext(), tareas )
        recyclerTareas.adapter=adaptadorTareas
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerTareasPorDefecto(){
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                tareas = FB.obtenerTareas()
            }
            job.join()
        }
        filtrosPorDefecto()
        lanzarAdaptador()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerTareasPorPrioridad(prioridad:String) {
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                tareas = FB.obtenerTareasPrioridad(prioridad)
            }
            job.join()
        }
        lanzarAdaptador()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerTareasPorTipo(tipo:String) {
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                tareas = FB.obtenerTareasTipo(tipo)
            }
            job.join()
        }
        lanzarAdaptador()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerTareasPorEstado(estado: String) {
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                tareas = FB.obtenerTareasEstado(estado)
            }
            job.join()
        }
        lanzarAdaptador()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun obtenerTareasPorFecha(direcion: Query.Direction) {
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                tareas = FB.obtenerTareasFecha(direcion)
            }
            job.join()
        }
        lanzarAdaptador()
    }

    private fun filtrosPorDefecto(){
        numPrioridad=0
        numEstado=0
        numTipo=0
        numFechaCreacion=1
        numFechaUltimaMod=0

        //Texto de Filtros
        btnOrdPrioridad.text="Prioridad"
        btnOrdTipo.text="Tipo"
        btnOrdEstado.text="Estado"
        btnOrdFechCreacion.text="Fecha Creación"
        btnOrdUltimaMod.text="Ultima Mod"
    }
}