package com.example.examen_angel_chicote.util

fun String.transformar(descolocada: Boolean, transformador: (c: Char, pos: Int) -> Any): String {
    val palabra: MutableList<Any> = mutableListOf()
    for (i in indices) {
        palabra += transformador(this[i], i)
    }

    if (descolocada) palabra.shuffle()
    return palabra.joinToString("")
}

val sustituirCaracter: (c: Char, pos: Int) -> Char = { c: Char, pos: Int ->
    if ((1..10).random() <= 4) '_' else c
}

val cambiarVocal: (c: Char, pos: Int) -> Char = { c: Char, pos: Int ->
    when (c) {
        'a' -> 'e'
        'e' -> 'i'
        'i' -> 'o'
        'o' -> 'u'
        'u' -> 'a'
        else -> c
    }
}

val posicionCaracter: (c: Char, pos: Int) -> Any = { c: Char, pos: Int ->
    if (pos % 2 == 0) (c.lowercaseChar().code - 'a'.code+1) else c
}

val aumentarPosicion: (c: Char, pos: Int) -> Char = { c: Char, pos: Int ->
    if (pos % 2 != 0) (c.lowercaseChar().code + 1).toChar() else c
}

val transformadores: List<(c: Char, pos: Int) -> Any> = listOf(
    sustituirCaracter,
    cambiarVocal,
    posicionCaracter,
    aumentarPosicion,
)