package com.example.infotasks.CrearEditarModelos

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.TipoTarea
import com.example.infotasks.ListasModelos.ListaClientesTarea
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.R
import com.example.infotasks.Utiles.FechaHora
import com.example.infotasks.Utiles.Funcionales.toast
import kotlinx.android.synthetic.main.activity_crear_tarea.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class CrearTarea : AppCompatActivity() {
    private val REQUEST_CODE=1

    //Solo en caso de editar
    private lateinit var accion:String //Crear Tarea o Editar Tarea
    private lateinit var tareaEditar: Tarea

    private lateinit var nuevaTarea:Tarea
    private lateinit var descripcion:String
    private lateinit var tipo:TipoTarea
    private lateinit var prioridad:PrioridadTarea
    private lateinit var dniCliente:String
    private var cliente: Cliente?=null

    //Spinners
    private lateinit var listaTiposTarea:ArrayList<TipoTarea>
    private var posTipo by Delegates.notNull<Int>()
    private lateinit var listaPrioridadesTarea:ArrayList<PrioridadTarea>
    private var posPrioridad by Delegates.notNull<Int>()

    private lateinit var validacionCampos:HashMap<Boolean,Int>



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tarea)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Crear"

        listaTiposTarea = arrayListOf(TipoTarea.INCIDENCIA, TipoTarea.INSTALACION)
        listaPrioridadesTarea = arrayListOf(PrioridadTarea.BAJA, PrioridadTarea.MEDIA, PrioridadTarea.ALTA)
        posPrioridad=0
        posTipo=0

        accion=intent.getStringExtra("accion") as String


        spinnerTipo.adapter=ArrayAdapter(this, R.layout.spinner_item, R.id.txtItem, listaTiposTarea)
        spinnerTipo.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                posTipo=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        spinnerPrioridad.adapter=ArrayAdapter(this,
            R.layout.spinner_item,
            R.id.txtItem, listaPrioridadesTarea )
        spinnerPrioridad.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                posPrioridad=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        comprobarAccion()



        btnAddCliNuevo.setOnClickListener {
            val intentAddCliente= Intent(this, CrearCliente::class.java)
                .putExtra("accion", "crear")
            this.startActivityForResult(intentAddCliente,REQUEST_CODE)
        }

        btnBuscarCliente.setOnClickListener {
            val intentLCT=Intent(this, ListaClientesTarea::class.java)
            Log.e("REQUEST_CODE", REQUEST_CODE.toString())
            this.startActivityForResult(intentLCT, REQUEST_CODE)
        }

        btnCrearTarea.setOnClickListener {

            if(obtenerDatos()){
                if (accion=="editar")
                {
                    asignarAtributosEditados()
                    guardarTarea(tareaEditar!!, true)
                }else{
                    asignarAtributosNuevos()
                    guardarTarea(nuevaTarea, false)
                }

            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun asignarAtributosNuevos() {
        nuevaTarea=Tarea()
        nuevaTarea.let {
            it.descripcion=descripcion
            it.tipo=tipo
            it.prioridad=prioridad
            it.idCliente=dniCliente
            it.fechaCreacion=FechaHora.obtenerFechaActual()
            it.fechaUltimaMod=FechaHora.obtenerFechaActual()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun asignarAtributosEditados() {
        tareaEditar.let {
            it!!.descripcion=descripcion
            it.prioridad=prioridad
            it.fechaUltimaMod=FechaHora.obtenerFechaActual()
        }
    }

    private fun guardarTarea(tarea:Tarea, editar:Boolean) {
        var a??adido=false
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                a??adido = if (editar)
                    FB.editarTarea(tarea)
                else
                    FB.a??adirTarea(tarea)
            }
            job.join()
        }
        if (a??adido && editar) {
            toast(this, "Tarea editada correctamente")
            setResult(RESULT_OK, Intent().putExtra("tareaEditar",tarea))
        }else if (a??adido && !editar) {
            toast(this, "Tarea creada correctamente")
        }else
            toast(this, "Hubro un error conexi??n")

        finish()
    }

    private fun comprobarAccion() {
        if (accion=="editar") {
            supportActionBar!!.title="Editar"
            tareaEditar = intent.getSerializableExtra("tarea") as Tarea
            cliente = intent.getSerializableExtra("cliente") as Cliente

            btnCrearTarea.text="Guardar"
            mostrarTarea()
        }
    }

    private fun mostrarTarea() {
        txtCrearDescTarea.setText(tareaEditar!!.descripcion)
        spinnerTipo.setSelection(listaTiposTarea.indexOf(tareaEditar!!.tipo ))
        spinnerPrioridad.setSelection(listaPrioridadesTarea.indexOf(tareaEditar!!.prioridad))
        establecerNombreCliente("${cliente!!.nombre}, ${cliente!!.apellidos}")
        //Deshabilitar elemenos que no se puden editar
        btnAddCliNuevo.isEnabled=false
        spinnerTipo.isEnabled=false
        btnBuscarCliente.isEnabled=false

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode==REQUEST_CODE){
            cliente= data?.getSerializableExtra("clienteAsigTarea",) as Cliente
            if (cliente!=null)
                establecerNombreCliente("${cliente!!.nombre}, ${cliente!!.apellidos}")
        }
    }

    private fun establecerNombreCliente(nombreCliente:String) {
        txtCrearNomClienteTarea.text=nombreCliente
    }

    private fun obtenerDatos(): Boolean {
        validacionCampos= hashMapOf(true to 0, false to 0)
        validacionCampos[validarDescripcion()]=+1
        validacionCampos[validarCliente()]=+1
        tipo=listaTiposTarea[posTipo]
        prioridad=listaPrioridadesTarea[posPrioridad]

        return (validacionCampos[false]==0)
    }

    private fun validarDescripcion():Boolean{
        descripcion = txtCrearDescTarea.text.toString().trim()
        return if(descripcion.isEmpty()) {
            mostrarError(txtErrorDescripcion)
            false
        }else{
            ocultarError(txtErrorDescripcion)
            true
        }
    }

    private fun validarCliente():Boolean{
        return if(cliente!=null){
            dniCliente = cliente!!.dni.toString()
            ocultarError(txtErrorCliente)
            true
        }else{
            mostrarError(txtErrorCliente)
            false
        }
    }

    private fun mostrarError(textView: TextView?) {
        textView!!.visibility= View.VISIBLE
    }

    private fun ocultarError(textView:TextView){
        textView.visibility=View.INVISIBLE
    }

}