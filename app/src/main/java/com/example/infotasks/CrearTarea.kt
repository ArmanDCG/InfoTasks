package com.example.infotasks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.TipoTarea
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Utiles.FechaHora
import com.example.infotasks.Utiles.Funcionales.toast
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CrearTarea : AppCompatActivity() {
    private val REQUEST_CODE=Activity.RESULT_OK

    private lateinit var descripcion:String
    private lateinit var tipo:TipoTarea
    private lateinit var prioridad:PrioridadTarea
    private lateinit var dniCliente:String
    private  var cliente: Cliente? =null


    private var listaTiposTarea= arrayListOf(TipoTarea.INCIDENCIA, TipoTarea.INSTALACION)
    private var posTipo:Int=0
    private var listaPrioridadesTarea= arrayListOf(PrioridadTarea.BAJA, PrioridadTarea.MEDIA, PrioridadTarea.ALTA)
    private var posPrioridad:Int=0






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
            startActivityForResult(intentAddCliente,REQUEST_CODE)
        }
        btnBuscarCliente.setOnClickListener {
            val intentLCT=Intent(this, ListaClientesTarea::class.java)
            Log.e("REQUEST_CODE", REQUEST_CODE.toString())
            startActivityForResult(intentLCT, REQUEST_CODE)
        }

        btnCrearTarea.setOnClickListener {
            var añadido=false
            if(obtenerDatos()){
                var nuevaTarea=Tarea()
                nuevaTarea.descripcion=descripcion
                nuevaTarea.tipo=tipo
                nuevaTarea.prioridad=prioridad
                nuevaTarea.idCliente=dniCliente
                nuevaTarea.fechaCreacion=FechaHora.obtenerFechaActual()
                nuevaTarea.fechaUltimaMod=FechaHora.obtenerFechaActual()
                runBlocking {
                    val job: Job = launch(context = Dispatchers.Default) {
                        añadido=FB.añadirTarea(nuevaTarea)
                    }
                job.join()
                }
                if (añadido) {
                    toast(this, "Tarea creada correctamente")
                    finish()
                }else
                    toast(this, "Hubo un error al crear la nueva tarea")
            }
        }

        btnCancelarCrearTarea.setOnClickListener{finish()}
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_CODE){
            cliente= data?.getSerializableExtra("clienteAsigTarea",) as Cliente
            Log.e("Cliente elegido", cliente.toString())
            if (cliente!=null)
                txtCrearNomClienteTarea.text="${cliente!!.nombre}, ${cliente!!.apellidos}"
        }
    }

    private fun obtenerDatos(): Boolean {
        var correcto = true

        if (txtCrearDescTarea.text.isNotEmpty()) {
            descripcion = txtCrearDescTarea.toString().trim()
        } else {
            correcto = false
            mostrarError(txtErrorDescripcion)
        }

        if (cliente!=null) {
            dniCliente = cliente!!.dni.toString()
        }else {
            correcto = false
            mostrarError(txtErrorCliente)
        }
        tipo=listaTiposTarea[posTipo]
        prioridad=listaPrioridadesTarea[posPrioridad]

        return correcto
    }

    private fun mostrarError(txtError: TextView?) {
        txtError!!.visibility= View.VISIBLE
    }

}