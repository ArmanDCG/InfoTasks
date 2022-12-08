package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.Utiles.Funcionales.toast
import kotlinx.android.synthetic.main.activity_crear_cliente.*
import kotlinx.android.synthetic.main.activity_crear_usuario.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class CrearUsuario : AppCompatActivity() {

    private lateinit var mail:String
    private lateinit var nombre:String
    private lateinit var apellidos:String
    private lateinit var telefono:String
    private lateinit var rol:RolUsuario
    private var activo by Delegates.notNull<Boolean>()

    private lateinit var listaRoles:ArrayList<RolUsuario>
    private var posRol by Delegates.notNull<Int>()

    private lateinit var validacionCampos:HashMap<Boolean, Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_usuario)

        validacionCampos= HashMap()
        listaRoles= arrayListOf(RolUsuario.TECNICO, RolUsuario.ADMINISTRADOR)


        spinnerRol.adapter=ArrayAdapter(this, R.layout.spinner_item,R.id.txtItem, listaRoles)
        spinnerRol.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                posRol=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                posRol=0
            }

        }

        btnCrearUsuario.setOnClickListener {
            var usuarioCreado=false
            if(obtenerDatos()){
                runBlocking {
                    val job: Job = launch(context = Dispatchers.Default) {
                    usuarioCreado= FB.añadirUsuario(Usuario(mail, nombre, apellidos, telefono.toInt(),rol, activo))
                    }
                    job.join()
                }
                if (usuarioCreado){
                    toast(this, "Nuevo Usuario creado correctamente")
                    finish()
                }else
                    toast(this, "NO se pudo añadir el nuevo Usuario")
            }
            }

        }

    private fun obtenerDatos():Boolean {
        validacionCampos[validarMail()]=+1
        validacionCampos[validarNombre()]=+1
        validacionCampos[validarApellidos()]=+1
        validacionCampos[validarTelefono()]=+1
        rol=listaRoles[posRol]
        activo=switch_activo.isChecked
        return (validacionCampos[false]==0)
    }

    private fun validarMail():Boolean{
        val dominio="@infotasks.com"
        mail=txtCrearMailTecnico.text.toString().trim()
        return (mail.endsWith(dominio,false) && mail.length > dominio.length)
    }

    private fun validarNombre():Boolean{
        nombre=txtCrearNombreTecnico.text.toString().trim()
        return if(nombre.isEmpty()) {
            mostrarError(txtErrorNombreTecnico, false)
            false
        }else {
            ocultarError(txtErrorNombreTecnico)
            true
        }
    }

    private fun validarApellidos():Boolean{
        apellidos=txtCrearApellidosTecnico.text.toString().trim()
        return if(apellidos.isEmpty()){
            mostrarError(txtErrorApellidosTecnico, false)
            false
        }else{
            ocultarError(txtErrorApellidosTecnico)
            true
        }
    }

    private fun validarTelefono():Boolean{
        telefono=txtCrearTlfCliente.text.toString()
        return if (telefono.isEmpty()){
            mostrarError(txtErrorTelefonoTecnico, false)
            false
        }else if(telefono.length != 9 ){
            mostrarError(txtErrorTelefonoTecnico, true)
            false
        }else{
            ocultarError(txtErrorTelefonoTecnico)
            true
        }
    }






    private fun mostrarError(textView: TextView, errorFormato:Boolean) {
        var msjObligatorio="*Campo obligatorio"
        var msjFormato="*Formato incorrecto"

        if (errorFormato)
            textView.text=msjFormato
        else
            textView.text=msjObligatorio

        textView.visibility= View.VISIBLE
    }

    private fun ocultarError(textView: TextView){
        textView.visibility= View.INVISIBLE
    }
}