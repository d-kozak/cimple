package io.dkozak.cimple.util

data class NameGenerator(
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

    inline fun <T> reserveTemporarily(names: List<String>, block: () -> T): T {
        val takenAlready = usedVarNames.intersect(names)
        check(takenAlready.isEmpty()) { "Names  $takenAlready are already taken" }
        usedVarNames.addAll(names)
        try {
            return block()
        } finally {
            usedVarNames.removeAll(names)
        }
    }

    fun reserve(name: String) {
        check(usedVarNames.add(name)) { "Name $name already allocated" }
    }
}

fun main() {
    val holder = NameGenerator()
    repeat(2000) {
        println(holder.nextVariable())
    }
}