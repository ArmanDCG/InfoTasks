package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
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

        listaTareas=ListaTareas()
        listaClientes=ListaClientes()
        listaTecnicos=ListaUsuarios()

        navigation_menu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_tareas-> cambiarFragment(listaTareas)
                R.id.item_tecnicos-> cambiarFragment(listaTecnicos)
                R.id.item_clientes-> cambiarFragment(listaClientes)
            }
            return@setOnItemSelectedListener true
        }

    }

    private fun cambiarFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.contenedor_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }
}