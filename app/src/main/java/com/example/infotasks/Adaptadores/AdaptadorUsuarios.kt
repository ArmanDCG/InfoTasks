package com.example.infotasks.Adaptadores

import android.annotation.SuppressLint
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
import com.example.infotasks.CrearEditarModelos.CrearUsuario
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.R
import com.example.infotasks.Utiles.Funcionales.toast
import com.example.infotasks.VistaModelos.UsuarioActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdaptadorUsuarios(var contexto:Context, var usuarios:ArrayList<Usuario>): RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder>(), Filterable {
    private lateinit var listaUsuariosFiltrada:ArrayList<Usuario>
    private var listaUsuariosCompleta=usuarios
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorUsuarios.ViewHolder {
        return ViewHolder(LayoutInflater.from(contexto).inflate(R.layout.card_view_cliente,parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(usuarios, contexto, position,this)
    }

    override fun getItemCount(): Int {
        return this.usuarios.size
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                if (constraint?.length == 0 || constraint == null) listaUsuariosFiltrada = listaUsuariosCompleta else {
                    listaUsuariosFiltrada = ArrayList()
                    var textoBusqueda= constraint.toString().toLowerCase().trim()
                    listaUsuariosCompleta.forEach { usuario->
                        if (usuario.nombre!!.toLowerCase().contains(textoBusqueda) ||
                            usuario.apellidos!!.toLowerCase().contains(textoBusqueda) ||
                            usuario.mail!!.toLowerCase().contains(textoBusqueda)
                        )
                            listaUsuariosFiltrada.add(usuario)
                    }
                }
                return  FilterResults().apply { values = listaUsuariosFiltrada }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                usuarios =
                    if (results?.values == null)
                        listaUsuariosCompleta
                    else
                        results.values as ArrayList<Usuario>
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtCardNombreUsuario=view.findViewById<TextView>(R.id.txtCardNombreTecnico)
        val txtCardApellidosUsuario=view.findViewById<TextView>(R.id.txtCardApellidosTecnico)
        val txtCardMailUsuario=view.findViewById<TextView>(R.id.txtCardMailTecnico)

        fun bind(listaUsuarios:ArrayList<Usuario>, contexto: Context, pos:Int, AdaptadorUsuarios: AdaptadorUsuarios ){
            txtCardNombreUsuario.text = listaUsuarios[pos].nombre
            txtCardApellidosUsuario.text = listaUsuarios[pos].apellidos
            txtCardMailUsuario.text = listaUsuarios[pos].mail

            itemView.setOnClickListener {

                    val intentTarea = Intent(contexto, UsuarioActivity::class.java)
                        .putExtra("usuario", listaUsuarios[pos])

                    contexto.startActivity(intentTarea)

            }

            itemView.setOnLongClickListener {
                /*
                    val dialog = AlertDialog.Builder(contexto)
                        .setMessage("¿Deseas borrar el usuario?")
                        .setCancelable(false)
                        .setTitle("Borrar Usuario")
                        .setPositiveButton("Aceptar") { _, _ ->
                            var borrado = false
                            runBlocking {
                                val job: Job = launch(context = Dispatchers.Default) {
                                    borrado = FB.borrarUsuario(listaUsuarios[pos].mail!!)
                                }
                                job.join()
                            }
                            if (borrado) {
                                listaUsuarios.remove(listaUsuarios[pos])
                                AdaptadorUsuarios.notifyDataSetChanged()
                                toast(contexto, "Usuario borrada correctamente")
                            }
                        }.setNegativeButton("Cancelar") { _, _ -> }
                        .create().show()

                 */

                return@setOnLongClickListener true
            }


        }
    }


}