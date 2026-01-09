package com.example.fastcar

import android.annotation.SuppressLint
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import kotlin.math.pow


class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    var ax: Float = 0.0F
    var ay: Float = 0.0F
    var az: Float = 0.0F
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

    lateinit var puntuakDao: UserDao

    lateinit var kotxea: ImageView
    lateinit var textua: TextView
    lateinit var puntua: ImageView
    lateinit var puntua2: ImageView
    lateinit var puntua3: ImageView
    lateinit var puntua4: ImageView
    lateinit var puntua5: ImageView

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
    var prueba: Long = Date().time
    var databasea: Datubasea_room = Datubasea_room()

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


    @SuppressLint("ClickableViewAccessibility")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorManager?.registerListener(
            this,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        );
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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




        botoi.setOnClickListener {
            if (puntuazioa>=100000000){
                puntuazioa = 0
                multiplikatzaile = 1
                abiadura = 1
                puntuKopurua = 1
                trofeoa++
                inprimatzailea = trofeoa
                datubasea(6,trofeoa)
                botoi.text = "Trofeoak: $trofeoa \n Coste: "+100000000*trofeoa
                textua.text = "Puntuazioa: $puntuazioa"
                denda.text = "Puntu balioa: $multiplikatzaile \n Coste: "+(multiplikatzaile*5*1.2).toInt().toString()
                abiaduraDenda.text = "Abiadura: $abiadura \n Coste: "+abiadura*10
                inprimaDenda.text = "Inprimatzen: $inprimatzailea \n Coste: "+(100*5*1.2)
                zenbatDenda.text = "Puntu kantitatea: $puntuKopurua \n Coste: " +(100.toDouble().pow(puntuKopurua)).toInt()

            }
        }
        gora.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        (OnTouchListener { v, event ->
            direkzioa=true
            if (!ukitzen) {
                ukitzen=true
            }else{
                ukitzen=false
            }
            false
        })
        )
        bera.setOnTouchListener(@SuppressLint("ClickableViewAccessibility")
        (OnTouchListener { v, event ->
            direkzioa=false
            if (!ukitzen) {
                ukitzen=true
            }else{
                ukitzen=false
            }
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
                        textua.text = "Puntuazioa: $puntuazioa"
                        denda.text =
                            "Puntu balioa: $multiplikatzaile \n Coste: " + (multiplikatzaile * 5 * 1.2).toInt()
                                .toString()

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
                textua.text = "Puntuazioa: $puntuazioa"
                abiaduraDenda.text = "Abiadura: $abiadura \n Coste: "+abiadura*10


                var aei: Long = ( Date().time - prueba)
                abiaduraDenda.text = (aei/1000).toInt().toString()

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
                        textua.text = "Puntuazioa: $puntuazioa"
                        inprimaDenda.text = "Inprimatzen: $inprimatzailea \n Coste: "+(100*5*1.2)
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
                            textua.text = "Puntuazioa: $puntuazioa"
                            inprimaDenda.text = "Inprimatzen: $inprimatzailea \n Coste: "+(inprimatzailea*100*5*1.2)
                        }
                }
            }
        }
        zenbatDenda.setOnClickListener {


                    if (puntuazioa >= (100.toDouble().pow(puntuKopurua)).toInt()) {
                        if(zenbatErosi) {
                            while(puntuazioa >= (100.toDouble().pow(puntuKopurua)).toInt()) {
                                puntuazioa = puntuazioa - (100.toDouble().pow(puntuKopurua)).toInt()
                                puntuKopurua++
                            }
                        }else{
                            puntuazioa = puntuazioa - (100.toDouble().pow(puntuKopurua)).toInt()
                            puntuKopurua++
                        }
                        datubasea(1, puntuazioa)
                        datubasea(5, puntuKopurua)
                        puntuLista = arrayOfNulls<ImageView>(puntuKopurua) as Array<ImageView>
                        when(puntuKopurua){
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
                        textua.text = "Puntuazioa: $puntuazioa"
                        zenbatDenda.text = "Puntu kantitatea: $puntuKopurua \n Coste: " +(100.toDouble().pow(puntuKopurua)).toInt()
                }

        }
        zenbatErosteko.setOnClickListener {
            if (zenbatErosi){
                zenbatErosi = false
                zenbatErosteko.text = "Zenbat erosi: x1"
            }else{
                zenbatErosi = true
                zenbatErosteko.text = "Zenbat erosi: Max"
            }
        }
        timer.schedule(task, 50, 50)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Puntuak"
        ).allowMainThreadQueries().build()

        puntuakDao = db.puntuakDao()

        /*   while(i<9)
           {
               val iv = ImageView(this);
               iv.setImageResource(R.drawable.ic_launcher_foreground);
               iv.setPadding(0,0,0,0);
               iv.setId(i*1000+i)

               puntuLista[i] = iv
              var a: androidx.constraintlayout.widget.ConstraintLayout = findViewById(R.id.main)
               a.addView(iv)

               i++
           }*/

        datubasea(0,0)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun datubasea(zein: Int, puntu: Int){
        GlobalScope.launch(Dispatchers.Main) {
            when(zein){
                0->{
                    var puntuazio: List<Puntuak> = puntuakDao.getAll()
                    if (puntuazio.isEmpty()){
                        puntuakDao.insertAll(Puntuak(1,0,1,1,0,1,0))
                        puntuazio = puntuakDao.getAll()
                    }
                    puntuazioa = puntuazio.first().component2()
                    multiplikatzaile = puntuazio.first().component3()
                    abiadura = puntuazio.first().component4()
                    inprimatzailea = puntuazio.first().component5()
                    if (inprimatzailea!=0){
                        timer2.schedule(task2, 0, 1)
                        inprimaDenda.text = "Inprimatzen: $inprimatzailea \n Coste: "+(inprimatzailea*100*5*1.2)
                    }else{
                        inprimaDenda.text = "Inprimatzen: ezer \n Coste: 100"
                    }
                    textua.text = "Puntuazioa: "+puntuazioa
                    denda.text = "Puntu balioa: $multiplikatzaile \n Coste: "+(multiplikatzaile*5*1.2).toInt().toString()
                    abiaduraDenda.text = "Abiadura: $abiadura \n Coste: "+abiadura*10


                    puntuKopurua = puntuazio.first().component6()
                    trofeoa = puntuazio.first().component7()
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
                    zenbatDenda.text = "Puntu kantitatea: $puntuKopurua \n Coste: " + (100.toDouble().pow(puntuKopurua)).toInt()
                    if (trofeoa==0) {
                        botoi.text = "Trofeoak: $trofeoa \n Coste: " + 100000000
                    }else{
                        botoi.text = "Trofeoak: $trofeoa \n Coste: " + 200000000 * trofeoa
                    }


                }
                1->{
                    puntuakDao.updateUser(1,puntu)
                }
                2->{
                    puntuakDao.updateMulti(1,puntu)
                }
                3->{
                    puntuakDao.updateAbiadura(1,puntu)
                }
                4->{
                    puntuakDao.updateInprima(1,puntu)
                }
                5->{
                    puntuakDao.updateZenbat(1,puntu)
                }
                6->{
                    puntuakDao.updateUser(1,0)
                    puntuakDao.updateMulti(1,1)
                    puntuakDao.updateAbiadura(1,1)
                    puntuakDao.updateInprima(1,puntu)
                    puntuakDao.updateZenbat(1,1)
                    puntuakDao.updateTrofeos(1,puntu)
                }
            }

            // puntuakDao.insertAll(Puntuak(uid = 1, puntuak = 2))

        }
    }


    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.i("a", "e")
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // ax = event?.values!![0]
        val x = event?.values!![0]
        ay = event.values!![1]
        az = event.values!![2]
        //  Log.d("ACCEL", "Z = $ax m/s²")
      //  botoi.text = ax.toString()



    }
    fun mugitu (nora: Boolean){
        runOnUiThread {
            var zenbat = kotxea.rotation
            var i = 0
            var o = 0
            val params = kotxea.layoutParams as ConstraintLayout.LayoutParams
            when {

                zenbat > 89 -> {
                    o = 30+(abiadura*3)

                    //  params.bottomMargin +=1
                }

                zenbat > 59 -> {
                    o = 20+(abiadura*3)
                    i=5+(abiadura*3)
                    //   params.bottomMargin += 2
                }

                zenbat > 29 -> {
                    o = 10+(abiadura*3)
                    i=10+(abiadura*3)
                    //  params.bottomMargin += 2
                }

                zenbat > -1 -> {
                    i=15+(abiadura*3)
                    //   params.bottomMargin += 3
                }

                zenbat > -31 -> {
                    i=10+(abiadura*3)
                    o = -10-(abiadura*3)
                    //  params.bottomMargin +=3
                }

                zenbat > -61 -> {
                    i=5+(abiadura*3)
                    o = -20-(abiadura*3)
                    //  params.bottomMargin += 4
                }

                zenbat > -91 -> {
                    o = -30-(abiadura*3)
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
                    puntuazioa+=multiplikatzaile
                    textua.text= "Puntuazioa: "+puntuazioa
                    datubasea(1,puntuazioa)
                    textua.text  = "Puntuazioa: "+puntuazioa

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
        eskuin.text = kotxea.rotation.toString()
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
            textua.text  = "Puntuazioa: "+puntuazioa
            datubasea(1,puntuazioa)
            Thread.sleep((1000/inprimatzailea).toLong())
        }
    }

    private fun Button.setOnTouchListener(l: Boolean) {
        if (l) {
            ax++
        }else{
            ax--
        }
    }
    private fun viewsOverlap(v1: View, v2: View): Boolean {
        val v1_coords = IntArray(2)
        v1.getLocationOnScreen(v1_coords)
        val v1_w = v1.getWidth()
        val v1_h = v1.getHeight()
        val v1_rect = Rect(v1_coords[0], v1_coords[1], v1_coords[0] + v1_w, v1_coords[1] + v1_h)

        val v2_coords = IntArray(2)
        v2.getLocationOnScreen(v2_coords)
        val v2_w = v2.getWidth()
        val v2_h = v2.getHeight()
        val v2_rect = Rect(v2_coords[0], v2_coords[1], v2_coords[0] + v2_w, v2_coords[1] + v2_h)

        return v1_rect.intersect(v2_rect) || v1_rect.contains(v2_rect) || v2_rect.contains(v1_rect)
    }
}
/*
Lista de mejoras:
mas puntos
mas velocidad
impresora de puntos
Mas de un punto aparece
 */