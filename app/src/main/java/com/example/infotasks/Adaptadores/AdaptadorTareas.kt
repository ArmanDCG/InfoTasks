package com.example.infotasks.Adaptadores

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.R
import com.example.infotasks.TareaActivity
import com.example.infotasks.Utiles.Funcionales.toast
import com.example.salidadeportiva.ConexionBD.FB
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
        return ViewHolder(LayoutInflater.from(contexto).inflate(R.layout.card_view_tarea,parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tarea = this.tareas[position]
        holder.bind( tarea,tareas, contexto, position,this)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val imgCardPrioridad=view.findViewById<ImageView>(R.id.imgCardPrioridad)
        val txtCardTipo=view.findViewById<TextView>(R.id.txtCardTipo)
        val txtCardEstado=view.findViewById<TextView>(R.id.txtCardEstado)
        val txtCardFechCreacion=view.findViewById<TextView>(R.id.txtCardFechCreacion)
        val txtCardUltimaMod= view.findViewById<TextView>(R.id.txtCardUltimaMod)

        @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
        fun bind( tarea:Tarea, listaTareas:ArrayList<Tarea>, contexto: Context, pos:Int, AdaptadorEventos: AdaptadorTareas){

            insertarImagenPrioridad(tarea.prioridad!!)
            txtCardTipo.text = tarea.tipo.toString()
            txtCardEstado.text = tarea.estado.toString()
            pintarEstado(tarea.estado)
            txtCardFechCreacion.text = txtCardFechCreacion.toString()
            if(txtCardUltimaMod!=null)
                txtCardUltimaMod.text = txtCardUltimaMod.toString()

            itemView.setOnClickListener {
                val intentTarea=Intent(contexto, TareaActivity::class.java)
                    .putExtra("tarea", tarea)

                contexto.startActivity(intentTarea)
            }

            itemView.setOnLongClickListener {
                val dialog= AlertDialog.Builder(contexto)
                    .setMessage("¿Deseas borrar la Tarea?")
                    .setCancelable(false)
                    .setTitle("Borrar Tarea")
                    .setPositiveButton("Aceptar"){_,_ ->
                        var borrado=false
                        runBlocking {
                            val job: Job = launch(context = Dispatchers.Default) {
                                borrado= FB.borrarTarea(tarea.id!!)
                            }
                            job.join()
                        }
                        if (borrado){
                            listaTareas.remove(tarea)
                            AdaptadorEventos.notifyDataSetChanged()
                            toast(contexto, "Tarea borrada correctamente")
                        }
                    }.setNegativeButton("Cancelar"){_,_->}
                    .create().show()

                return@setOnLongClickListener true
            }




        }

        @SuppressLint("ResourceAsColor")
        private fun pintarEstado(estado: EstadoTarea) {
            if (estado == EstadoTarea.PENDIENTE){
                txtCardEstado.setBackgroundColor(R.color.verde)
            }else{
                txtCardEstado.setBackgroundColor(R.color.gris)
            }
        }

        private fun insertarImagenPrioridad(prioridad: PrioridadTarea) {
            when(prioridad){
                PrioridadTarea.ALTA -> imgCardPrioridad.setImageResource(R.drawable.flecha_prioridad_alta)
                PrioridadTarea.MEDIA -> imgCardPrioridad.setImageResource(R.drawable.prioridad_media)
                PrioridadTarea.BAJA -> imgCardPrioridad.setImageResource(R.drawable.flecha_prioridad_baja)
            }
        }


    }


}