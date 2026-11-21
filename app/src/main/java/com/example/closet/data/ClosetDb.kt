package com.example.closet.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

enum class Categoria(val etiqueta: String) {
    SUPERIOR("Superior"),
    INFERIOR("Inferior"),
    VESTIDO("Vestido"),
    ABRIGO("Abrigo"),
    CALZADO("Calzado"),
    ACCESORIO("Accesorio")
}

@Entity(tableName = "prendas")
data class Prenda(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fotoPath: String,
    val categoria: Categoria,
    val color: Int,              // ARGB: color dominante de la prenda
    val estilo: String = "casual",
    val creadaEn: Long = System.currentTimeMillis()
)

@Dao
interface PrendaDao {
    @Query("SELECT * FROM prendas ORDER BY creadaEn DESC")
    fun observarTodas(): Flow<List<Prenda>>

    @Insert
    suspend fun insertar(prenda: Prenda)

    @Delete
    suspend fun eliminar(prenda: Prenda)
}

@Database(entities = [Prenda::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prendaDao(): PrendaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "closet.db"
                ).build().also { INSTANCE = it }
            }
    }
}
