package com.example.infotasks
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.ListasModelos.ListaTareas
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.Utiles.Funcionales.toast
import kotlinx.android.synthetic.main.acceso.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates


class Acceso : AppCompatActivity() {

    private lateinit var mail:String
    private lateinit var pass:String
    private var usuario:Usuario? = null

    private lateinit var validarCampos:HashMap<Boolean, Int>




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acceso)

        supportActionBar?.hide()


        btnAcceder.setOnClickListener {
            if (obtenerCampos()) {

                if (autenticar()) {
                    comprobarUsuario()
                } else {
                    toast(this, "Credenciales incorrectas")
                }

            }
        }

    }

    override fun onStart() {
        super.onStart()
        txtID.setText("")
        txtPass.setText("")
    }

    private fun autenticar(): Boolean {
        var credencialesCorrectas=false
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                credencialesCorrectas = FB.autenticar(mail, pass)
            }
            job.join()
        }
        return credencialesCorrectas
    }
    fun comprobarUsuario(){
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                usuario = FB.obtenerUsuario(mail)
                Log.e("usuario", usuario.toString())
            }
            job.join()
        }
        if(comprobarActivo()) comprobarRol()
    }

    private fun comprobarActivo(): Boolean { return usuario!!.activo }

    private fun comprobarRol() {

        if (usuario!=null) {
            if (usuario!!.rol == RolUsuario.ADMINISTRADOR)
                lanzarVentanaAdmin()
            else
                lanzarVentanaTecnico()
        }
    }

    private fun lanzarVentanaTecnico() {
        val intentTec= Intent(this, Tecnico::class.java)
        intentTec.putExtra("tecnico", usuario)
        startActivity(intentTec)
    }

    private fun lanzarVentanaAdmin() {
        val intentAdmin= Intent(this, Administrador::class.java)
        intentAdmin.putExtra("admin", usuario)
        startActivity(intentAdmin)
    }

    private fun obtenerCampos(): Boolean {
        validarCampos= hashMapOf(true to 0, false to 0)
        validarCampos[validarMail()]=+1
        validarCampos[validarContraseña()]=+1

        return validarCampos[false]==0
    }
    private fun validarMail():Boolean {
        val dominio="infotasks.com"
        mail=txtID.text.toString().trim()
        return if (mail.isEmpty()) {
            mostrarError(txtErrorMailAcceso, false)
            false
        }else if(mail.length <= dominio.length) {
            mostrarError(txtErrorMailAcceso, true)
            false
        }else{
            ocultarError(txtErrorMailAcceso)
            true
        }
    }
    private fun validarContraseña():Boolean {
        pass=txtPass.text.toString().trim()
        return if(pass.isEmpty()) {
            mostrarError(txtErrorPassAcceso, false)
            false
        } else {
            ocultarError(txtErrorPassAcceso)
            true
        }
    }

    private fun mostrarError(textView: TextView, errorFormato:Boolean){
        if (errorFormato)
            textView.text="*Error de formato"
        textView.visibility = View.VISIBLE
    }

    private fun ocultarError(textView: TextView){
        textView.visibility = View.INVISIBLE
    }
}


