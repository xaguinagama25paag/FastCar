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

class Menu : AppCompatActivity() {
    lateinit var menua: Button
    lateinit var puntuakDao: MainActivity.UserDao


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.menu_printzipala)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        menua = findViewById(R.id.arrow)
        menua.setOnClickListener {

        }

        val db = MainActivity.


        datubasea(0, 0)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun datubasea(zein: Int, puntu: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            when (zein) {
                0 -> {
                    var puntuazio: List<Puntuak> = puntuakDao.getAll()
                    if (puntuazio.isEmpty()) {
                        puntuakDao.insertAll(Puntuak(1, 0, 1, 1, 0, 1, 0))
                        puntuazio = puntuakDao.getAll()
                    }
                    puntuazioa = puntuazio.first().component2()
                    multiplikatzaile = puntuazio.first().component3()
                    abiadura = puntuazio.first().component4()
                    inprimatzailea = puntuazio.first().component5()
                    if (inprimatzailea != 0) {
                        timer2.schedule(task2, 0, 1)
                        inprimaDenda.text =
                            "Inprimatzen: $inprimatzailea \n Coste: " + (inprimatzailea * 100 * 5 * 1.2)
                    } else {
                        inprimaDenda.text = "Inprimatzen: ezer \n Coste: 100"
                    }
                    textua.text = "Puntuazioa: $puntuazioa"
                    denda.text =
                        "Puntu balioa: $multiplikatzaile \n Coste: " + (multiplikatzaile * 5 * 1.2).toInt()
                            .toString()
                    abiaduraDenda.text = "Abiadura: $abiadura \n Coste: " + abiadura * 10


                    puntuKopurua = puntuazio.first().component6()
                    trofeoa = puntuazio.first().component7()
                    puntuLista = arrayOfNulls<ImageView>(puntuKopurua) as Array<ImageView>
                    when (puntuKopurua) {
                        1 -> {
                            puntuLista[0] = puntua
                        }

                        2 -> {
                            puntua2.visibility = View.VISIBLE
                            puntuLista[0] = puntua
                            puntuLista[1] = puntua2
                        }

                        3 -> {
                            puntua2.visibility = View.VISIBLE
                            puntua3.visibility = View.VISIBLE
                            puntuLista[0] = puntua
                            puntuLista[1] = puntua2
                            puntuLista[2] = puntua3
                        }

                        4 -> {
                            puntua2.visibility = View.VISIBLE
                            puntua3.visibility = View.VISIBLE
                            puntua4.visibility = View.VISIBLE
                            puntuLista[0] = puntua
                            puntuLista[1] = puntua2
                            puntuLista[2] = puntua3
                            puntuLista[3] = puntua4
                        }

                        5 -> {
                            puntua2.visibility = View.VISIBLE
                            puntua3.visibility = View.VISIBLE
                            puntua4.visibility = View.VISIBLE
                            puntua5.visibility = View.VISIBLE
                            puntuLista[0] = puntua
                            puntuLista[1] = puntua2
                            puntuLista[2] = puntua3
                            puntuLista[3] = puntua4
                            puntuLista[4] = puntua5
                        }
                    }
                    zenbatDenda.text =
                        "Puntu kantitatea: $puntuKopurua \n Coste: " + (100.toDouble()
                            .pow(puntuKopurua)).toInt()
                    if (trofeoa == 0) {
                        botoi.text = "Trofeoak: $trofeoa \n Coste: 100000000"
                    } else {
                        botoi.text = "Trofeoak: $trofeoa \n Coste: " + 200000000 * trofeoa
                    }


                }

                1 -> {
                    puntuakDao.updateUser(1, puntu)
                }

                2 -> {
                    puntuakDao.updateMulti(1, puntu)
                }

                3 -> {
                    puntuakDao.updateAbiadura(1, puntu)
                }

                4 -> {
                    puntuakDao.updateInprima(1, puntu)
                }

                5 -> {
                    puntuakDao.updateZenbat(1, puntu)
                }

                6 -> {
                    puntuakDao.updateUser(1, 0)
                    puntuakDao.updateMulti(1, 1)
                    puntuakDao.updateAbiadura(1, 1)
                    puntuakDao.updateInprima(1, puntu)
                    puntuakDao.updateZenbat(1, 1)
                    puntuakDao.updateTrofeos(1, puntu)
                }
            }

            // puntuakDao.insertAll(Puntuak(uid = 1, puntuak = 2))

        }
    }
}