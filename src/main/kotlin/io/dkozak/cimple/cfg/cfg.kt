package io.dkozak.cimple.cfg

import io.dkozak.cimple.BinaryOperation

enum class Type {
    INT
}

data class Function(val name: String, val params: List<String>, val instructions: List<Instruction>)

sealed class Instruction
data class Def(val dst: String, val type: Type, val op: Operation) : Instruction()
data class Return(val op: Operation?) : Instruction()
data class Label(val name: String) : Instruction()
data class If(val condition: String, val thenLabel: String, val elseLabel: String)

sealed class Operation
data class Binary(val left: String, val right: String, val op: BinaryOperation) : Operation()
data class Id(val source: String) : Operation()
data class Call(val name: String, val args: List<String>) : Operation()

