package io.dkozak.cimple.compiler

class Parser(private val buffer: Buffer) {

    fun parse(): AstNode = IntLiteral(42)
}