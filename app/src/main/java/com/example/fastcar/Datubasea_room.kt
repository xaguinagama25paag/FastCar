package com.example.fastcar

import androidx.activity.enableEdgeToEdge
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

class Datubasea_room {
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


    }
    @Database(entities = [Puntuak::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun puntuakDao(): UserDao
    }
    fun onCreate(){
        val db = Room.databaseBuilder(
            applicationContext,
            MainActivity.AppDatabase::class.java, "Puntuak"
        ).allowMainThreadQueries().build()
    puntuakDao = db.puntuakDao()
    }
    }