package io.dkozak.cimple.cfg

import io.dkozak.cimple.FunctionNode

fun FunctionNode.lower(): Function {
    val holder = InstructionHolder()
    return holder.lower(this)
}

private class InstructionHolder {
    val instructions = mutableListOf<Instruction>()
    val varGenerator = VarNameGenerator()

    fun lower(functionNode: FunctionNode): Function {
        return Function(functionNode.name, emptyList(), emptyList())
    }
}

private data class VarNameGenerator(
    val usedVarNames: MutableSet<String> = mutableSetOf(),
    val buff: MutableList<Char> = mutableListOf()
) {
    fun nextVariable(): String {
        var carry = true
        var i = 0
        while (carry && i < buff.size) {
            buff[i] = buff[i] + 1
            carry = buff[i] == 'z' + 1
            if (carry) buff[i] = 'a'
            i++
        }
        if (carry) buff.add('a')
        val name = buff.joinToString("") { it.toString() }.reversed()
        return if (usedVarNames.add(name)) name else nextVariable()

    }
}

fun main() {
    val holder = VarNameGenerator()
    repeat(2000) {
        println(holder.nextVariable())
    }
}