package io.dkozak.cimple.compiler

fun lexer(input: String): Token {

    var currentIndex = 0
    while (currentIndex < input.length && input[currentIndex].isDigit()) {
        currentIndex++
    }
    if (input[currentIndex] == '.') {
        currentIndex++
        while (currentIndex < input.length && input[currentIndex].isDigit()) {
            currentIndex++
        }
        return DoubleToken(input.substring(0, currentIndex).toDouble())
    }

    return IntToken(input.substring(0, currentIndex).toInt())
}