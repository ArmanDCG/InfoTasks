package com.example.infotask.ConexionBD


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.Utiles.ConversorQueryAModelo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception


//Conexion con FireBase
object FB {
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore

    //--Acceso---------------------------------------------------------------
    suspend fun autenticar(mail:String, pass:String):Boolean {
        return try {
            auth.signInWithEmailAndPassword(mail, pass).await()
            true
        } catch (e: Exception){false}
    }

    suspend fun registrar(email:String, pass:String):Boolean{
        return try {
            auth.createUserWithEmailAndPassword(email, pass).await()
            true
        }catch (e:Exception){false}
    }

    //--Añadir--------------------------------------------------------------

    suspend fun añadirTarea(tarea: Tarea):Boolean{
        tarea.id= obtenerIdTarea()
        return try {
            db.collection("tareas")
                .document(tarea.id!!)
                .set(tarea)
                .await()
            true
        }catch (ex:Exception){
            Log.e("Error al insertar Tarea", ex.localizedMessage)
            false}
    }

    suspend fun añadirCliente(cliente: Cliente):Boolean{
        return try {
            db.collection("clientes")
                .document(cliente.dni!!)
                .set(cliente)
                .await()
            true
        }catch (ex:Exception){
            Log.e("Error al insertarCLiente", ex.localizedMessage)
            false
        }
    }

    suspend fun añadirUsuario(usuario: Usuario):Boolean{
        return try {
            db.collection("usuarios")
                .document(usuario.mail!!)
                .set(usuario)
                .await()
            true
        }catch (ex:Exception){
            Log.e("Error al insertar Usuario", ex.localizedMessage)
            false
        }
    }

    //--Editar---------------------------------------------------------------------
    suspend fun editarTarea(tarea: Tarea):Boolean{
        return try {
            db.collection("tareas")
                .document(tarea.id!!)
                .set(tarea)
                .await()
            true
        }catch (ex:Exception){
            Log.e("Error al editar Tarea", ex.localizedMessage)
            false
        }
    }
    //--Obtener---------------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun obtenerTareas():ArrayList<Tarea>{
        return try {
           val query = db.collection("tareas")
               .orderBy("fechaCreacion", Query.Direction.DESCENDING)
               .get()
               .await()
            ConversorQueryAModelo.queryATareas(query)
        }catch (ex:Exception){
            Log.e("ERROR, al extraer tareas", ex.localizedMessage)
            arrayListOf()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun obtenerTareasPrioridad(prioridad:String):ArrayList<Tarea>{
        return try {
            val query = db.collection("tareas")
                .whereEqualTo("prioridad", prioridad)
                .get()
                .await()
            ConversorQueryAModelo.queryATareas(query)
        }catch (ex:Exception){
            Log.e("ERROR, al extraer tareas", ex.localizedMessage)
            arrayListOf()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun obtenerTareasTipo(tipo:String):ArrayList<Tarea>{
        return try {
            val query = db.collection("tareas")
                .whereEqualTo("tipo", tipo)
                .get()
                .await()
            ConversorQueryAModelo.queryATareas(query)
        }catch (ex:Exception){
            Log.e("ERROR, al extraer tareas", ex.localizedMessage)
            arrayListOf()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun obtenerTareasEstado(estado:String):ArrayList<Tarea>{
        return try {
            val query = db.collection("tareas")
                .whereEqualTo("estado", estado)
                .get()
                .await()
            ConversorQueryAModelo.queryATareas(query)
        }catch (ex:Exception){
            Log.e("ERROR, al extraer tareas", ex.localizedMessage)
            arrayListOf()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun obtenerTareasFecha(direccion: Query.Direction):ArrayList<Tarea>{
        return try {
            val query = db.collection("tareas")
                .orderBy("fechaCreacion", direccion)
                .get()
                .await()
            ConversorQueryAModelo.queryATareas(query)
        }catch (ex:Exception){
            Log.e("ERROR, al extraer tareas", ex.localizedMessage)
            arrayListOf()
        }
    }

    suspend fun obtenerClientes():ArrayList<Cliente>{
        return try {
            val query=db.collection("clientes")
                //.orderBy("apellidos")
                .orderBy("nombre")
                .get()
                .await()
            ConversorQueryAModelo.queryAClientes(query)
        }catch (ex:Exception){
            Log.e("Error al obtener clientes", ex.localizedMessage)
            arrayListOf()
        }
    }
    suspend fun obtenerUsuarios():ArrayList<Usuario>{
        return try {
            val query=db.collection("usuarios")
                //.orderBy("apellidos")
                .orderBy("nombre")
                .get()
                .await()
            ConversorQueryAModelo.queryAUsuarios(query)
        }catch (ex:Exception){
            Log.e("Error al obtener usuarios", ex.localizedMessage)
            arrayListOf()
        }
    }

    suspend fun obtenerUsuario(mail: String): Usuario? {
        return try{
            val qUsuario= db.collection("usuarios")
                .whereEqualTo("mail", mail)
                .get()
                .await()
            ConversorQueryAModelo.queryAUsuario(qUsuario.first())
        }catch ( e:Exception){ null }
    }

    suspend fun obtenerCliente(dni: String): Cliente? {
        return try {
           val  qCliente = db.collection("clientes")
               .whereEqualTo("dni", dni)
               .get()
               .await()
            ConversorQueryAModelo.queryACliente(qCliente.first())
        }catch (ex:Exception){
            null
        }
    }

    fun obtenerIdTarea(): String {
        return db.collection("tareas").document().id
    }

    //--Borrar---------------------------------------------------------------

    suspend fun borrarTarea(idTarea:String):Boolean{
        return try {
            db.collection("tareas")
                .document(idTarea)
                .delete()
                .await()
            true
        }catch (ex:Exception){
            Log.e("NO se pudo borrar la tarea", ex.localizedMessage)
            false
        }
    }

    suspend fun borrarCliente(idCliente:String):Boolean{
        return try {
            db.collection("tareas")
                .document(idCliente)
                .delete()
                .await()
            true
        }catch (ex:Exception){
            Log.e("No se pudo borrar las tareas asosciadas al cliente", ex.localizedMessage)
            false
        }
    }

    suspend fun borrarUsuario(idUsuario:String):Boolean{
        return try {
            db.collection("usuarios")
                .document(idUsuario)
                .delete()
                .await()
            true
        }catch (ex:Exception){
            Log.e("No se pudo borrar el usuario $idUsuario", ex.localizedMessage)
            false
        }
    }

    private suspend fun borrarTareasDeCliente(dniCliente:String) {
        try {
            var tareas = db.collection("tareas")
                .whereEqualTo("idCliente", dniCliente)
                .get()
                .await()

            for (dc in tareas.documentChanges) {
                if (dc.type == DocumentChange.Type.ADDED) {
                    borrarTarea(dc.document["id"].toString())
                }
                true
            }
        }catch (ex:Exception){
                Log.e("Hubo un error al borrar las tareas del cliente $dniCliente", ex.localizedMessage)
        }

        }

    private suspend fun borrarCuenta(){

    }
}



