package com.example.infotasks

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cliente)

        btnCrearCliente.setOnClickListener {
            var addCliente=false
            var validacionCampos= hashMapOf<Boolean,Int>( true to 0, false to 0)

            validacionCampos[validarDni()]=+1
            validacionCampos[validarNombre()]=+1
            validacionCampos[validarApellidos()]=+1
            validacionCampos[validarTelefono()]=+1
            validacionCampos[validarLocalidad()]=+1
            validacionCampos[validarDomicilio()]=+1

            Log.e("Resultado de validación de campos", validacionCampos.toString())

            if(validacionCampos[false]==0){
                runBlocking {
                    val job: Job = launch(context = Dispatchers.Default) {
                    addCliente= FB.añadirCliente(Cliente(dni, nombre, apellidos, Integer.parseInt(telefono), localidad, domicilio))
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

    private fun validarDni():Boolean {
        var dniCorrecto=true
        dni=txtCrearDniCliente.text.toString().trim().toUpperCase()

        if (dni.isEmpty()) {
            mostrarError(txtErrorDni, false)
            dniCorrecto=false
        }else if (dni.length != 9){
                mostrarError(txtErrorDni, true)
                dniCorrecto=false
        }else if(!dni[8].isLetter()){
            mostrarError(txtErrorDni, true)
            dniCorrecto=false
        }else
            try {
                Integer.parseInt(dni.substring(0,8))
            }catch (ex:Exception){
                mostrarError(txtErrorDni, true)
                dniCorrecto=false
            }
        if (dniCorrecto) ocultarError(txtErrorDni)
        return dniCorrecto
    }

    private fun validarNombre():Boolean{
        nombre=txtCrearNombreCliente.text.toString().trim()
        if(nombre.isNotEmpty()) {
            ocultarError(txtErrorNombre)
            return true
        }else
            mostrarError(txtErrorNombre, false)
            return false
    }

    private fun validarApellidos():Boolean{
        apellidos=txtCrearApellidosCliente.text.toString().trim()
        if (apellidos.isNotEmpty()) {
            ocultarError(txtErrorApellidos)
            return true
        }else
            mostrarError(txtErrorApellidos, false)
            return false
    }

    private fun validarTelefono():Boolean{
        var tlfCorrecto=true
        telefono=txtCrearTlfCliente.text.toString()
        if (telefono.isEmpty()){
            mostrarError(txtErrorTelefono, false)
            tlfCorrecto=false
        }else if(telefono.length != 9 ){
            mostrarError(txtErrorTelefono, true)
            tlfCorrecto=false
        }

        if (tlfCorrecto)ocultarError(txtErrorTelefono)
        return tlfCorrecto
    }

    private fun validarLocalidad():Boolean{
        localidad=txtCrearLocalidadCliente.text.toString().trim()
        if (localidad.isNotEmpty()) {
            ocultarError(txtErrorLocalidad)
            return true
        }else
            mostrarError(txtErrorLocalidad, false)
            return false
    }

    private fun validarDomicilio():Boolean{
        domicilio=txtCrearDomicilioCliente.text.toString().trim()
        if (domicilio.isNotEmpty()) {
            ocultarError(txtErrorDomicilio)
            return true
        }else
            mostrarError(txtErrorDomicilio, false)
            return false
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