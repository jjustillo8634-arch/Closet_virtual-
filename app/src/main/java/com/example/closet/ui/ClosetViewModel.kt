package com.example.closet.ui

import android.app.Application
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.closet.data.AppDatabase
import com.example.closet.data.Categoria
import com.example.closet.data.Prenda
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class ClosetViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.get(app).prendaDao()

    val prendas: StateFlow<List<Prenda>> = dao.observarTodas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun guardarPrenda(uri: Uri, categoria: Categoria, onListo: () -> Unit) {
        viewModelScope.launch {
            val prenda = withContext(Dispatchers.IO) {
                val archivo = copiarAAlmacenamientoInterno(uri)
                Prenda(
                    fotoPath = archivo.absolutePath,
                    categoria = categoria,
                    color = colorDominante(archivo)
                )
            }
            dao.insertar(prenda)
            onListo()
        }
    }

    fun eliminar(prenda: Prenda) {
        viewModelScope.launch {
            dao.eliminar(prenda)
            withContext(Dispatchers.IO) { File(prenda.fotoPath).delete() }
        }
    }

    // Copia la foto elegida a la carpeta privada de la app (el Uri del picker es temporal).
    private fun copiarAAlmacenamientoInterno(uri: Uri): File {
        val ctx = getApplication<Application>()
        val carpeta = File(ctx.filesDir, "prendas").apply { mkdirs() }
        val destino = File(carpeta, "${UUID.randomUUID()}.jpg")
        ctx.contentResolver.openInputStream(uri)!!.use { entrada ->
            destino.outputStream().use { salida -> entrada.copyTo(salida) }
        }
        return destino
    }

    private fun colorDominante(archivo: File): Int {
        val opciones = BitmapFactory.Options().apply { inSampleSize = 4 }
        val bitmap = BitmapFactory.decodeFile(archivo.absolutePath, opciones)
            ?: return android.graphics.Color.GRAY
        return Palette.from(bitmap).generate().getDominantColor(android.graphics.Color.GRAY)
    }
}
