package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.ListasModelos.ListaClientes
import com.example.infotasks.ListasModelos.ListaTareas
import com.example.infotasks.ListasModelos.ListaUsuarios
import com.example.infotasks.Modelo.Usuario
import kotlinx.android.synthetic.main.activity_administrador.*


class Administrador : AppCompatActivity() {
    lateinit var usuario:Usuario

    lateinit var listaTareas:Fragment
    lateinit var listaTecnicos:Fragment
    lateinit var listaClientes:Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Administrador"

        listaTareas= ListaTareas(RolUsuario.ADMINISTRADOR)
        listaClientes= ListaClientes()
        listaTecnicos= ListaUsuarios()

        navigation_menu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_tareas-> cambiarFragment(listaTareas)
                R.id.item_tecnicos-> cambiarFragment(listaTecnicos)
                R.id.item_clientes-> cambiarFragment(listaClientes)
            }
            return@setOnItemSelectedListener true
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cambiarFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.contenedor_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}