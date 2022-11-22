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

    var accesoCorrecto = false
    var mail:String = ""
    var pass:String = ""
    var usuario:Usuario? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acceso)

        /*var btnAcceder = findViewById<Button>(R.id.btnAcceder)
        var txtID = findViewById<TextView>(R.id.txtID)
        var txtPass = findViewById<TextView>(R.id.txtPass)*/

        btnAcceder.setOnClickListener {
            mail=txtID.text.toString().trim()
            pass=txtPass.text.toString().trim()

            if (!camposVacios()){
                runBlocking {
                    val job : Job = launch ( context = Dispatchers.Default){
                        accesoCorrecto = FB.autenticar(mail, pass)
                    }
                    job.join()
                }

                if (!accesoCorrecto){
                    toast(this@Acceso, "Credenciales incorrectas")
                }else{
                    comprobarRol()
                }


            }else
                toast(this, "Todos los campos son obligatorios")


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

        return (mail.isEmpty() || pass.isEmpty())
    }
}