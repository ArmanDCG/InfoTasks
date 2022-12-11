package com.example.infotasks.CrearEditarModelos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.R
import com.example.infotasks.Utiles.Funcionales.toast
import kotlinx.android.synthetic.main.activity_crear_cliente.*
import kotlinx.android.synthetic.main.activity_crear_usuario.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class CrearUsuario : AppCompatActivity() {

    //Edición
    private lateinit var editarUsuario:Usuario
    private lateinit var accion:String


    private lateinit var nuevoUsuario:Usuario

    private lateinit var mail:String
    private lateinit var pass:String
    private lateinit var confirmarPass:String
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Crear"

        accion=intent.getStringExtra("accion") as String

        listaRoles= arrayListOf(RolUsuario.TECNICO, RolUsuario.ADMINISTRADOR)


        spinnerRol.adapter=ArrayAdapter(this, R.layout.spinner_item, R.id.txtItem, listaRoles)
        spinnerRol.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                posRol=position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                posRol=0
            }

        }
        comprobarAccion()

        btnCrearUsuario.setOnClickListener {
            if (accion=="editar"){
                if( obtenerDatos()) {
                    asignarAtributosEditar()
                    guardarUsuario(editarUsuario, true)
                }
            }else{
                if(obtenerCredenciales() && obtenerDatos()){
                    asignarAtributosCrear()
                    if (registrarUsuario()) {
                        guardarUsuario(nuevoUsuario, false)
                    }
                }
            }
        }

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun asignarAtributosCrear() {
        nuevoUsuario= Usuario(mail, nombre, apellidos, telefono.toInt(), rol, activo)
    }

    private fun asignarAtributosEditar() {
        editarUsuario.let {
            it.nombre=nombre
            it.apellidos=apellidos
            it.telefono=telefono.toInt()
            it.rol=rol
            it.activo=activo
        }
    }

    private fun guardarUsuario(usuario: Usuario, editar: Boolean) {
        var guardado=false
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                guardado = FB.añadirUsuario(usuario)
            }
            job.join()
        }

        if (guardado && editar){
            toast(this, "Usuario editado correctamente")
            setResult(RESULT_OK, Intent().putExtra("usuarioEditado", editarUsuario))

        }else if (guardado && !editar) {
            toast(this, "Usuario creado correctamente")
        }else{
            toast(this, "Hubo un problema de conexión")
        }

        finish()
    }
    private  fun registrarUsuario(): Boolean {
        var registrado=false
        runBlocking {
            val job: Job = launch(context = Dispatchers.Default) {
                 registrado=FB.registrar(mail, pass)
            }
            job.join()
        }
        if (!registrado)
            toast(this, "La cuenta $mail ya está registrada")
        return registrado
    }

    private fun comprobarAccion() {
        if (accion=="editar"){
            supportActionBar!!.title="Editar"
            editarUsuario=intent.getSerializableExtra("usuario") as Usuario
            mostrarDatos()
        }
    }

    private fun mostrarDatos() {
        txtCrearMailTecnico.setText(editarUsuario.mail)
        txtCrearNombreTecnico.setText(editarUsuario.nombre)
        txtCrearApellidosTecnico.setText(editarUsuario.apellidos)
        txtCrearTlfTecnico.setText(editarUsuario.telefono.toString())
        spinnerRol.setSelection(listaRoles.indexOf(editarUsuario.rol))
        switch_activo.isChecked=editarUsuario.activo

        textoMail.visibility=View.GONE
        txtCrearMailTecnico.visibility=View.GONE
        textoPass.visibility=View.GONE
        txtCrearPassTecnico.visibility=View.GONE
        textoConfirmarPass.visibility=View.GONE
        txtCrearPassConfirmarTecnico.visibility=View.GONE

        btnCrearUsuario.text="Guardar"
    }

    private fun obtenerCredenciales(): Boolean {
        validacionCampos= hashMapOf(true to 0, false to 0)

        validacionCampos[validarMail()]=+1
        validacionCampos[validarContraseña()]=+1
        validacionCampos[validarConfirmarContraseña()]=+1
        return (validacionCampos[false]==0)
    }

    private fun obtenerDatos():Boolean {
        validacionCampos= hashMapOf(true to 0, false to 0)

        validacionCampos[validarNombre()]=+1
        validacionCampos[validarApellidos()]=+1
        validacionCampos[validarTelefono()]=+1
        rol=listaRoles[posRol]
        activo=switch_activo.isChecked
        return (validacionCampos[false]==0)
    }

    private fun validarMail():Boolean{
        val dominio="@infotasks.com"
        mail=txtCrearMailTecnico.text.toString().trim().replace(" ", "")

        return if(mail.isEmpty()){
            mostrarError(txtErrorMail, false)
            false
        }else if(!mail.endsWith(dominio,false) || mail.length <= dominio.length){
            mostrarErrorMail(txtErrorMail)
            false
        }else{
            ocultarError(txtErrorMail)
            true
        }
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
        telefono=txtCrearTlfTecnico.text.toString().trim()
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

    private fun validarContraseña():Boolean{
        pass=txtCrearPassTecnico.text.toString().trim()
        return if (pass.isEmpty()){
            mostrarError(txtErrorContraseña, false)
            false
        }else if(pass.length < 8){
            mostrarErrorFormatoPass(txtErrorContraseña)
            false
        }else{
            ocultarError(txtErrorContraseña)
            true
        }
    }

    private fun validarConfirmarContraseña():Boolean{
        confirmarPass=txtCrearPassConfirmarTecnico.text.toString().trim()
        return if (confirmarPass.isEmpty()){
            mostrarError(txtErrorConfirmarContraseña, false)
            false
        }else  if (confirmarPass!=pass) {
            mostrarErrorCoincidenciaPass(txtErrorConfirmarContraseña)
            false
        }else{
            ocultarError(txtErrorConfirmarContraseña)
            true
        }

    }
    private fun mostrarError(textView: TextView, errorFormato:Boolean) {
        val msjObligatorio="*Campo obligatorio"
        val msjFormato="*Formato incorrecto"

        if (errorFormato)
            textView.text=msjFormato
        else
            textView.text=msjObligatorio

        textView.visibility= View.VISIBLE
    }

    private fun mostrarErrorFormatoPass(textView: TextView){
        val msjFormato="*Formato incorrecto. Se requieren 8 caracteres mínimo"
        textView.text=msjFormato
        textView.visibility= View.VISIBLE
    }
    private fun mostrarErrorCoincidenciaPass(textView: TextView){
        val msjError="*Las contraseñas no coinciden"
        textView.text=msjError
        textView.visibility= View.VISIBLE
    }

    private fun mostrarErrorMail(textView: TextView){
        val msjError="Formato incorrecto. Usar este dominio: @infotask.com"
        textView.text=msjError
        textView.visibility= View.VISIBLE
    }



    private fun ocultarError(textView: TextView){
        textView.visibility= View.INVISIBLE
    }
}