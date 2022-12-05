package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Utiles.Funcionales.toast
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.activity_crear_cliente.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class CrearCliente : AppCompatActivity() {

    private lateinit var dni:String
    private lateinit var nombre:String
    private lateinit var apellidos:String
    private var telefono=0
    private lateinit var localidad:String
    private lateinit var domicilio:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cliente)

        btnCrearCliente.setOnClickListener {
            var addCliente=false
            if(validarDni() && validarNombre() && validarApellidos() && validarTelefono() && validarLocalidad() && validarDomicilio()){
                runBlocking {
                    val job: Job = launch(context = Dispatchers.Default) {
                    addCliente=FB.añadirCliente(Cliente(dni, nombre, apellidos, telefono, localidad, domicilio))
                    }
                    job.join()
                }
                if (addCliente) {
                    toast(this, "Nuevo Cliente añadido correctamente")
                    finish()
                }else {
                    toast(this, "NO se pudo añadir el nuevo Cliente")
                }
            }
        }
        btnCancelarCrearCliente.setOnClickListener {
            finish()
        }
    }


    private fun validarDni(): Boolean {
        dni=txtCrearDniCliente.text.toString().trim().toUpperCase()
        return try {
            Integer.parseInt(dni.substring(0,8))
            var letra:Char=dni[8]
            Character.isLetter(letra)
            true
        }catch (ex:Exception){
            pintarError(txtErrorDni)
            false
        }
    }
    private fun validarNombre():Boolean{
        nombre=txtCrearNombreCliente.text.toString().trim()
        if(nombre.isNotEmpty())
            return true
        else
            pintarError(txtErrorNombre)
            return false

    }
    private fun validarApellidos():Boolean{
        apellidos=txtCrearApellidosCliente.text.toString().trim()
        if (apellidos.isNotEmpty())
            return true
        else
            pintarError(txtErrorApellidos)
            return false
    }
    private fun validarTelefono():Boolean{
        return try {
            telefono = Integer.parseInt(txtCrearTlfCliente.text.toString())
            true
        }catch (ex:Exception){
            pintarError(txtErrorTelefono)
            false
        }
    }
    private fun validarLocalidad():Boolean{
        localidad=txtCrearLocalidadCliente.text.toString().trim()
        if (localidad.isNotEmpty())
            return true
        else
            pintarError(txtErrorLocalidad)
            return false
    }
    private fun validarDomicilio():Boolean{
        domicilio=txtCrearDomicilioCliente.text.toString().trim()
        if (domicilio.isNotEmpty())
            return true
        else
            pintarError(txtErrorDomicilio)
            return false
    }



    private fun pintarError(txtError: TextView?) {
        txtError!!.visibility= View.VISIBLE
    }
}