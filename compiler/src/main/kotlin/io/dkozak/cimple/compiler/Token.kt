package io.dkozak.cimple.compiler

sealed class Token

class IntToken(val value: Int) : Token()
class DoubleToken(val value: Double) : Token()