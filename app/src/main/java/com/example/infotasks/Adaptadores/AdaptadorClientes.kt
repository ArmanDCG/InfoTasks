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
import com.example.infotasks.ClienteActivity
import com.example.infotasks.Constantes.EstadoTarea
import com.example.infotasks.Constantes.PrioridadTarea
import com.example.infotasks.ListaClientes
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.R
import com.example.infotasks.TareaActivity
import com.example.infotasks.Utiles.Funcionales.toast
import com.example.salidadeportiva.ConexionBD.FB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Text


class AdaptadorClientes(var contexto:Context, var clientes:ArrayList<Cliente>) : RecyclerView.Adapter<AdaptadorClientes.ViewHolder>() {

    companion object{
        //Variable que nos indica si una tarea está seleccionada
        var itemSelect=-1
    }

    override fun getItemCount(): Int{
        return this.clientes.size!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(contexto).inflate(R.layout.card_view_cliente,parent,false))

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind( clientes, contexto, position,this)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtCardNombreCliente=view.findViewById<TextView>(R.id.txtCardNombreCliente)
        val txtCardApellidosCliente=view.findViewById<TextView>(R.id.txtCardApellidosCliente)
        val txtCardDniCliente=view.findViewById<TextView>(R.id.txtCardDniCliente)


        @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
        fun bind(  listaClientes:ArrayList<Cliente>, contexto: Context, pos:Int, AdaptadorEventos: AdaptadorClientes){

            txtCardNombreCliente.text = listaClientes[pos].nombre
            txtCardApellidosCliente.text = listaClientes[pos].apellidos
            txtCardDniCliente.text = listaClientes[pos].dni

            itemView.setOnClickListener {
                val intentTarea=Intent(contexto, ClienteActivity::class.java)
                    .putExtra("cliente", listaClientes[pos])

                contexto.startActivity(intentTarea)
            }

            /*itemView.setOnLongClickListener {
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
            }*/




        }

    }


}