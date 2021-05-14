package io.dkozak.cimple

import org.junit.jupiter.api.Test

class ParserTests {

    @Test
    fun basic() {
        run("fun main() { return 52;}")
    }
}