package com.example.infotasks
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.Utiles.Funcionales.toast
import kotlinx.android.synthetic.main.acceso.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates


class Acceso : AppCompatActivity() {
    private var credencialesCorrectas by Delegates.notNull<Boolean>()
    private lateinit var mail:String
    private lateinit var pass:String
    private var usuario:Usuario? = null




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acceso)

        supportActionBar!!.hide()

        txtID.setText("admin@infotasks.com")
        txtPass.setText("administrador")

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
        val intentAdmin= Intent(this, Administrador::class.java)
        intentAdmin.putExtra("admin", usuario)
        startActivity(intentAdmin)
    }

    private fun camposVacios(): Boolean {
        mail=txtID.text.toString().trim()
        pass=txtPass.text.toString().trim()
        return (mail.isEmpty() || pass.isEmpty())
    }
}


