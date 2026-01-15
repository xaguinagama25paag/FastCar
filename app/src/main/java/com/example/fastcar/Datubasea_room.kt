package com.example.fastcar

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.math.pow
import androidx.core.view.isVisible

@SuppressLint("ClickableViewAccessibility", "SetTextI18n")

class Datubasea_room : AppCompatActivity(){
    lateinit var puntuakDao: UserDao


    @Entity
    data class Puntuak(
        @PrimaryKey val uid: Int,
        @ColumnInfo(name = "puntuak") val puntuak: Int,
        @ColumnInfo(name = "multi") val multi: Int,
        @ColumnInfo(name = "abiadura") val abiadura: Int,
        @ColumnInfo(name = "inprima") val inprima: Int,
        @ColumnInfo(name = "zenbat") val zenbat: Int,
        @ColumnInfo(name = "trofeo") val trofeo: Int,
    )
    @Entity
    data class Lengoaia(
        @PrimaryKey val lengoaia: Int,
    )
    @Dao
    interface UserDao {
        @Query("SELECT * FROM puntuak")
        fun getAll(): List<Puntuak>

        @Query("SELECT * FROM puntuak WHERE uid IN (:userIds)")
        fun loadAllByIds(userIds: IntArray): List<Puntuak>

        @Query("SELECT * FROM puntuak WHERE puntuak LIKE :first LIMIT 1")
        fun findByName(first: Int): Puntuak

        @Insert
        fun insertAll(vararg puntuazio: Puntuak)

        @Delete
        fun delete(puntuak: Puntuak)

        @Query("UPDATE puntuak SET puntuak=:puntu WHERE uid=:idea")
        fun updateUser(idea: Int, puntu: Int?)

        @Query("UPDATE puntuak SET multi=:puntu WHERE uid=:idea")
        fun updateMulti(idea: Int, puntu: Int?)

        @Query("UPDATE puntuak SET abiadura=:puntu WHERE uid=:idea")
        fun updateAbiadura(idea: Int, puntu: Int?)

        @Query("UPDATE puntuak SET inprima=:puntu WHERE uid=:idea")
        fun updateInprima(idea: Int, puntu: Int?)

        @Query("UPDATE puntuak SET zenbat=:puntu WHERE uid=:idea")
        fun updateZenbat(idea: Int, puntu: Int?)

        @Query("UPDATE puntuak SET trofeo=:puntu WHERE uid=:idea")
        fun updateTrofeos(idea: Int, puntu: Int?)

        @Query("UPDATE lengoaia SET lengoaia=1 WHERE lengoaia=0")
        fun lengoaiaEs()
        @Query("UPDATE lengoaia SET lengoaia=0 WHERE lengoaia=1")
        fun lengoaiaEus()
        @Query("SELECT * FROM lengoaia")
        fun getLengoaia(): List<Lengoaia>

    }
    @Database(entities = [Puntuak::class,Lengoaia::class], version = 2,exportSchema=false)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun puntuakDao(): UserDao
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Puntuak"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        puntuakDao = db.puntuakDao()

    }

}
/*
Lista de mejoras:
mas puntos
mas velocidad
impresora de puntos
Mas de un punto aparece
 */