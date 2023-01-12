package com.example.simon_tarea3_3_pmdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.awaitAll
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //Botones de colores
    private var btnAmarillo: ImageButton? = null
    private var btnAzul: ImageButton? = null
    private var btnVerde: ImageButton? = null
    private var btnRosa: ImageButton? = null
    //Contadores de puntuacion

    private var tvRonda: TextView? = null

    //Otras variables
    private var acertado = false
    private var colorMostrandose = false
    private var contRonda = 1
    private var contPulsados = 0
    private var coloresMostrados: ArrayList<Int>? = ArrayList()
    private val coloresBrillantes: Array<Int> = arrayOf(
        R.drawable.btn_amarillo_birillante_shape,
        R.drawable.btn_azul_birillante_shape,
        R.drawable.btn_verde_birillante_shape,
        R.drawable.btn_rosa_birillante_shape
    )
    private val coloresApagados: Array<Int> = arrayOf(
        R.drawable.btn_amarillo_shape,
        R.drawable.btn_azul_shape,
        R.drawable.btn_verde_shape,
        R.drawable.btn_rosa_shape
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Asignamos los botones a las variables
        btnAmarillo = findViewById(R.id.btnAmarillo)
        btnAzul = findViewById(R.id.btnAzul)
        btnVerde = findViewById(R.id.btnVerde)
        btnRosa = findViewById(R.id.btnRosa)


        tvRonda = findViewById(R.id.tvRonda)

        btnAmarillo?.setOnClickListener(this)
        btnAzul?.setOnClickListener(this)
        btnVerde?.setOnClickListener(this)
        btnRosa?.setOnClickListener(this)

        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage("¿Desea comenzar el juego?")
            .setPositiveButton("Sí") { _, _ ->
                Log.d("Dialog", "---------------- Sí ----------------")
                tvRonda?.text = contRonda.toString()
                secuenciaColores()
                habilitarBtn()
            }
            .setNegativeButton("No") { _, _ ->
                Log.d("Dialog", "---------------- No ----------------")
                finish()
            }
            .create()
            .show()
    }

    /**
     * Habilita la posibilidad de pulsar los botones
     */
    private fun habilitarBtn() {
        btnAmarillo?.isClickable = true
        btnAzul?.isClickable = true
        btnVerde?.isClickable = true
        btnRosa?.isClickable = true
    }

    /**
     * Deshabilita la posibilidad de pulsar los botones
     */
    private fun inhabilitarBtn() {
        btnAmarillo?.isClickable = false
        btnAzul?.isClickable = false
        btnVerde?.isClickable = false
        btnRosa?.isClickable = false
    }

    /**
     * Cambia la imagen del boton enviado a su versión brillante y espera 1 segundo para volver a poner la normal
     * Precondición: el entero introducido debe estar entre 0 y 3
     * @param color entero entre 0 y 3 que corresponde al color que se quiere cambiar
     * @param btnColor ImageButton que se quiere cambiar
     */
    private fun resaltaColor(color: Int, btnColor: ImageButton, tiempo : Long) {
        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({ btnColor.setImageResource(coloresBrillantes[color]) }, tiempo)
        handler.postDelayed({ btnColor.setImageResource(coloresApagados[color]) }, 500+tiempo)
    }

    /**
     * Elige un color con Random y llama al metodo resaltaColor pasando el boton correspondiente
     */
    private fun secuenciaColores() {
        val color: Int = (0..3).random(Random(System.nanoTime()))
        var tiempo : Long = 0
        coloresMostrados?.add(color)
        inhabilitarBtn()
        for (i in 0 until coloresMostrados?.size!!) {
            when (coloresMostrados!![i]) {
                0 -> resaltaColor(0, btnAmarillo!!, tiempo)
                1 -> resaltaColor(1, btnAzul!!, tiempo)
                2 -> resaltaColor(2, btnVerde!!, tiempo)
                3 -> resaltaColor(3, btnRosa!!, tiempo)
            }
            tiempo += 600
        }
    }

    /**
     * Recibe un entero que corresponde al color pulsado y asigna verdadero o falso
     * a la variable acertado en función de si el color pulsado es igual que el mostrado
     * @param btnPulsado Entero correspondiente al botón pulsado
     */
    private fun comprobarColoresElegidos(btnPulsado: Int) {
        acertado = btnPulsado == coloresMostrados!![contPulsados]
    }

    /**
     * Si se ha fallado la última ronda pregunta si desea volver a jugar y
     * empieza una ronda nueva reseteando el juego; si se ha acertado,
     * combprueba que se hayan pulsado tantos botones como colores se hayan
     * mostrado y si es así, comienza una nueva ronda reseteando los pulsados y
     * actualizando el letrero de ronda
     */
    private fun comienzaRonda() {
        if (!acertado) {
            //Salta una alerta para preguntar si se desea comenzar el juego, si se responde sí, comienza el juego, sino, se cierra la app
            AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("¿Desea volver a jugar?")
                .setPositiveButton("Sí") { _, _ ->
                    Log.d("Dialog", "---------------- Sí ----------------")
                    contRonda = 1
                    tvRonda?.text = contRonda.toString()
                    coloresMostrados = ArrayList()
                    contPulsados = 0
                    secuenciaColores()
                    habilitarBtn()
                }
                .setNegativeButton("No") { _, _ ->
                    Log.d("Dialog", "---------------- No ----------------")
                    finish()
                }
                .create()
                .show()
        } else if (contPulsados >= contRonda) {
            contRonda++
            contPulsados = 0
            tvRonda?.text = contRonda.toString()
            secuenciaColores()
            habilitarBtn()
        }
    }

    /**
     * Comprueba qué botón se ha pulsado y llama al método de comprobación pasando el
     * entero correspondiente, aumenta el contador de pulsados y llama al método para
     * comenzar nueva ronda
     */
    override fun onClick(p0: View?) {
        when (p0?.id) {
            btnAmarillo?.id -> {
                comprobarColoresElegidos(0)
            }
            btnAzul?.id -> {
                comprobarColoresElegidos(1)
            }
            btnVerde?.id -> {
                comprobarColoresElegidos(2)
            }
            btnRosa?.id -> {
                comprobarColoresElegidos(3)
            }
        }
        contPulsados++
        comienzaRonda()
    }
}