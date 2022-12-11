package com.example.infotasks.VistaModelos

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.CrearEditarModelos.CrearTarea
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.R
import com.example.infotasks.Utiles.FechaHora
import kotlinx.android.synthetic.main.activity_tarea.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class TareaActivity : AppCompatActivity() {
    private val REQUEST_CODE=1
    private lateinit var tarea: Tarea
    private  lateinit var cliente: Cliente
    private lateinit var rol:RolUsuario

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Descripción Tarea"

        obtenerFunteDatos()
        mostrarDatos()
        comprobarRol()

        btnEditTarea.setOnClickListener {
            var intentTarea=Intent(this, CrearTarea::class.java)
                .putExtra("tarea", tarea)
                .putExtra("accion", "editar")
                .putExtra("cliente", cliente)
            startActivityForResult(intentTarea, REQUEST_CODE)
        }

        btnAbrirGoogleMaps.setOnClickListener {
            try {
                var dir= "${cliente.domicilio}, ${cliente.localidad}"
                var map = "http://maps.google.co.in/maps?q=$dir"
                var intentMap = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                startActivity(intentMap)
            }catch (e: Exception){
                Log.e("NO se pudo abrir maps", e.stackTraceToString())
            }
        }

        //btnCerrarTarea.setOnClickListener{
          //  guardarTarea()
        //}
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {

                if (rol==RolUsuario.TECNICO && txtVistaObservacionesTarea.text.toString() != tarea.observaciones) {
                   guardarCamposEditados()
                    guardarTarea()
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE && resultCode == RESULT_OK){
            tarea=data?.getSerializableExtra("tareaEditar") as Tarea
            mostrarDatos()
        }
    }

    private fun guardarTarea(){
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                FB.editarTarea(tarea)
            }
            job.join()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun guardarCamposEditados(){
        var observaciones=txtVistaObservacionesTarea.text.toString()
        tarea.observaciones = observaciones
        tarea.fechaUltimaMod = FechaHora.obtenerFechaActual()
    }

    private fun obtenerFunteDatos() {
        tarea=intent.getSerializableExtra("tarea") as Tarea
        rol=intent.getSerializableExtra("rol") as RolUsuario

        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                cliente= FB.obtenerCliente(tarea.idCliente!!)!!
            }
            job.join()
        }
    }

    private fun comprobarRol(){
        //Por defecto la Activity esta habilitada para interactuar como Administrador
        if (rol==RolUsuario.TECNICO){
            btnEditTarea.visibility= View.INVISIBLE
        }else
            txtVistaObservacionesTarea.isEnabled=false
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDatos() {
        //Tarea
        txtVistaTipoTarea.text=tarea.tipo.toString()
        txtVistaDescTarea.text=tarea.descripcion
        if (tarea.observaciones!=null)
            txtVistaObservacionesTarea.setText(tarea.observaciones)

        txtVistaPrioridadTarea.text=tarea.prioridad.toString()
        txtVistaEstadoTarea.text=tarea.estado.toString()
        txtVistaFechaCreacionTarea.text=FechaHora.dateToString(tarea.fechaCreacion!!)
        txtVistaFechaUltimaModTarea.text=FechaHora.dateToString(tarea.fechaUltimaMod!!)
        //Cliente
        txtVistaDniCliTarea.text=cliente.dni
        txtVistaNombreCliTarea.text=cliente.nombre
        txtVistaApellidosCliTarea.text=cliente.apellidos
        txtVistaTlfCliTarea.text=cliente.telefono.toString()
        txtVistaLocalidadCliTarea.text=cliente.localidad
        txtVistaDomicilioCliTarea.text=cliente.domicilio

    }

}
