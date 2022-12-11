package com.example.infotasks.Utiles

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
object FechaHora {
    private const val ZONE_ID="Europe/Madrid"
    private const val FORMATO_DATE= "dd/MM/yyyy HH:mm"
    private lateinit var fechaActual:ZonedDateTime
    //private val FORMATO_DTF= DateTimeFormatter.ofPattern(FORMATO)
    private val FORMATO_SDF=SimpleDateFormat(FORMATO_DATE)



    fun obtenerFechaActual():Date{
        fechaActual=Instant.now().atZone(ZoneId.of(ZONE_ID))
        var date= Date.from(fechaActual.toInstant())
        Log.e("Fecha/Hora Actual", FORMATO_SDF.format(date))
        return date
    }

    fun dateToString(date: Date):String{
         return try {
             FORMATO_SDF.format(date)
         }catch (ex:NullPointerException){
             Log.e("Error", "No se puede formatear un valor null")
             "null"
         }


    }






    //var date= Date(fechaHora.format(dtf))
    //Log.e("Date", sdf.format(date))

}