package io.dkozak.cimple

import org.junit.jupiter.api.Test
import java.io.File

class ParserTests {

    @Test
    fun basic() {
        run("fun main() { return 52;}")
    }

    @Test
    fun basic2() {
        run("""fun main(){1+1;}""")
    }

    @Test
    fun batch() {
        val exampleDir = File("./src/test/resources")
        for (testFile in exampleDir.listFiles() ?: emptyArray()) {
            val prog = testFile.readText()
            println("Testing prog: $testFile")
            println(prog)
            println("---")
            run(prog)
            println("===")
        }
    }
}