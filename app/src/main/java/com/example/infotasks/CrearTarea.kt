package com.example.infotasks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
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
    private val REQUEST_CODE=0

    private lateinit var descripcion:String
    private lateinit var tipo:TipoTarea
    private lateinit var prioridad:PrioridadTarea
    private lateinit var dniCliente:String
    private  var cliente: Cliente? =null


    private var listaTiposTarea= arrayListOf(TipoTarea.INCIDENCIA, TipoTarea.INSTALACION)
    private var posTipo:Int=-1
    private var listaPrioridadesTarea= arrayListOf(PrioridadTarea.BAJA, PrioridadTarea.MEDIA, PrioridadTarea.ALTA)
    private var posPrioridad:Int=-1






    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tarea)


        spinnerTipo.adapter=ArrayAdapter(this, R.layout.spinner_item,R.id.txtItem, listaTiposTarea)

        spinnerTipo.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                posTipo=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        //spinnerPrioridad.adapter=ArrayAdapter(this, R.id.txtItem, listaPrioridadesTarea)
        spinnerPrioridad.adapter=ArrayAdapter(this, R.layout.spinner_item,R.id.txtItem, listaPrioridadesTarea )
        spinnerPrioridad.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                posPrioridad=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }



        btnAddCliNuevo.setOnClickListener {
            val intentAddCliente= Intent(this, CrearCliente::class.java)
            startActivity(intentAddCliente)
        }
        btnBuscarCliente.setOnClickListener {
            val intentLCT=Intent(this, ListaClientesTarea::class.java)
            startActivityForResult(intentLCT, REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_CODE){
            cliente= data?.getSerializableExtra("clienteSeleccionado",) as Cliente

            if (cliente!=null)
                txtCrearNomClienteTarea.text="${cliente!!.nombre}, ${cliente!!.apellidos}"
        }
    }

    private fun obtenerDatos(): Boolean {
        var correcto=true
        if (txtCrearDescTarea.text.isNotEmpty()) descripcion=txtCrearDescTarea.toString().trim() else correcto=false; pintarError(txtErrorDescripcion)
        if (cliente!=null) dniCliente= cliente!!.dni.toString() else correcto=false; pintarError(txtErrorCliente)
        if (posTipo != -1) tipo=listaTiposTarea[posTipo] else correcto=false; pintarError(txtErrorTipo)
        if (posPrioridad != -1) prioridad=listaPrioridadesTarea[posPrioridad] else correcto=false; pintarError(txtErrorPrioridad)

        return correcto
    }

    private fun pintarError(txtError: TextView?) {
        txtError!!.visibility= View.VISIBLE
    }

}