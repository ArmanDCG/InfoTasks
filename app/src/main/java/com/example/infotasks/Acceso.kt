package com.example.infotasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.Utiles.Funcionales.toast
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.acceso.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class Acceso : AppCompatActivity() {
    var credencialesCorrectas:Boolean = false
    var mail:String = ""
    var pass:String = ""
    var usuario:Usuario? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acceso)

        txtID.setText("admin@infotasks.com")
        txtPass.setText("administrador")

        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                Log.e("id HJmwVyzkP2qQ34oGjyIx ", FB.obtenerIdTarea())
            }
            job.join()
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