package io.dkozak.cimple.compiler

sealed class Token

class IntToken(val value: Int) : Token()
class DoubleToken(val value: Double) : Token()
object Plus : Token()
object Minus : Token()
object Multiply : Token()
object Divide : Token()
object ParenOpen : Token()
object ParenClose : Token()
object Newline : Token()

class UnknownCharacters(val value: String) : Token()