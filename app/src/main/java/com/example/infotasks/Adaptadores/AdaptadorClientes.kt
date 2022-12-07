package com.example.infotasks.Adaptadores

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.infotask.ConexionBD.FB
import com.example.infotasks.ClienteActivity
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.R
import com.example.infotasks.Utiles.Funcionales.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdaptadorClientes(var contexto:Context, var clientes:ArrayList<Cliente>, var asignarClienteTarea:Boolean): RecyclerView.Adapter<AdaptadorClientes.ViewHolder>(), Filterable {
    private lateinit var listaClientesFiltrada:ArrayList<Cliente>
    private var listaClientesCompleta=clientes
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorClientes.ViewHolder {
        return ViewHolder(LayoutInflater.from(contexto).inflate(R.layout.card_view_cliente,parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(clientes, contexto, asignarClienteTarea, position,this)
    }

    override fun getItemCount(): Int {
        return this.clientes.size
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                if (constraint?.length == 0 || constraint == null) listaClientesFiltrada = listaClientesCompleta else {
                    listaClientesFiltrada = ArrayList()
                    var textoBusqueda= constraint.toString().toLowerCase().trim()
                    clientes.forEach { cliente->
                        if (cliente.nombre!!.toLowerCase().contains(textoBusqueda) ||
                            cliente.apellidos!!.toLowerCase().contains(textoBusqueda) ||
                            cliente.dni!!.toLowerCase().contains(textoBusqueda)
                        )
                            listaClientesFiltrada.add(cliente)
                    }
                }
                return  FilterResults().apply { values = listaClientesFiltrada }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                clientes =
                    if (results?.values == null)
                        listaClientesCompleta
                    else
                        results.values as ArrayList<Cliente>
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtCardNombreCliente=view.findViewById<TextView>(R.id.txtCardNombreCliente)
        val txtCardApellidosCliente=view.findViewById<TextView>(R.id.txtCardApellidosCliente)
        val txtCardDniCliente=view.findViewById<TextView>(R.id.txtCardDniCliente)

        fun bind(listaClientes:ArrayList<Cliente>, contexto: Context,asignarClienteTarea: Boolean, pos:Int, AdaptadorClientes: AdaptadorClientes ){
            txtCardNombreCliente.text = listaClientes[pos].nombre
            txtCardApellidosCliente.text = listaClientes[pos].apellidos
            txtCardDniCliente.text = listaClientes[pos].dni

            itemView.setOnClickListener {
                if (asignarClienteTarea){

                    val intent= Intent()
                        .putExtra("clienteAsigTarea", listaClientes[pos])
                    (contexto as Activity).setResult(Activity.RESULT_OK, intent)
                    contexto.finish()
                }else {
                    val intentTarea = Intent(contexto, ClienteActivity::class.java)
                        .putExtra("cliente", listaClientes[pos])

                    contexto.startActivity(intentTarea)
                }
            }

            itemView.setOnLongClickListener {
                if(!asignarClienteTarea) {
                    val dialog = AlertDialog.Builder(contexto)
                        .setMessage("¿Deseas borrar el cliente?")
                        .setCancelable(false)
                        .setTitle("Borrar Cliente")
                        .setPositiveButton("Aceptar") { _, _ ->
                            var borrado = false
                            runBlocking {
                                val job: Job = launch(context = Dispatchers.Default) {
                                    borrado = FB.borrarCliente(listaClientes[pos].dni!!)
                                }
                                job.join()
                            }
                            if (borrado) {
                                listaClientes.remove(listaClientes[pos])
                                AdaptadorClientes.notifyDataSetChanged()
                                toast(contexto, "Tarea borrada correctamente")
                            }
                        }.setNegativeButton("Cancelar") { _, _ -> }
                        .create().show()
                }

                return@setOnLongClickListener true
            }
        }
    }


}