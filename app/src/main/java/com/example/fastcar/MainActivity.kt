package com.example.fastcar

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.Timer
import java.util.TimerTask

@SuppressLint("ClickableViewAccessibility", "SetTextI18n")

class MainActivity : AppCompatActivity() {

    // partidak gordetzeko fitxategiak
    lateinit var savefileBat: Button
    lateinit var savefileBi: Button
    lateinit var savefileHiru: Button
    //Fitxategien informazioa
    lateinit var saveTextBat: TextView
    lateinit var saveTextBi: TextView
    lateinit var saveTextHiru: TextView
    //Lengoaia aldatzeko botoia
    lateinit var lengoaiaButton: Button
    //datubasearen aldagaiak
    lateinit var puntuakDao: Datubasea_room.UserDao
    var datubase: Datubasea_room = Datubasea_room()
    //datubasearen "boolearra", datubasean int bat bezala dagoena
    var lengua: Int = 0
    //fondoko musika
    private var musika: MediaPlayer? = null
    lateinit var fondoKotxea: ImageView
    val timer: Timer = Timer()
    var checker: Boolean = false
    var zenbat =0
    var non = 0
    var altura = 0



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

        //komponenteak definitu
        savefileBat = findViewById(R.id.savefile1)
        savefileBi = findViewById(R.id.savefile2)
        savefileHiru = findViewById(R.id.savefile3)
        saveTextBat = findViewById(R.id.saveText1)
        saveTextBi = findViewById(R.id.saveText2)
        saveTextHiru = findViewById(R.id.saveText3)
        lengoaiaButton = findViewById(R.id.lengoaia)
        fondoKotxea = findViewById(R.id.kotxea2)

        //listenerrak ezarri datubasearen funtziorako
        savefileBat.setOnClickListener {
            datubasea(1)
        }
        savefileBi.setOnClickListener {
            datubasea(2)
        }
        savefileHiru.setOnClickListener {
            datubasea(3)
        }

        // Lengoaia aldatzeko botoia, beste lengoaiara aldatuko da
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

        timer.schedule(task, 50, 50)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun datubasea(zein: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            when (zein) {
                //Lengoaia lortzeko
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
                    //Lehenengo fitxategian klikatzean, partidara bidaliko du partida horrekin, datuak ez direnean existitzen lehenengo sortuko ditu partidara bidali baino lehen, bigarren eta hirugarren fitxategiarekin berdina da
                    var puntuazio: List<Datubasea_room.Puntuak> = puntuakDao.getAll()
                    if (puntuazio.isEmpty()){
                        puntuakDao.insertAll(Datubasea_room.Puntuak(1, 0, 1, 1, 0, 1, 1))

                    }else{
                        puntuazio = puntuakDao.getAll()
                        var checker = false
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
                        var checker = false
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
                        var checker = false
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
                    // Fitxategien datuak lortzeko, existitzen den fitxategi bakoitzaren lortuko ditu eta aukeratutako hizkuntzan erakutsiko du
                4 -> {
                    val puntuazio: List<Datubasea_room.Puntuak> = puntuakDao.getAll()
                    if (!puntuazio.isEmpty()){
                        var check = 1
                        puntuazio.forEach {
                            if (it.component1() ==check){
                                val textua: String = if (lengua==0) {
                                    "Puntuak: ${it.component2()} \n Balioa: ${it.component3()} \n Abiadura: ${it.component4()} \n Inprimatzailea: ${it.component5()} \n Puntu kantitatea: ${it.component6()} \n Mundua: ${it.component7()}"
                                }else{
                                    "Puntos: ${it.component2()} \n Valor: ${it.component3()} \n Velocidad: ${it.component4()} \n Impresora: ${it.component5()} \n Cantidad de puntos: ${it.component6()} \n Mundo: ${it.component7()}"
                                }
                                    when(check){
                                    1->saveTextBat.text = textua
                                    2->saveTextBi.text = textua
                                    3->saveTextHiru.text = textua
                                }
                            }
                            check++
                        }
                        var o = 3
                        while(o>puntuazio.count()){
                            val textua: String = if (lengua==0) {
                                "Ez dira partida honen datuak aurkitu"
                            }else{
                                "No se han encontrado datos de esta partida"
                            }
                            when(o){
                                1->saveTextBat.text = textua
                                2->saveTextBi.text = textua
                                3->saveTextHiru.text = textua
                            }
                            o--
                        }
                    }
                }
            }
        }
    }

    //Partidara bidaltzeko funtzioa, aukeratu den fitxategiaren zenbakia gordeko du
    suspend fun mainera(savefile: Int){
        delay(1000)
        musika?.stop()
        val switchActivityIntent = Intent(this, Menu::class.java)
        switchActivityIntent.putExtra("savefile",savefile)
        startActivity(switchActivityIntent)
    }
    val task = object : TimerTask() {
        override fun run() {
                mugitu()
        }
    }

    fun mugitu (){
        runOnUiThread {
            if(!checker) {
                zenbat = (1..4).random()
                non = (1..2).random()
                altura = (-100..1000).random()
                val params = fondoKotxea.layoutParams as ConstraintLayout.LayoutParams

                when (zenbat) {

                    1 -> fondoKotxea.setImageResource(R.drawable.kotxea)
                    2 -> fondoKotxea.setImageResource(R.drawable.kotxea2)
                    3 -> fondoKotxea.setImageResource(R.drawable.kotxea3)
                    4 -> fondoKotxea.setImageResource(R.drawable.kotxea4)
                }
                params.topMargin = altura
                when (non) {

                    1 -> {
                        params.leftMargin = -2000
                        fondoKotxea.rotation = 90F
                        fondoKotxea.layoutParams = params
                        }


                    2 -> {
                        params.leftMargin = 2900
                        fondoKotxea.rotation = 270F
                        fondoKotxea.layoutParams = params
                    }

                }
                checker = true
            }else {
                val params = fondoKotxea.layoutParams as ConstraintLayout.LayoutParams
                when (non) {

                    1 -> {
                        if (params.leftMargin > 2900) {
                        checker = false
                        }else{
                            params.leftMargin += 40
                            fondoKotxea.layoutParams = params
                        }
                    }


                    2 -> {
                        if (params.leftMargin < -2000) {
                            checker = false
                        }else{
                            params.leftMargin -= 40
                            fondoKotxea.layoutParams = params
                        }
                    }
                }
            }
        }
    }
}