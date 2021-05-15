package io.dkozak.cimple.cfg

import io.dkozak.cimple.BinaryOperation

enum class Type {
    INT
}

data class Function(val name: String, val params: List<String>, val instructions: List<Instruction>)

sealed class Instruction
data class Def(val dst: String, val type: Type, val op: Operation) : Instruction()
data class Return(val src: String?) : Instruction()
data class Label(val name: String) : Instruction()
data class If(val condition: String, var thenLabel: String, var elseLabel: String) : Instruction()
data class Jump(val label: String) : Instruction()
data class BranchFalse(val condition: String, val label: String) : Instruction()


sealed class Operation
data class Binary(val left: String, val right: String, val op: BinaryOperation) : Operation()
data class Id(val source: String) : Operation()
data class Call(val name: String, val args: List<String>) : Operation()
data class Const(val value: Int) : Operation()

data class ControlFlowGraph(
    val entry: BasicBlock,
    val blockMap: MutableMap<String, BasicBlock> = mutableMapOf()
)

const val ENTRY_BLOCK_NAME = "entry"
const val EXIT_BLOCK_NAME = "exit"

data class BasicBlock(
    val name: String,
    val instructions: MutableList<Instruction> = mutableListOf(),
    val successors: MutableList<BasicBlock> = mutableListOf()
)