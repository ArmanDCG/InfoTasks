package com.example.infotasks

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Constantes.TipoTarea
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.Utiles.Funcionales.toast
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.acceso.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.time.*
import java.time.format.DateTimeFormatter

class Acceso : AppCompatActivity() {
    var credencialesCorrectas:Boolean = false
    var mail:String = ""
    var pass:String = ""
    var usuario:Usuario? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acceso)

        txtID.setText("admin@infotasks.com")
        txtPass.setText("administrador")




        var fechaHora =Instant.now().atZone(ZoneId.of("Europe/Madrid"))
        var fecha=fechaHora.format(DateTimeFormatter.ofPattern("HH:mm"))
        var hora = fechaHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        Log.e("date", "$fecha - $hora")
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                FB.añadirTarea(Tarea("", "no funciona internet", TipoTarea.INCIDENCIA, PrioridadTarea.ALTA, EstadoTarea.PENDIENTE, fecha, hora, "", "1"))
            }
            job.join()
        }
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {

                Log.e("Tareas",FB.obtenerTareas().toString())
            }
            job.join()
        }

        try {
            var dir= "Calle Tercia 1, Fuente el Fresno"
            var map = "http://maps.google.co.in/maps?q=$dir"
            var intentMap:Intent= Intent(Intent.ACTION_VIEW, Uri.parse(map))
            startActivity(intentMap)
        }catch (e:Exception){
            Log.e("NO se pudo abrir maps", e.stackTraceToString())
        }


        btnAcceder.setOnClickListener {
            if (!camposVacios()) {
                runBlocking {
                    val job: Job = launch(context = Dispatchers.Default) {
                        credencialesCorrectas = FB.autenticar(mail, pass)
                    }
                    job.join()
                }
                if (credencialesCorrectas) {
                    comprobarRol()
                } else {
                    toast(this, "Credenciales incorrectas")
                }

            }else{
                toast(this, "Todos los campos son obligatorios")
            }
        }



    }

    private fun comprobarRol() {
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                usuario = FB.obtenerUsuario(mail)
                Log.e("usuario", usuario.toString())
            }
            job.join()
        }

        if (usuario!!.rol == RolUsuario.ADMINISTRADOR )
            lanzarVentanaAdmin()
        else
            lanzarVentanaTecnico()
    }

    private fun lanzarVentanaTecnico() {
        val intentTec= Intent(this, Usuario::class.java)
        intentTec.putExtra("tecnico", usuario)
        startActivity(intentTec)
    }

    private fun lanzarVentanaAdmin() {
        val intentAdmin= Intent(this, AdministradorActivity::class.java)
        intentAdmin.putExtra("admin", usuario)
        startActivity(intentAdmin)
    }

    private fun camposVacios(): Boolean {
        mail=txtID.text.toString().trim()
        pass=txtPass.text.toString().trim()
        return (mail.isEmpty() || pass.isEmpty())
    }
}


