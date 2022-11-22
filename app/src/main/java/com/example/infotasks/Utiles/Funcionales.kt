package com.example.infotasks.Utiles

import android.content.Context
import android.widget.Toast

object Funcionales {

    fun toast(contexto: Context?, msj:String){
        Toast.makeText(contexto, msj , Toast.LENGTH_SHORT).show()
    }
}