package com.example.infotasks.Utiles

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
object FechaHora {
    private const val SEPARADOR=';'
    private const val ZONE_ID="Europe/Madrid"
    private const val STR_FORMATO="dd/MM/yyyy"
    private lateinit var fechaActual:ZonedDateTime
    private val FORMATO_DTF= DateTimeFormatter.ofPattern(STR_FORMATO)
    private val FORMATO_SDF=SimpleDateFormat(STR_FORMATO)



    fun obtenerFechaActual():Date{
        fechaActual=Instant.now().atZone(ZoneId.of(ZONE_ID))
        var date= Date(fechaActual.format(FORMATO_DTF))
        Log.e("Fecha/Hora Actual", FORMATO_SDF.format(date))
        return date
    }






    //var date= Date(fechaHora.format(dtf))
    //Log.e("Date", sdf.format(date))

}