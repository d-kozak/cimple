package io.dkozak.cimple

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream

fun parse(prog: String) = CimpleParser(CommonTokenStream(CimpleLexer(CharStreams.fromString(prog)))).file()