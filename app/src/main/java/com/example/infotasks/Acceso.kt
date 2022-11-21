package com.example.infotasks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.infotasks.Modelo.Usuario
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.android.synthetic.main.acceso.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class Acceso : AppCompatActivity() {

    var accesoCorrecto = false
    var mail:String = ""
    var pass:String = ""
    lateinit var usuario:Usuario


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acceso)

        btnAcceder.setOnClickListener {
            mail=txtID.toString().trim()
            pass=txtPass.toString().trim()

            if (!camposVacios()){
                runBlocking {
                    val job : Job = launch ( context = Dispatchers.Default){
                        accesoCorrecto = FB.autenticar(mail, pass)
                        //usuario=FB.getUsuario(mail)
                    }
                    job.join()
                }
            }


        }



    }

    private fun camposVacios(): Boolean {

        return (mail.isEmpty() || pass.isEmpty())
    }
}