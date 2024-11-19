package com.example.examen_angel_chicote.model

import com.example.examen_angel_chicote.util.aumentarPosicion
import com.example.examen_angel_chicote.util.cambiarVocal
import com.example.examen_angel_chicote.util.posicionCaracter
import com.example.examen_angel_chicote.util.sustituirCaracter

class JuegoDePalabras() {
    val VALOR_INICIAL: Int = 2
    private var palabras: List<String> = listOf(
        "casa",
        "piso",
        "planta",
        "ramas",
        "cama",
        "sacos",
        "calabaza",
        "trigo",
        "blanco",
        "setas"
    )

    val pistas: List<String> = listOf(
        "Faltan caracteres",
        "Cambio de vocal",
        "Posición del caracter",
        "Aumentar posición del carácter"
    )

    var puntos: Int = VALOR_INICIAL
        set(value) {
            field = value
        }
        get() = field

    constructor(palabras: List<String>) : this() {
        this.palabras = palabras
    }

    fun obtenerPalabra(): String {
        return palabras.random()
    }

    fun obtenerPista(transformador: (c: Char, pos: Int) -> Any): String {
        // Lo hago diferente, en función de la lambda utilizada para modificar la palabra, la cual
        // determina el tipo de pista, eligo el mensaje correspondiente.
        return when (transformador) {
            sustituirCaracter -> pistas[0]
            cambiarVocal -> pistas[1]
            posicionCaracter -> pistas[2]
            aumentarPosicion -> pistas[3]
            else -> "Sin pista"
        }
    }

    fun incrementarPuntos(transformador: (c: Char, pos: Int) -> Any, lenght: Int) {
        val value = when (transformador) {
            cambiarVocal -> 1
            sustituirCaracter -> 2
            posicionCaracter -> 3
            aumentarPosicion -> 4
            else -> 0
        }
        val res = value + lenght
        puntos += if (res > 10) 10 else res
    }

    fun decrementarPuntos() {
        puntos -= 1
    }
}