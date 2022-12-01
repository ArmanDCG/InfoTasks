package com.example.salidadeportiva.ConexionBD

import android.util.Log
import com.example.infotasks.Modelo.Cliente
import com.example.infotasks.Modelo.Tarea
import com.example.infotasks.Modelo.Usuario
import com.example.infotasks.Utiles.ConversorQueryAModelo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.coroutines.tasks.await
import java.lang.Exception


//Conexion con FireBase
object FB {
    private val auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    //private val storage = Firebase.storage.reference



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

    //--Obtener---------------------------------------------------------------------
    suspend fun obtenerTareas():ArrayList<Tarea>{
        return try {
           val query = db.collection("tareas")
               .get()
               .await()
            ConversorQueryAModelo.queryATareas(query)
        }catch (ex:Exception){
            arrayListOf()
        }
    }

    suspend fun obtenerClientes():ArrayList<Cliente>{
        return try {
            val query=db.collection("clientes")
                .orderBy("apellidos")
                .orderBy("nombre")
                .get()
                .await()
            ConversorQueryAModelo.queryAClientes(query)
        }catch (ex:Exception){
            Log.e("Error al obtener clientes", ex.localizedMessage)
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

    /*
    //--GET---------------------------------------------------------------
    suspend fun getUsuario(email: String): Usuario? {
        return try{
            val qUsuario= db.collection("usuarios")
                .whereEqualTo("email", email)
                .get()
                .await()
            Utiles.queryToUsuario(qUsuario.first())
        }catch ( e:Exception){ null }
    }

    suspend fun getUsuariosEstandar():ArrayList<Usuario>?{
        return try{
           var query= db.collection("usuarios")
               //.whereEqualTo("rol", "estandar")
               .get().await()
            //Convertimos la query en un array list de usuarios y lo retornamos
            Utiles.queryToArrayUsuarios(query)
        }catch (e:Exception){null}
    }

    suspend fun getEventos():ArrayList<Evento>?{
        return try{
            var query= db.collection("eventos").get().await()
            Log.e("exito", "evnetos")

            Utiles.queryToArrayEventos(query)
        }catch (e:Exception){
            Log.e("error", e.toString())
            null
        }
    }

    suspend fun getImagenes(context: Imagenes, nombreCarpeta: String):ArrayList<Bitmap>{
        val ONE_BYTE:Long=1024*1024
        var imagenes = arrayListOf<Bitmap>()
        var bitmap:Bitmap
        storage.child(nombreCarpeta).listAll()
            .addOnSuccessListener { listResult ->
                for (item in listResult.items) {
                    Log.e("referencia", item.toString())
                    item.getBytes(ONE_BYTE).addOnSuccessListener { byteArray ->
                        //bitmap=MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        //byteToBitmap(byteArray)?.let { imagenes.add(it) }
                        imagenes.add(Utiles.byteToBitmap(byteArray)!!)
                        Log.e("NumeroImagenes", imagenes.size.toString())
                    }.addOnFailureListener {
                     Log.e("Error", "No se pudo guardar en la lista de imagenes")
                    }
                }
            }.await()
        Log.e("NumeroImagenes", imagenes.size.toString())
        return imagenes

    }




    //--SET---------------------------------------------------------------

    suspend fun setNuevoUsuario(usuario:Usuario):Boolean{
        return try {

            db.collection("usuarios")
                .document(usuario.nombre)
                .set(Utiles.userToHashMap(usuario))
                .await()
            true
        }catch (e:Exception){false}
    }

    suspend fun setNuevoEvento(evento: Evento):Boolean{
        return try {
            db.collection("eventos")
                .document(evento.nombre)
                .set(Utiles.eventoToHashMap(evento)).await()
            true
        }catch (e:Exception){false}
    }

    fun setAltaBaja(nombreUsuario: String, valor:Boolean){
        db.collection("usuarios").document(nombreUsuario).update("alta", valor)
    }

    fun setNuevoRol(nombreUsuario: String, rol:String){
        db.collection("usuarios").document(nombreUsuario).update("rol", rol)
    }
    fun setEstadoEvento(nombre:String, estado:String){
        db.collection("eventos").document(nombre).update("estado", estado)
    }
    suspend fun setUpdateEvento(evento: Evento):Boolean{
        return try{
            db.collection("eventos").document(evento.nombre).set(evento).await()
            true
        }catch (e:Exception){false}

    }

    suspend fun setImagen(uri:Uri, nombreCarpeta:String):Boolean{

        return try {
            var  rutaReferencia=storage.child(nombreCarpeta).child(uri.lastPathSegment!!)
            rutaReferencia.putFile(uri).addOnSuccessListener {
                Log.e("Imagen", "Subida corractamente")
            }.await()
            true
        }catch (e:Exception){
            false
        }

    }

    fun setLocalizacionUsuario( evento: Evento){
        db.collection("eventos").document(evento.nombre).update("asistentes", evento.asistentes).addOnSuccessListener {
            Log.e("Matriz", "actualizada ")
        }.addOnFailureListener {
            Log.e("excepción actualizar matriz", it.message.toString())
        }
    }

    //--BORRAR------------------------------------------------------------
    suspend fun borrarEvento(nombre:String):Boolean{
        return try{
            db.collection("eventos").document(nombre).delete().await()
            true
        }catch (e:Exception){false}
    }
    */
}