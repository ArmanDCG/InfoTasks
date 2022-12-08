package com.example.infotasks

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Utiles.FechaHora
import io.grpc.InternalChannelz.id
import kotlinx.android.synthetic.main.activity_tarea.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class TareaActivity : AppCompatActivity() {

    private lateinit var tarea: Tarea
    private  lateinit var cliente: Cliente

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        obtenerFunteDatos()
        mostrarDatos()

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun obtenerFunteDatos() {
        tarea=intent.getSerializableExtra("tarea") as Tarea
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                cliente= FB.obtenerCliente(tarea.idCliente!!)!!
            }
            job.join()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrarDatos() {
        //Tarea
        txtVistaTipoTarea.text=tarea.tipo.toString()
        txtVistaDescTarea.text=tarea.descripcion
        if (tarea.observaciones!=null)
            txtVistaObservacionesTarea.text= tarea.observaciones
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
