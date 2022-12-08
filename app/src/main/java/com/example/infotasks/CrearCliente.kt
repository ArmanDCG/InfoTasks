package com.example.infotasks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Utiles.Funcionales.toast
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
    private lateinit var telefono:String
    private lateinit var localidad:String
    private lateinit var domicilio:String

    private lateinit var validacionCampos:HashMap<Boolean,Int>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_usuario)
        validacionCampos= HashMap()

        btnCrearCliente.setOnClickListener {
            var clienteCreado=false

            Log.e("Resultado de validación de campos", validacionCampos.toString())

            if(obtenerDatos()){
                var nuevoCliente=Cliente(dni, nombre, apellidos, Integer.parseInt(telefono), localidad, domicilio)
                runBlocking {
                    val job: Job = launch(context = Dispatchers.Default) {
                    clienteCreado= FB.añadirCliente(nuevoCliente)
                    }
                    job.join()
                }

                if (clienteCreado) {
                    toast(this, "Nuevo Cliente creado correctamente")
                    var intent= Intent().putExtra("clienteAsigTarea",nuevoCliente )
                    setResult(Activity.RESULT_OK, intent)
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

    private fun obtenerDatos():Boolean{
        validacionCampos[validarDni()]=+1
        validacionCampos[validarNombre()]=+1
        validacionCampos[validarApellidos()]=+1
        validacionCampos[validarTelefono()]=+1
        validacionCampos[validarLocalidad()]=+1
        validacionCampos[validarDomicilio()]=+1

        return(validacionCampos [false]==0)
    }

    private fun validarDni():Boolean {
        dni=txtCrearDniCliente.text.toString().trim().toUpperCase()

        return if (dni.isEmpty()) {
            mostrarError(txtErrorDni, false)
            false
        }else if (dni.length != 9){
                mostrarError(txtErrorDni, true)
                false
        }else if(!dni[8].isLetter()){
            mostrarError(txtErrorDni, true)
            false
        }else
            return try {
                Integer.parseInt(dni.substring(0,8))
                ocultarError(txtErrorDni)
                true
            }catch (ex:Exception){
                mostrarError(txtErrorDni, true)
                false
            }
    }

    private fun validarNombre():Boolean{
        nombre=txtCrearNombreCliente.text.toString().trim()
        return if(nombre.isNotEmpty()) {
            ocultarError(txtErrorNombre)
            true
        }else {
            mostrarError(txtErrorNombre, false)
            false
        }
    }

    private fun validarApellidos():Boolean{
        apellidos=txtCrearApellidosCliente.text.toString().trim()
        return if (apellidos.isNotEmpty()) {
            ocultarError(txtErrorApellidos)
            return true
        }else {
            mostrarError(txtErrorApellidos, false)
            false
        }
    }

    private fun validarTelefono():Boolean{
        telefono=txtCrearTlfCliente.text.toString()
        return if (telefono.isEmpty()){
            mostrarError(txtErrorTelefono, false)
            false
        }else if(telefono.length != 9 ){
            mostrarError(txtErrorTelefono, true)
            false
        }else{
            ocultarError(txtErrorTelefono)
            true
        }
    }

    private fun validarLocalidad():Boolean{
        localidad=txtCrearLocalidadCliente.text.toString().trim()
        return if (localidad.isNotEmpty()) {
            ocultarError(txtErrorLocalidad)
            true
        }else{
            mostrarError(txtErrorLocalidad, false)
            false
        }
    }

    private fun validarDomicilio():Boolean{
        domicilio=txtCrearDomicilioCliente.text.toString().trim()
        return if (domicilio.isNotEmpty()) {
            ocultarError(txtErrorDomicilio)
             true
        }else{
            mostrarError(txtErrorDomicilio, false)
            false
        }
    }



    private fun mostrarError(textView:TextView, errorFormato:Boolean) {
        var msjObligatorio="*Campo obligatorio"
        var msjFormato="*Formato incorrecto"

        if (errorFormato)
            textView.text=msjFormato
        else
            textView.text=msjObligatorio

        textView.visibility= View.VISIBLE
    }

    private fun ocultarError(textView:TextView){
        textView.visibility= View.INVISIBLE
    }
}