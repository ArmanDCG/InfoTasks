package com.example.infotasks

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.infotasks.Constantes.RolUsuario
import com.example.infotasks.ListasModelos.ListaTareas
import kotlinx.android.synthetic.main.activity_lista_tareas.*

class Tecnico : AppCompatActivity() {
    private lateinit var fragmentTareas:Fragment
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tecnico)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Tecnico"

        fragmentTareas= ListaTareas(RolUsuario.TECNICO)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.containerLayoutTareas, fragmentTareas )
            .commit()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home-> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}