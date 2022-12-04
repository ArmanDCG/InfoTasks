package com.example.infotasks

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.TipoTarea
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Utiles.FechaHora
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CrearTarea : AppCompatActivity() {

    private lateinit var descripcion:String
    private lateinit var tipo:TipoTarea
    private lateinit var prioridad:PrioridadTarea
    private lateinit var dniCliente:String

    private lateinit var listaClientes:ArrayList<Cliente>
    private var posCliente:Int=-1
    private var listaTiposTarea= arrayListOf(TipoTarea.INCIDENCIA, TipoTarea.INSTALACION)
    private var posTipo:Int=-1
    private var listaPrioridadesTarea= arrayListOf(PrioridadTarea.BAJA, PrioridadTarea.MEDIA, PrioridadTarea.ALTA)
    private var posPrioridad:Int=-1






    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tarea)

        cargarListaClientes()


        spinnerCliente.adapter=ArrayAdapter(this, R.id.txtItem, listaClientes )
        spinnerCliente.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                posCliente=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

        spinnerTipo.adapter=ArrayAdapter(this, R.id.txtItem, listaTiposTarea)
        spinnerTipo.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                posTipo=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinnerPrioridad.adapter=ArrayAdapter(this, R.id.txtItem, listaPrioridadesTarea)
        spinnerPrioridad.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                posPrioridad=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        btnAddCliNuevo.setOnClickListener {
            //val intentAddCliente= Intent(this, AddClienteActivity::class.java)
            //startActivity(intentAddCliente)
        }

        btnCrearTarea.setOnClickListener {
            if(obtenerDatos()){
                var nuevaTarea=Tarea().let {
                    it.descripcion=descripcion
                    it.tipo=tipo
                    it.prioridad=prioridad
                    it.idCliente=dniCliente
                    it.fechaCreacion=FechaHora.obtenerFechaActual()
                    it.fechaUltimaMod=FechaHora.obtenerFechaActual()

                } as Tarea
                runBlocking {
                    val job: Job = launch(context = Dispatchers.Default) {

                        FB.añadirTarea(nuevaTarea)

                    }
                job.join()
                }
            }
        }

        btnCancelarCrearTarea.setOnClickListener{finish()}
    }

    private fun obtenerDatos(): Boolean {
        var correcto=true
        if (txtCrearDescTarea.text.isNotEmpty()) descripcion=txtCrearDescTarea.toString().trim() else correcto=false; pintar(txtErrorDescripcion)
        if (posCliente != -1) dniCliente= listaClientes[posCliente].toString() else correcto=false; pintar(txtErrorCliente)
        if (posTipo != -1) tipo=listaTiposTarea[posTipo] else correcto=false; pintar(txtErrorTipo)
        if (posPrioridad != -1) prioridad=listaPrioridadesTarea[posPrioridad] else correcto=false; pintar(txtErrorPrioridad)

        return correcto
    }

    private fun pintar(txtError: TextView?) {
        txtError!!.visibility= View.VISIBLE
    }

    private fun cargarListaClientes(){
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                listaClientes = FB.obtenerClientes()
            }
            job.join()
        }
    }

    override fun onStart() {
        super.onStart()
        cargarListaClientes()

    }
}