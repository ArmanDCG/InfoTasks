package com.example.infotasks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.infotasks.Modelo.Usuario
import kotlinx.android.synthetic.main.activity_administrador.*


class AdministradorActivity : AppCompatActivity() {
    lateinit var usuario:Usuario
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)


        txtWelcom.text=usuario.toString()

        btnTareas.setOnClickListener {
            val intent= Intent(this, ListaTareasActivity::class.java)
            startActivity(intent)
        }
    }
}