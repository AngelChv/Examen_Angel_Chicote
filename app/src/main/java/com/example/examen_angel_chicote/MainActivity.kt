package com.example.examen_angel_chicote

import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.KeyListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.examen_angel_chicote.model.JuegoDePalabras
import com.example.examen_angel_chicote.util.cambiarVocal
import com.example.examen_angel_chicote.util.transformadores
import com.example.examen_angel_chicote.util.transformar

class MainActivity : AppCompatActivity() {
    private val TIEMPO_INICIAL = "3:00"

    private lateinit var juegoDePalabras: JuegoDePalabras

    private lateinit var palabraOriginaL: String
    private lateinit var transformador: (c: Char, pos: Int) -> Any

    private var timer: CountDownTimer? = null

    private lateinit var tag: KeyListener

    private lateinit var tiempoTxt: TextView
    private lateinit var jugarBttn: Button
    private lateinit var puntosTxt: TextView
    private lateinit var palabraModificadaTxt: TextView
    private lateinit var pistaTxt: TextView
    private lateinit var adivinaETxt: TextView
    private lateinit var comprobarBttn: Button
    private lateinit var imagen: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialize()

        jugarBttn.setOnClickListener{jugar()}

        comprobarBttn.setOnClickListener {comprobar()}
    }

    private fun generarPalabra() {
        // Generar palabra original y modificada:
        palabraOriginaL = juegoDePalabras.obtenerPalabra()
        // Guardar transformador:
        // Para comprobar si se descoloca la palabra.
        // Para obtener la pista.
        // Y para calcular el incremento de la puntuación.
        transformador = transformadores.random()
        palabraModificadaTxt.text = palabraOriginaL.transformar(
            transformador == cambiarVocal, // cuando se cambia la vocal tambíen se descoloca la palabra.
            transformador)
        pistaTxt.text = juegoDePalabras.obtenerPista(transformador)
    }

    private fun initialize() { // Ej 4. Comenzar ejecución de la aplicación.
        juegoDePalabras = JuegoDePalabras()
        timer?.cancel()
        tiempoTxt = findViewById(R.id.tiempoTxt)
        jugarBttn = findViewById(R.id.comenzarBttn)
        puntosTxt = findViewById(R.id.puntosTxt)
        palabraModificadaTxt = findViewById(R.id.palabraModificadaTxt)
        pistaTxt = findViewById(R.id.pistaTxt)
        adivinaETxt = findViewById(R.id.adivinaETxt)
        comprobarBttn = findViewById(R.id.comprobarPalabraBttn)
        imagen = findViewById(R.id.imageView)

        palabraModificadaTxt.clearComposingText()
        pistaTxt.clearComposingText()
        tag = adivinaETxt.keyListener
        adivinaETxt.keyListener = null
        comprobarBttn.isEnabled = false
        tiempoTxt.text = TIEMPO_INICIAL
        puntosTxt.text = "Puntos: ${juegoDePalabras.VALOR_INICIAL}"
        // Si limpio la imagen, nunca se llegaría a ver.
        //imagen.setImageDrawable(null)
        jugarBttn.isEnabled = true
    }

    private fun jugar() {
        // Estado inicial:
        adivinaETxt.keyListener = tag
        comprobarBttn.isEnabled = true
        jugarBttn.isEnabled = false
        imagen.setImageDrawable(null)

        generarPalabra()

        // Timer
        timer = object: CountDownTimer((1000 * 60 * 3), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val segundos = (millisUntilFinished / 1000) % 60
                val minutos = (millisUntilFinished / 1000) / 60
                tiempoTxt.text = "$minutos:${if (segundos < 10) "0$segundos" else segundos}"
            }

            override fun onFinish() {
                imagen.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.lose_foreground))
                Toast.makeText(
                    this@MainActivity,
                    "El tiempo se ha acabado, has perdido. Tu puntuación es: ${juegoDePalabras.puntos}",
                    Toast.LENGTH_LONG
                ).show()
                initialize()
            }
        }.start()
    }

    private fun comprobar() {
        var mensaje: String

        // Comprobar palabra:
        if (adivinaETxt.text.toString().isEmpty()) { // No se ha rellenado la palabra
            mensaje = "Introduce una palabra"
        } else if (adivinaETxt.text.toString().lowercase() == palabraOriginaL.lowercase()) {
            // Acertar
            mensaje = "Has acertado la palabra."
            juegoDePalabras.incrementarPuntos(transformador, palabraOriginaL.length)
            generarPalabra()
            adivinaETxt.clearComposingText()
        } else { // Fallar
            mensaje = "Has fallado la palabra."
            juegoDePalabras.decrementarPuntos()
        }
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()

        puntosTxt.text = "Puntos: ${juegoDePalabras.puntos}"

        // Comporbar victoria:
        var fin = false
        if (juegoDePalabras.puntos >= 50) { // Victoria
            imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.win_foreground))
            mensaje = "Has ganado!"
            fin = true
        } else if (juegoDePalabras.puntos < 0) { // Derrota
            imagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.lose_foreground))
            mensaje = "Has perdido!"
            fin = true
        }

        if (fin) {
            Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
            ).show()
            initialize()
        }
    }
}