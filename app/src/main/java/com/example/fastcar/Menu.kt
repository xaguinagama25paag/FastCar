package com.example.fastcar

import android.annotation.SuppressLint
import android.graphics.Rect
import android.media.MediaPlayer
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.math.pow
import androidx.core.view.isVisible

@SuppressLint("ClickableViewAccessibility", "SetTextI18n")

class Menu : AppCompatActivity(){
    lateinit var botoi: Button
    lateinit var gora: Button
    lateinit var bera: Button
    lateinit var eskuin: Button
    lateinit var esker: Button
    lateinit var denda: Button
    lateinit var abiaduraDenda: Button
    lateinit var inprimaDenda: Button
    lateinit var zenbatDenda: Button
    lateinit var zenbatErosteko: Button
    lateinit var menua: Button

    lateinit var puntuakDao: Datubasea_room.UserDao

    lateinit var kotxea: ImageView
    lateinit var textua: TextView
    lateinit var puntua: ImageView
    lateinit var puntua2: ImageView
    lateinit var puntua3: ImageView
    lateinit var puntua4: ImageView
    lateinit var puntua5: ImageView
    lateinit var botoiArea: View

    lateinit var puntuLista: Array<ImageView>
    var puntuazioa: Int = 0
    var multiplikatzaile: Int = 1
    var abiadura = 1
    var inprimatzailea = 0
    var puntuKopurua = 1
    var trofeoa = 0
    var zenbatErosi: Boolean = false
    val timer: Timer = Timer()
    var timer2: Timer = Timer()
    var ukitzen: Boolean = false
    var direkzioa: Boolean = false
    var datubase: Datubasea_room = Datubasea_room()
    var savefile: Int = 0
    //var prueba: Long = Date().time
    //var databasea: Datubasea_room = Datubasea_room()
    private var musika: MediaPlayer? = null
    var lengua: Int = 0

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = getIntent().getExtras()
        if (extras != null) {
            savefile = extras.getInt("savefile")
        }

        botoi = findViewById(R.id.trofeoa)
        gora = findViewById(R.id.gora)
        bera = findViewById(R.id.behera)
        esker = findViewById(R.id.eskuin)
        eskuin = findViewById(R.id.esker)
        denda = findViewById(R.id.denda)
        abiaduraDenda = findViewById(R.id.abiadura)
        inprimaDenda = findViewById(R.id.inprimaDenda)
        zenbatDenda = findViewById(R.id.zenbatDenda)
        zenbatErosteko = findViewById(R.id.MultiDenda)
        kotxea = findViewById(R.id.kotxea)
        textua = findViewById(R.id.puntuazioa)
        puntua = findViewById(R.id.puntua)
        puntua2 = findViewById(R.id.puntua2)
        puntua3 = findViewById(R.id.puntua3)
        puntua4 = findViewById(R.id.puntua4)
        puntua5 = findViewById(R.id.puntua5)
        menua = findViewById(R.id.savefile1)
        botoiArea = findViewById(R.id.botoiArea)
        menua.setOnClickListener {
            if (botoiArea.isVisible){
                botoi.visibility = View.GONE
                inprimaDenda.visibility = View.GONE
                denda.visibility = View.GONE
                abiaduraDenda.visibility = View.GONE
                botoiArea.visibility = View.GONE

                runOnUiThread {
                    val params = menua.layoutParams as ConstraintLayout.LayoutParams
                    params.leftMargin = -725
                    menua.layoutParams = params
                }
            }else{
                botoi.visibility = View.VISIBLE
                inprimaDenda.visibility = View.VISIBLE
                denda.visibility = View.VISIBLE
                abiaduraDenda.visibility = View.VISIBLE
                botoiArea.visibility = View.VISIBLE
                runOnUiThread {
                    val params = menua.layoutParams as ConstraintLayout.LayoutParams
                    params.leftMargin = 0
                    menua.layoutParams = params
                }
            }
        }


