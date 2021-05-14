package io.dkozak.cimple

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode
import java.io.File
import kotlin.math.max
import kotlin.math.min

fun readInput(args: Array<String>): String {
    if (args.isEmpty()) {
        return buildString {
            var line = readLine()
            while (line != null) {
                append(line)
                line = readLine()
            }
        }
    }
    return File(args[0]).readText()
}

fun run(input: String) {
    val ast = parse(input).toCimpleAst()
    println(ast)
}

private fun CimpleParser.FileContext.toCimpleAst(): FileNode {
    val visitor = CimpleAstGenerationVisitor()
    val root = accept(visitor)
    return root as FileNode
}

class CimpleAstGenerationVisitor : CimpleBaseVisitor<Node>() {
    override fun visitFile(ctx: CimpleParser.FileContext): Node {
        val functions = ctx.func().map { it.accept(this) as FunctionNode }
        return FileNode(functions, ctx.extractLocation())
    }

    override fun visitFunc(ctx: CimpleParser.FuncContext): Node {
        val name = ctx.ID().text
        val args = ctx.params()?.ID()?.map { ParameterNode(it.text, it.toSourceLocation()) } ?: emptyList()
        val body = ctx.body().stat().map { it.accept(this) as StatementNode }
        return FunctionNode(name, args, body, ctx.extractLocation())
    }

    override fun visitCall(ctx: CimpleParser.CallContext): Node {
        val name = ctx.ID().text
        val arguments = ctx.args()?.expr()?.map { it.accept(this) as ExpressionNode } ?: emptyList()
        return CallNode(name, arguments, ctx.extractLocation())
    }

    override fun visitDefAttr(ctx: CimpleParser.DefAttrContext): Node {
        return visitDef(ctx.def())
    }

    override fun visitDef(ctx: CimpleParser.DefContext): Node {
        val name = ctx.ID().text
        val expr = ctx.expr().accept(this) as ExpressionNode
        return VariableDefinitionNode(name, expr, ctx.extractLocation())
    }

    override fun visitForAttr(ctx: CimpleParser.ForAttrContext): Node {
        val ctx = ctx.forStat()
        val init = ctx.init?.accept(this) as VariableDefinitionNode?
        val cond = ctx.cond?.accept(this) as ExpressionNode?
        val inc = ctx.inc?.accept(this) as VariableDefinitionNode?
        val body = ctx.body().stat().map { it.accept(this) as StatementNode }
        return ForStatement(init, cond, inc, body, ctx.extractLocation())
    }

    override fun visitIfAttr(ctx: CimpleParser.IfAttrContext): Node {
        val ctx = ctx.ifStat()
        val cond = ctx.cond.accept(this) as ExpressionNode
        val thenPart = ctx.thenPart.stat().map { it.accept(this) as StatementNode }
        val elsePart = ctx.elsePart?.stat()?.map { it.accept(this) as StatementNode } ?: emptyList()
        return IfStatementNode(cond, thenPart, elsePart, ctx.extractLocation())
    }

    override fun visitExprAttr(ctx: CimpleParser.ExprAttrContext): Node {
        val expr = ctx.expr().accept(this) as ExpressionNode
        return ExpressionStatementNode(expr, ctx.extractLocation())
    }

    override fun visitRetAttr(ctx: CimpleParser.RetAttrContext): Node {
        val expr = ctx.expr()?.accept(this) as ExpressionNode?
        return ReturnStatement(expr, ctx.extractLocation())
    }

    override fun visitExpr(ctx: CimpleParser.ExprContext): Node {
        val loc = ctx.extractLocation()
        return when {
            ctx.expr().size == 2 -> {
                val left = ctx.expr(0).accept(this) as ExpressionNode
                val right = ctx.expr(1).accept(this) as ExpressionNode
                BinaryOperationNode(left, right, ctx.op.text.toBinaryOp(), loc)
            }
            ctx.expr().size == 1 -> ctx.expr(0).accept(this) as ExpressionNode // paren
            ctx.NUM() != null -> IntLiteral(ctx.NUM().text.toInt(), loc)
            ctx.ID() != null -> VariableNode(ctx.ID().text, loc)
            ctx.call() != null -> ctx.call().accept(this)
            else -> unreachable(ctx.toString())
        }
    }
}

fun unreachable(msg: String = ""): Nothing = error("Should never get here. $msg")


private fun TerminalNode.toSourceLocation(): SourceLocation = SourceLocation(
    this.symbol.line..this.symbol.line,
    this.symbol.charPositionInLine..this.symbol.charPositionInLine + this.text.length
)


fun ParserRuleContext.extractLocation(): SourceLocation {
    val start = getStart()
    val topLine = start.line
    val stop = getStop()
    val bottomLine = stop.line
    val leftChar = min(start.charPositionInLine, stop.charPositionInLine)
    val rightChar = max(
        start.charPositionInLine + start.text.length,
        stop.charPositionInLine + stop.text.length
    )
    return SourceLocation(topLine..bottomLine, leftChar..rightChar)
}


fun main(args: Array<String>) {
    val prog = readInput(args)
    run(prog)
}

private fun parse(prog: String) = CimpleParser(CommonTokenStream(CimpleLexer(CharStreams.fromString(prog)))).file()


