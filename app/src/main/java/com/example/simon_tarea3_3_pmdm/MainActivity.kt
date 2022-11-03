package com.example.simon_tarea3_3_pmdm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //Botones de colores
    var btnAmarillo: ImageButton? = null
    var btnAzul: ImageButton? = null
    var btnVerde: ImageButton? = null
    var btnRosa: ImageButton? = null
    //Contadores de puntuacion

    //Otras variables
    var acertado = false
    var contRonda: Int = 0
    var contPulsados: Int = 0
    var btnPulsados: ArrayList<Int>? = null
    var coloresMostrados: ArrayList<Int>? = null
    val coloresBrillantes: Array<Int> = arrayOf(
        R.drawable.btn_amarillo_birillante_shape,
        R.drawable.btn_azul_birillante_shape,
        R.drawable.btn_verde_birillante_shape,
        R.drawable.btn_rosa_birillante_shape
    )
    val coloresApagados: Array<Int> = arrayOf(
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

        btnAmarillo?.setOnClickListener(this)
        btnAzul?.setOnClickListener(this)
        btnVerde?.setOnClickListener(this)
        btnRosa?.setOnClickListener(this)

        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage("¿Desea comenzar el juego?")
            .setPositiveButton("Sí") { _, _ ->
                Log.d("Dialog", "---------------- Sí ----------------")
                juego()
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
    private fun resaltaColor(color: Int, btnColor: ImageButton) {
        val handler = Handler(Looper.myLooper()!!)
        btnColor.setImageResource(coloresBrillantes[color])
        handler.postDelayed({ btnColor.setImageResource(coloresApagados[color]) }, 1000)
    }

    /**
     * Elige un color con Random y llama al metodo resaltaColor pasando el boton correspondiente
     */
    private fun secuenciaColores() {
        var color: Int
        do {
            color = Random.nextInt(0, 3)
            coloresMostrados?.add(color)
            when (color) {
                0 -> resaltaColor(color, btnAmarillo!!)
                1 -> resaltaColor(color, btnAzul!!)
                2 -> resaltaColor(color, btnVerde!!)
                3 -> resaltaColor(color, btnRosa!!)
            }
        } while (coloresMostrados?.lastIndex!! < contRonda)
    }

    private fun comprobarColoresElegidos() {
        var i = 0
        while (i < contRonda && acertado) {
            if (btnPulsados!![i] != coloresMostrados!![i]) acertado = false
            i++
        }
    }

    private fun juego() {
        acertado = true
        contRonda++
        secuenciaColores()
        habilitarBtn()
        if (contPulsados>=contRonda) {
            inhabilitarBtn()
            comprobarColoresElegidos()
        }
        comienzaRonda()
    }

    private fun comienzaRonda(){
        btnPulsados = null
        coloresMostrados = null
        if (!acertado) {
            //Salta una alerta para preguntar si se desea comenzar el juego, si se responde sí, comienza el juego, sino, se cierra la app
            AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("¿Desea volver a jugar?")
                .setPositiveButton("Sí") { _, _ ->
                    Log.d("Dialog", "---------------- Sí ----------------")
                    contRonda = 0
                    juego()
                }
                .setNegativeButton("No") { _, _ ->
                    Log.d("Dialog", "---------------- No ----------------")
                    finish()
                }
                .create()
                .show()
        }else if (acertado&&contPulsados>=contRonda) {
            contPulsados = 0
            juego()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id){
            btnAmarillo?.id->{
                resaltaColor(0, btnAmarillo!!)
                btnPulsados?.add(0)
            }
            btnAzul?.id->{
                resaltaColor(1, btnAzul!!)
                btnPulsados?.add(1)
            }
            btnVerde?.id->{
                resaltaColor(2, btnVerde!!)
                btnPulsados?.add(2)
            }
            btnRosa?.id->{
                resaltaColor(3, btnRosa!!)
                btnPulsados?.add(3)
            }
        }
        contPulsados ++
    }
}