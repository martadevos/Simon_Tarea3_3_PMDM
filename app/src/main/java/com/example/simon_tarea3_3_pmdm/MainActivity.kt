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
    private var btnAmarillo: ImageButton? = null
    private var btnAzul: ImageButton? = null
    private var btnVerde: ImageButton? = null
    private var btnRosa: ImageButton? = null
    //Contadores de puntuacion

    //Otras variables
    private var acertado = false
    private var contRonda = 0
    private var contPulsados = 0
    private var btnPulsados: ArrayList<Int>? = ArrayList()
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

        btnAmarillo?.setOnClickListener(this)
        btnAzul?.setOnClickListener(this)
        btnVerde?.setOnClickListener(this)
        btnRosa?.setOnClickListener(this)

        AlertDialog.Builder(this)
            .setTitle("")
            .setMessage("¿Desea comenzar el juego?")
            .setPositiveButton("Sí") { _, _ ->
                Log.d("Dialog", "---------------- Sí ----------------")
                comienzaRonda()
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
    //NO SE COMO HAC ER PARA QUE ME MUESTRE LOS COLORES UNO A UNO EN ORDEN Y NO TODOS A LA VEZ
    private fun secuenciaColores() {
        val color: Int = Random.nextInt(0, 3)
        coloresMostrados?.add(color)
        for (i  in 0 until coloresMostrados?.size!!) {
            when (coloresMostrados!![i]) {
                0 -> resaltaColor(0, btnAmarillo!!)
                1 -> resaltaColor(1, btnAzul!!)
                2 -> resaltaColor(2, btnVerde!!)
                3 -> resaltaColor(3, btnRosa!!)
            }
        }
    }
//NO SE XQ ME COMPRUEBA SOLO CON EL ULTIMO BOTON PULSADO
    private fun comprobarColoresElegidos() {
        var i = 0
        do {
            acertado = btnPulsados!![i] == coloresMostrados!![i]
            i++
        } while (i < contRonda && acertado)
    }


    private fun comienzaRonda(){
        if (!acertado) {
            //Salta una alerta para preguntar si se desea comenzar el juego, si se responde sí, comienza el juego, sino, se cierra la app
            AlertDialog.Builder(this)
                .setTitle("")
                .setMessage("¿Desea volver a jugar?")
                .setPositiveButton("Sí") { _, _ ->
                    Log.d("Dialog", "---------------- Sí ----------------")
                    contRonda = 1
                    btnPulsados= ArrayList()
                    coloresMostrados= ArrayList()
                    secuenciaColores()
                    habilitarBtn()
                }
                .setNegativeButton("No") { _, _ ->
                    Log.d("Dialog", "---------------- No ----------------")
                    finish()
                }
                .create()
                .show()
        }else if (contPulsados>=contRonda) {
            contRonda++
            secuenciaColores()
            habilitarBtn()
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
        if (contPulsados>=contRonda) {
            inhabilitarBtn()
            comprobarColoresElegidos()

            comienzaRonda()
        }
    }
}