package io.dkozak.cimple.compiler


class Lexer(private val input: String) {
    private var currentIndex = 0


    fun getNextToken(): Token? {
        while (currentIndex < input.length && input[currentIndex].isDigit()) {
            currentIndex++
        }
        if (currentIndex < input.length && input[currentIndex] == '.') {
            currentIndex++
            if (currentIndex == input.length || (currentIndex < input.length && !input[currentIndex].isDigit())) {
                currentIndex--
                return IntToken(input.substring(0, currentIndex).toInt())
            }
            while (currentIndex < input.length && input[currentIndex].isDigit()) {
                currentIndex++
            }
            return DoubleToken(input.substring(0, currentIndex).toDouble())
        }

        return IntToken(input.substring(0, currentIndex).toInt())
    }
}