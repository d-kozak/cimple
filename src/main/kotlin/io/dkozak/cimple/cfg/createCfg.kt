package io.dkozak.cimple.cfg

import io.dkozak.cimple.util.NameGenerator

fun Function.toCfg(): ControlFlowGraph {
    val nameGen = NameGenerator()
    nameGen.reserve(ENTRY_BLOCK_NAME)
    nameGen.reserve(EXIT_BLOCK_NAME)
    val instructionToBlock = mutableMapOf<Instruction, BasicBlock>()
    val labelToBlock = mutableMapOf<String, BasicBlock>()
    val blockMap = mutableMapOf<String, BasicBlock>()
    val entry = BasicBlock(ENTRY_BLOCK_NAME)
    blockMap[ENTRY_BLOCK_NAME] = entry
    labelToBlock[ENTRY_BLOCK_NAME] = entry

    val exitBlock = BasicBlock(EXIT_BLOCK_NAME)
    blockMap[EXIT_BLOCK_NAME] = exitBlock
    labelToBlock[EXIT_BLOCK_NAME] = exitBlock

    var current = entry
    for (instruction in instructions) {
        // todo generate differently using terminators?
        // or rather generate a proper lable ifFalse during lowering..
        if (instruction is Label) {
            current = BasicBlock(instruction.name)
            blockMap[instruction.name] = current
            labelToBlock[instruction.name] = current
            instructionToBlock[instruction] = current
        } else {
            current.instructions.add(instruction)
            instructionToBlock[instruction] = current
        }

        if (instruction is Return) {
            val afterReturnName = ".after_ret_" + nameGen.nextVariable()
            current = BasicBlock(afterReturnName)
        } else if (instruction is BranchFalse) {
            val afterBranchFalse = ".brach_false_next_" + nameGen.nextVariable()
            current = BasicBlock(afterBranchFalse)
            blockMap[afterBranchFalse] = current
        }
    }

    for ((i, instruction) in instructions.withIndex()) {
        if (!(instruction is Jump || instruction is If || instruction is BranchFalse || instruction is Return || instruction is Label)) continue
        val currentBlock = instructionToBlock.getValue(instruction)
        if (instruction is Label && i > 0 && !(instructions[i - 1] is Jump || instructions[i - 1] is If)) {
            val prevBlock = instructionToBlock.getValue(instructions[i - 1])
            prevBlock.successors.add(currentBlock)
            continue
        }
        when (instruction) {
            is Jump -> {
                val nextBlock = labelToBlock.getValue(instruction.label)
                currentBlock.successors.add(nextBlock)
            }
            is If -> {
                val thenBlock = labelToBlock.getValue(instruction.thenLabel)
                val elseBlock = labelToBlock.getValue(instruction.elseLabel)
                currentBlock.successors.add(thenBlock)
                currentBlock.successors.add(elseBlock)
            }

            is BranchFalse -> {
                val jumpBlock = labelToBlock.getValue(instruction.label)
                currentBlock.successors.add(jumpBlock)
                if (i < instructions.size - 1) {
                    val nextBlock = instructionToBlock.getValue(instructions[i + 1])
                    currentBlock.successors.add(nextBlock)
                }
            }

            is Return -> {
                currentBlock.successors.add(exitBlock)
            }
        }
    }

    for (block in blockMap.values) {
        if (block.successors.isEmpty() && block.name != EXIT_BLOCK_NAME)
            block.successors.add(exitBlock)
    }

    return ControlFlowGraph(entry, blockMap)
}