        botoi.setOnClickListener {
            if (puntuazioa>=1000*(1.15*trofeoa)){
                puntuazioa = 0
                multiplikatzaile = 1
                abiadura = 1
                puntuKopurua = 1
                trofeoa++
                inprimatzailea = trofeoa-1
                datubasea(6,trofeoa)
                if (lengua==0) {
                    botoi.text = "Trofeoak: $trofeoa \n Koste: " + 1000 * (1.15 * trofeoa)
                    textua.text = "Puntuazioa: $puntuazioa"
                    denda.text =
                        "Puntu balioa: $multiplikatzaile \n Koste: " + (multiplikatzaile * 5 * 1.2).toInt()
                            .toString()
                    abiaduraDenda.text = "Abiadura: $abiadura \n Koste: " + abiadura * 10
                    inprimaDenda.text = "Inprimatzen: $inprimatzailea \n Koste: " + (100 * 5 * 1.2)
                    zenbatDenda.text =
                        "Puntu kantitatea: $puntuKopurua \n Koste: " + (100.toDouble()
                            .pow(puntuKopurua)).toInt()
                }else{
                    botoi.text = "Trofeos: $trofeoa \n Coste: " + 1000 * (1.15 * trofeoa)
                    textua.text = "Puntuación: $puntuazioa"
                    denda.text =
                        "Valor de puntos: $multiplikatzaile \n Coste: " + (multiplikatzaile * 5 * 1.2).toInt()
                            .toString()
                    abiaduraDenda.text = "Velocidad: $abiadura \n Coste: " + abiadura * 10
                    inprimaDenda.text = "Inprimiendo: $inprimatzailea \n Coste: " + (100 * 5 * 1.2)
                    zenbatDenda.text =
                        "Cantidad de puntos: $puntuKopurua \n Coste: " + (100.toDouble()
                            .pow(puntuKopurua)).toInt()
                }
            }else{
                runOnUiThread{
                    val params = menua.layoutParams as ConstraintLayout.LayoutParams
                    botoi.text = params.leftMargin.toString()


                }
            }
        }
        gora.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        (OnTouchListener { v, event ->
            direkzioa=true
            ukitzen = !ukitzen
            false
        })
        )
        bera.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        (OnTouchListener { v, event ->
            direkzioa=false
            ukitzen = !ukitzen
            false
        })
        )
        eskuin.setOnClickListener {
            giratu(true)
        }
        esker.setOnClickListener {
            giratu(false)
        }
        denda.setOnClickListener {
            if (puntuazioa >= multiplikatzaile * 5 * 1.2) {
                if (zenbatErosi) {
                    while (puntuazioa >= multiplikatzaile * 5 * 1.2) {
                        puntuazioa = puntuazioa - (multiplikatzaile * 5 * 1.2).toInt()
                        multiplikatzaile += 1
                    }
                }else{
                    puntuazioa = puntuazioa - (multiplikatzaile * 5 * 1.2).toInt()
                    multiplikatzaile += 1
                }
                datubasea(1, puntuazioa)
                datubasea(2, multiplikatzaile)
                if (lengua==0) {
                    textua.text = "Puntuazioa: $puntuazioa"
                    denda.text =
                        "Puntu balioa: $multiplikatzaile \n Koste: " + (multiplikatzaile * 5 * 1.2).toInt()
                            .toString()
                }else{
                    textua.text = "Puntuación: $puntuazioa"
                    denda.text =
                        "Valor de puntos: $multiplikatzaile \n Coste: " + (multiplikatzaile * 5 * 1.2).toInt()
                            .toString()
                }
            }

        }
        abiaduraDenda.setOnClickListener {
            if (puntuazioa>=abiadura*10){
                if (zenbatErosi) {
                    while (puntuazioa>=abiadura*10) {
                        puntuazioa = puntuazioa - abiadura * 10
                        abiadura++
                    }
                }else{
                    puntuazioa = puntuazioa - abiadura * 10
                    abiadura++
                }
                datubasea(1,puntuazioa)
                datubasea(3,abiadura)
                if (lengua==0) {
                    textua.text = "Puntuazioa: $puntuazioa"
                    abiaduraDenda.text = "Abiadura: $abiadura \n Koste: " + abiadura * 10
                }else{
                    textua.text = "Puntuación: $puntuazioa"
                    abiaduraDenda.text = "Velocidad: $abiadura \n Coste: " + abiadura * 10
                }

                /* var aei: Long = ( Date().time - prueba)
                 abiaduraDenda.text = (aei/1000).toInt().toString()*/

            }
        }
        inprimaDenda.setOnClickListener {
            when(inprimatzailea){
                0->{
                    if (puntuazioa>=100){
                        puntuazioa = puntuazioa - 100
                        inprimatzailea++
                        datubasea(1,puntuazioa)
                        datubasea(4,inprimatzailea)
                        if (lengua==0) {
                            textua.text = "Puntuazioa: $puntuazioa"
                            inprimaDenda.text =
                                "Inprimatzen: $inprimatzailea \n Koste: " + (100 * 5 * 1.2)
                        }else{
                            textua.text = "Puntuación: $puntuazioa"
                            inprimaDenda.text =
                                "Inprimiendo: $inprimatzailea \n Coste: " + (100 * 5 * 1.2)

                        }
                        timer2.schedule(task2, 0, 1)
                    }
                }
                else->{
                    if (puntuazioa>=inprimatzailea*100*5*1.2){
                        if (zenbatErosi) {
                            while (puntuazioa>=inprimatzailea*100*5*1.2) {
                                puntuazioa =
                                    (puntuazioa - (inprimatzailea * 100 * 5 * 1.2)).toInt()
                                inprimatzailea++
                            }
                        }else{
                            puntuazioa = (puntuazioa - (inprimatzailea * 100 * 5 * 1.2)).toInt()
                            inprimatzailea++
                        }
                        datubasea(1,puntuazioa)
                        datubasea(4,inprimatzailea)
                        if (lengua==0) {
                            textua.text = "Puntuazioa: $puntuazioa"
                            inprimaDenda.text =
                                "Inprimatzen: $inprimatzailea \n Koste: " + (inprimatzailea * 100 * 5 * 1.2)
                        }else{
                            textua.text = "Puntuación: $puntuazioa"
                            inprimaDenda.text =
                                "Inprimiendo: $inprimatzailea \n Coste: " + (inprimatzailea * 100 * 5 * 1.2)
                        }
                    }
                }
            }
        }
        zenbatDenda.setOnClickListener {


            if (puntuazioa >= (100.toDouble().pow(puntuKopurua)).toInt()) {
                if (zenbatErosi) {
                    while (puntuazioa >= (100.toDouble().pow(puntuKopurua)).toInt()) {
                        puntuazioa = puntuazioa - (100.toDouble().pow(puntuKopurua)).toInt()
                        puntuKopurua++
                    }
                } else {
                    puntuazioa = puntuazioa - (100.toDouble().pow(puntuKopurua)).toInt()
                    puntuKopurua++
                }
                datubasea(1, puntuazioa)
                datubasea(5, puntuKopurua)
                puntuLista = arrayOfNulls<ImageView>(puntuKopurua) as Array<ImageView>
                when (puntuKopurua) {
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
                if (lengua == 0) {
                    textua.text = "Puntuazioa: $puntuazioa"
                    zenbatDenda.text =
                        "Puntu kantitatea: $puntuKopurua \n Koste: " + (100.toDouble()
                            .pow(puntuKopurua)).toInt()
                }else{
                    textua.text = "Puntuación: $puntuazioa"
                    zenbatDenda.text =
                        "Cantidad de puntos: $puntuKopurua \n Coste: " + (100.toDouble()
                            .pow(puntuKopurua)).toInt()

                }
            }

        }
        zenbatErosteko.setOnClickListener {
            if (zenbatErosi){
                zenbatErosi = false
                if (lengua == 0) {
                    zenbatErosteko.text = "Zenbat erosi: x1"
                }else{
                    zenbatErosteko.text = "Cuanto comprar: x1"
                }
            }else{
                zenbatErosi = true
                if (lengua == 0) {
                    zenbatErosteko.text = "Zenbat erosi: Max"
                }else{
                    zenbatErosteko.text = "Cuanto comprar: Max"
                }            }
        }
        timer.schedule(task, 50, 50)

        val db = datubase.Datubasea(applicationContext)

        puntuakDao = db.puntuakDao()

        datubasea(0,0)

        musika = MediaPlayer.create(this, R.raw.mundubat)

        musika?.start()
        musika?.isLooping = true

    }

    @OptIn(DelicateCoroutinesApi::class)
    fun datubasea(zein: Int, puntu: Int){
        GlobalScope.launch(Dispatchers.Main) {
            when(zein){
                0->{
                    var lengoa: List<Datubasea_room.Lengoaia> = puntuakDao.getLengoaia()
                    lengua = lengoa.first().component1()
                    if (lengua==1){
                        eskuin.text = "Derecha"
                        esker.text = "Izquierda"
                        gora.text = "Arriba"
                        bera.text = "Abajo"
                        zenbatErosteko.text = "Cuanto comprar: x1"

                    }
                    var puntuazio: List<Datubasea_room.Puntuak> = puntuakDao.getAll()
                    puntuazio.forEach {
                        if (it.component1()==savefile){
                            puntuazioa = it.component2()
                            multiplikatzaile = it.component3()
                            abiadura = it.component4()
                            inprimatzailea = it.component5()

                            if (lengua == 0) {
                                if (inprimatzailea != 0) {
                                    timer2.schedule(task2, 0, 1)
                                    inprimaDenda.text =
                                        "Inprimatzen: $inprimatzailea \n Koste: " + (inprimatzailea * 100 * 5 * 1.2)
                                } else {
                                    inprimaDenda.text = "Inprimatzen: ezer \n Koste: 100"
                                }
                                textua.text = "Puntuazioa: $puntuazioa"
                                denda.text =
                                    "Puntu balioa: $multiplikatzaile \n Koste: " + (multiplikatzaile * 5 * 1.2).toInt()
                                        .toString()
                                abiaduraDenda.text =
                                    "Abiadura: $abiadura \n Koste: " + abiadura * 10
                            }else{
                                if (inprimatzailea != 0) {
                                    timer2.schedule(task2, 0, 1)
                                    inprimaDenda.text =
                                        "Inprimiendo: $inprimatzailea \n Coste: " + (inprimatzailea * 100 * 5 * 1.2)
                                } else {
                                    inprimaDenda.text = "Inprimiendo: nada \n Coste: 100"
                                }
                                textua.text = "Puntuación: $puntuazioa"
                                denda.text =
                                    "Valor de puntos: $multiplikatzaile \n Coste: " + (multiplikatzaile * 5 * 1.2).toInt()
                                        .toString()
                                abiaduraDenda.text =
                                    "Velocidad: $abiadura \n Coste: " + abiadura * 10
                            }

                            puntuKopurua = it.component6()
                            trofeoa = it.component7()
                            puntuLista = arrayOfNulls<ImageView>(puntuKopurua) as Array<ImageView>
                            when(puntuKopurua){
                                1->{
                                    puntuLista[0] = puntua
                                }
                                2->{
                                    puntua2.visibility = View.VISIBLE
                                    puntuLista[0] = puntua
                                    puntuLista[1] = puntua2
                                }
                                3->{
                                    puntua2.visibility = View.VISIBLE
                                    puntua3.visibility = View.VISIBLE
                                    puntuLista[0] = puntua
                                    puntuLista[1] = puntua2
                                    puntuLista[2] = puntua3
                                }
                                4->{
                                    puntua2.visibility = View.VISIBLE
                                    puntua3.visibility = View.VISIBLE
                                    puntua4.visibility = View.VISIBLE
                                    puntuLista[0] = puntua
                                    puntuLista[1] = puntua2
                                    puntuLista[2] = puntua3
                                    puntuLista[3] = puntua4
                                }
                                5->{
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
                            if (lengua ==0) {
                                zenbatDenda.text =
                                    "Puntu kantitatea: $puntuKopurua \n Koste: " + (100.toDouble()
                                        .pow(puntuKopurua)).toInt()
                                botoi.text =
                                    "Trofeoak: $trofeoa \n Koste: " + 1000 * (1.15 * trofeoa)
                            }else{
                                zenbatDenda.text =
                                    "Cantidad de puntos: $puntuKopurua \n Coste: " + (100.toDouble()
                                        .pow(puntuKopurua)).toInt()
                                botoi.text =
                                    "Trofeos: $trofeoa \n Coste: " + 1000 * (1.15 * trofeoa)

                            }


                        }
                    }

                }
                1->{
                    puntuakDao.updateUser(savefile,puntu)
                }
                2->{
                    puntuakDao.updateMulti(savefile,puntu)
                }
                3->{
                    puntuakDao.updateAbiadura(savefile,puntu)
                }
                4->{
                    puntuakDao.updateInprima(savefile,puntu)
                }
                5->{
                    puntuakDao.updateZenbat(savefile,puntu)
                }
                6->{
                    puntuakDao.updateUser(savefile,0)
                    puntuakDao.updateMulti(savefile,1)
                    puntuakDao.updateAbiadura(savefile,1)
                    puntuakDao.updateInprima(savefile,puntu)
                    puntuakDao.updateZenbat(savefile,1)
                    puntuakDao.updateTrofeos(savefile,puntu)
                }
            }

            // puntuakDao.insertAll(Puntuak(uid = 1, puntuak = 2))

        }
    }
    fun mugitu (nora: Boolean){
        runOnUiThread {
            val zenbat = kotxea.rotation
            var i = 0
            var o = 0
            val params = kotxea.layoutParams as ConstraintLayout.LayoutParams
            when {

                zenbat > 89 -> {
                    o = 25+(abiadura*2)

                    //  params.bottomMargin +=1
                }

                zenbat > 59 -> {
                    o = 15+(abiadura*2)
                    i=5+(abiadura*2)
                    //   params.bottomMargin += 2
                }

                zenbat > 29 -> {
                    o = 5+(abiadura*2)
                    i=5+(abiadura*2)
                    //  params.bottomMargin += 2
                }

                zenbat > -1 -> {
                    i=10+(abiadura*2)
                    //   params.bottomMargin += 3
                }

                zenbat > -31 -> {
                    i=5+(abiadura*2)
                    o = -5-(abiadura*2)
                    //  params.bottomMargin +=3
                }

                zenbat > -61 -> {
                    i=5+(abiadura*2)
                    o = -15-(abiadura*2)
                    //  params.bottomMargin += 4
                }

                zenbat > -91 -> {
                    o = -25-(abiadura*2)
                    //  params.bottomMargin += 5
                }


            }
            //kotxea.rotation +=1
            if (nora) {
                params.leftMargin +=o

                params.bottomMargin += i
            }else{
                params.leftMargin -=o
                params.bottomMargin -= i

            }
            //
            //Arriba: bottom: 1100
            //abajo: bottom: -600
            //derecha: left: 2500
            //izquierda: left: -2500
            if (params.bottomMargin>1100){
                params.bottomMargin = -500
            }else if (params.bottomMargin<-600){
                params.bottomMargin = 1000
            }
            if (params.leftMargin>2900){
                params.leftMargin = -2900
            }else if(params.leftMargin<-2900){
                params.leftMargin = 2900
            }
            kotxea.layoutParams = params
            kotxea.requestLayout()
            puntuLista.forEach {
                if(viewsOverlap(kotxea,it)){
                    puntuazioa += if (trofeoa>=3){
                        (multiplikatzaile*2)
                    }else{
                        multiplikatzaile
                    }
                    if (lengua==0){
                        textua.text  = "Puntuazioa: $puntuazioa"
                    }else{
                        textua.text  = "Puntuación: $puntuazioa"
                    }
                    datubasea(1,puntuazioa)

                    val params = it.layoutParams as ConstraintLayout.LayoutParams
                    params.horizontalBias =  (1..1000).random().toFloat()/1000
                    params.verticalBias = (1..1000).random().toFloat()/1000
                    it.layoutParams = params
                }
            }
        }
    }
    fun giratu(aldea: Boolean){

        Log.i("Bistak", viewsOverlap(kotxea,puntua).toString())

        if (aldea && kotxea.rotation<90) {
            kotxea.rotation +=30
        }else if(!aldea && kotxea.rotation>-90){
            kotxea.rotation -=30
        }
    }

    val task = object : TimerTask() {
        override fun run() {
            if (ukitzen) {
                mugitu(direkzioa)
            }
        }
    }
    val task2 = object : TimerTask() {
        override fun run() {
            puntuazioa++
            if (lengua==0) {
                textua.text = "Puntuazioa: $puntuazioa"
            }else{
                textua.text = "Puntuación: $puntuazioa"
            }
            datubasea(1,puntuazioa)
            Thread.sleep((1000/inprimatzailea).toLong())
        }
    }


    private fun viewsOverlap(v1: View, v2: View): Boolean {
        val v1coords = IntArray(2)
        v1.getLocationOnScreen(v1coords)
        val v1w = v1.width
        val v1h = v1.height
        val v1rect = Rect(v1coords[0], v1coords[1], v1coords[0] + v1w, v1coords[1] + v1h)

        val v2coords = IntArray(2)
        v2.getLocationOnScreen(v2coords)
        val v2w = v2.width
        val v2h = v2.height
        val v2rect = Rect(v2coords[0], v2coords[1], v2coords[0] + v2w, v2coords[1] + v2h)

        return v1rect.intersect(v2rect) || v1rect.contains(v2rect) || v2rect.contains(v1rect)
    }
}
/*
Lista de mejoras:
mas puntos
mas velocidad
impresora de puntos
Mas de un punto aparece
 */