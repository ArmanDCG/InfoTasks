package com.example.infotasks.Adaptadores

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AdaptadorTareas(var contexto:Context, var tareas:ArrayList<Tarea>) : RecyclerView.Adapter<AdaptadorTareas.ViewHolder>() {

    companion object{
        //Variable que nos indica si una tarea está seleccionada
        var itemSelect=-1
    }

    override fun getItemCount(): Int{
        return this.tareas.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(contexto).inflate(R.layout.tareas_card,parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tarea = this.tareas[position]
        holder.bind( tarea, contexto, position,this)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {

        /*val txtNombre=view.findViewById<TextView>(R.id.txtNomEvento)
        val txtAsistentes=view.findViewById<TextView>(R.id.txtAsistentes)
        val txtEstado=view.findViewById<TextView>(R.id.txtEstadoEvento)
        val txtFecha=view.findViewById<TextView>(R.id.txtFechaEvento)
        val txtHora=view.findViewById<TextView>(R.id.txtHoraEvento)

         */

        @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
        fun bind( tarea:Tarea, context: Context, pos:Int, AdaptadorEventos: AdaptadorTareas){

            /*txtNombre.text = even.nombre
            txtAsistentes.text = even.asistentes.size.toString()
            txtFecha.text = even.fecha
            txtHora.text = even.hora

            when(even.estado){
                "pendiente"-> txtEstado.append("Pendiente")
                "activo"-> txtEstado.append("Activo")
                else -> txtEstado.append("Realizado")
            }

            itemView.setOnClickListener {
                val intentConsultaEvento=Intent(context, ConsultaEventoAdministrador::class.java)
                IntentEvento.evento=even
                context.startActivity(intentConsultaEvento)

            }

            itemView.setOnLongClickListener {
                val dialog=AlertDialog.Builder(context)
                    .setMessage("¿Deseas borrar el Evento?\nSe borrarán todos los datos asociados permanentemente.")
                    .setCancelable(false)
                    .setTitle("Borrar")
                    .setPositiveButton("Aceptar"){_,_ ->
                        var borrado=false
                        runBlocking {
                            val job: Job = launch(context = Dispatchers.Default) {
                               borrado=FB.borrarEvento(even.nombre)
                            }
                            job.join()
                        }
                        if (borrado){
                            eventos.removeAt(pos)
                            AdaptadorEventos.notifyDataSetChanged()
                            Utiles.toast(context, "Borrado correctamente")
                        }

                    }.setNegativeButton("Cancelar"){_,_->}
                    .create().show()
                return@setOnLongClickListener true
            }



             */


        }


    }


}