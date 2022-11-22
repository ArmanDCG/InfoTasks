package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.infotasks.Modelo.Usuario


class AdministradorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_administrador)

        var usuario=intent.getSerializableExtra("admin") as Usuario
        var txtWelcome=findViewById<TextView>(R.id.txtWelcom)
        txtWelcome.text=usuario.toString()
    }
}