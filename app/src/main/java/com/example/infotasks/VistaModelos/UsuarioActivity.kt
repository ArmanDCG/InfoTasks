package com.example.infotasks.VistaModelos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.infotasks.CrearEditarModelos.CrearUsuario
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.R
import kotlinx.android.synthetic.main.activity_usuario.*

class UsuarioActivity : AppCompatActivity() {
    private val REQUEST_CODE=1
    private lateinit var usuario:Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usuario)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title="Descripción Usuario"

        usuario=intent.getSerializableExtra("usuario") as Usuario
        mostrarDatos()

        btnEditUsuario.setOnClickListener {
            val intentUsuario= Intent(this, CrearUsuario::class.java)
                .putExtra("accion", "editar")
                .putExtra("usuario", usuario)
            startActivityForResult(intentUsuario, REQUEST_CODE)
        }





    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK ){
            usuario=data?.getSerializableExtra("usuarioEditado") as Usuario
            mostrarDatos()
        }
    }
    private fun mostrarDatos() {
        txtVistaMailUsuario.text = usuario.mail
        txtVistaNombreUsuario.text = usuario.nombre
        txtVistaApellidosUsuario.text = usuario.apellidos
        txtVistaTlfUsuario.text = usuario.telefono.toString()
        txtVistaRolUsuario.text = usuario.rol.toString()
        txtVistaActivoUsuario.text= if (usuario.activo) "SI" else "NO"

    }
}