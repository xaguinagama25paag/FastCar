package com.example.fastcar

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@SuppressLint("ClickableViewAccessibility", "SetTextI18n")

class MainActivity : AppCompatActivity() {
    lateinit var savefileBat: Button
    lateinit var savefileBi: Button
    lateinit var savefileHiru: Button
    lateinit var saveTextBat: TextView
    lateinit var saveTextBi: TextView
    lateinit var saveTextHiru: TextView
    lateinit var lengoaiaButton: Button

    lateinit var puntuakDao: Datubasea_room.UserDao
    var datubase: Datubasea_room = Datubasea_room()
    var lengua: Int = 0
    private var musika: MediaPlayer? = null


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
        savefileBat = findViewById(R.id.savefile1)
        savefileBi = findViewById(R.id.savefile2)
        savefileHiru = findViewById(R.id.savefile3)
        saveTextBat = findViewById(R.id.saveText1)
        saveTextBi = findViewById(R.id.saveText2)
        saveTextHiru = findViewById(R.id.saveText3)
        lengoaiaButton = findViewById(R.id.lengoaia)

        savefileBat.setOnClickListener {
            datubasea(1)
        }
        savefileBi.setOnClickListener {
            datubasea(2)
        }
        savefileHiru.setOnClickListener {
            datubasea(3)
        }

        lengoaiaButton.setOnClickListener {
            if (lengua==0) {
               lengua= 1
                puntuakDao.lengoaiaEs()
                lengoaiaButton.text = "Español"
            }else{
                lengua = 0
                puntuakDao.lengoaiaEus()
                lengoaiaButton.text = "Euskera"
            }
            datubasea(4)
        }

        val db = datubase.Datubasea(applicationContext)

        puntuakDao = db.puntuakDao()
        datubasea(0)
        datubasea(4)

        musika = MediaPlayer.create(this, R.raw.menu)

        musika?.start()
        musika?.isLooping = true


    }

    @OptIn(DelicateCoroutinesApi::class)
    fun datubasea(zein: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            when (zein) {
                0 -> {
                    var lengoa: List<Datubasea_room.Lengoaia> = puntuakDao.getLengoaia()
                    if (lengoa.isEmpty()) {
                        puntuakDao.insertLen(Datubasea_room.Lengoaia(0))
                        lengoa = puntuakDao.getLengoaia()
                    }

                    lengua = lengoa.first().component1()
                    if (lengua==1){
                        lengoaiaButton.text = "Español"
                    }
                }
                1 -> {
                    var puntuazio: List<Datubasea_room.Puntuak> = puntuakDao.getAll()
                    if (puntuazio.isEmpty()){
                        puntuakDao.insertAll(Datubasea_room.Puntuak(1, 0, 1, 1, 0, 1, 1))

                    }else{
                        puntuazio = puntuakDao.getAll()
                        var checker: Boolean = false
                        puntuazio.forEach {
                            if (it.component1()==1){
                                checker = true
                            }
                        }
                        if (!checker){
                            puntuakDao.insertAll(Datubasea_room.Puntuak(1, 0, 1, 1, 0, 1, 1))
                        }
                    }
                    runBlocking {
                        mainera(1)
                    }

                }

                2 -> {
                    var puntuazio: List<Datubasea_room.Puntuak> = puntuakDao.getAll()
                    if (puntuazio.isEmpty()){
                        puntuakDao.insertAll(Datubasea_room.Puntuak(2, 0, 1, 1, 0, 1, 1))

                    }else{
                        puntuazio = puntuakDao.getAll()
                        var checker: Boolean = false
                        puntuazio.forEach {
                            if (it.component1()==2){
                                checker = true
                            }
                        }
                        if (!checker){
                            puntuakDao.insertAll(Datubasea_room.Puntuak(2, 0, 1, 1, 0, 1, 1))
                        }
                    }
                    runBlocking {
                        mainera(2)
                    }
                }

                3 -> {
                    var puntuazio: List<Datubasea_room.Puntuak> = puntuakDao.getAll()
                    if (puntuazio.isEmpty()){
                        puntuakDao.insertAll(Datubasea_room.Puntuak(3, 0, 1, 1, 0, 1, 1))

                    }else{
                        puntuazio = puntuakDao.getAll()
                        var checker: Boolean = false
                        puntuazio.forEach {
                            if (it.component1()==3){
                                checker = true
                            }
                        }
                        if (!checker){
                            puntuakDao.insertAll(Datubasea_room.Puntuak(3, 0, 1, 1, 0, 1, 1))
                        }
                    }
                    runBlocking {
                        mainera(3)
                    }
                }

                4 -> {
                    var puntuazio: List<Datubasea_room.Puntuak> = puntuakDao.getAll()
                    if (!puntuazio.isEmpty()){
                        var check: Int = 1
                        puntuazio.forEach {
                            if (it.component1() ==check){
                                var textua: String
                                if (lengua==0) {
                                    textua = "Puntuak: ${it.component2()} \n Balioa: ${it.component3()} \n Abiadura: ${it.component3()} \n Inprimatzailea: ${it.component4()} \n Puntu kantitatea: ${it.component5()} \n Trofeoak: ${it.component6()}"
                                }else{
                                    textua = "Puntos: ${it.component2()} \n Valor: ${it.component3()} \n Velocidad: ${it.component3()} \n Impresora: ${it.component4()} \n Cantidad de puntos: ${it.component5()} \n Trofeos: ${it.component6()}"
                                }
                                    when(check){
                                    1->saveTextBat.text = textua
                                    2->saveTextBi.text = textua
                                    3->saveTextHiru.text = textua
                                }
                            }
                            check++
                        }

                    }
                }

                5 -> {
                }

                6 -> {
                }
            }

            // puntuakDao.insertAll(Puntuak(uid = 1, puntuak = 2))

        }
    }
    suspend fun mainera(savefile: Int){
        delay(1000)
        musika?.stop()
        val switchActivityIntent: Intent = Intent(this, Menu::class.java)
        switchActivityIntent.putExtra("savefile",savefile)
        startActivity(switchActivityIntent)
    }
}