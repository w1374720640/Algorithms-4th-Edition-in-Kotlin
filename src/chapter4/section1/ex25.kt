package chapter4.section1

import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.StdIn
import extensions.inputPrompt

/**
 * 编写一个类似于DegreesOfSeparation的SymbolGraph用例，使用深度优先搜索代替广度优先搜索来查找两个演员之间的路径
 *
 * 解：深度优先搜索时，手动维护一个栈而不是用递归，防止栈溢出
 */
class DFSDegreesOfSeparation(stream: String, sp: String, val name: String) {
    private val sg = SymbolGraph(stream, sp)

    init {
        require(sg.contains(name))
    }

    private val paths = StackDepthFirstPaths(sg.G(), sg.index(name))

    fun degrees(s: String): Int {
        if (!sg.contains(s)) return Int.MAX_VALUE
        return if (paths.hasPathTo(sg.index(s))) paths.distTo(sg.index(s)) / 2 else Int.MAX_VALUE
    }

    fun pathTo(s: String): Iterable<String>? {
        if (!sg.contains(s)) return null
        val iterator = paths.pathTo(sg.index(s))?.iterator() ?: return null
        val queue = Queue<String>()
        while (iterator.hasNext()) {
            queue.enqueue(sg.name(iterator.next()))
        }
        return queue
    }

    fun printPath(s: String) {
        val degrees = degrees(s)
        println(if (degrees == Int.MAX_VALUE) "Not connected" else "degrees: $degrees")
        val iterator = pathTo(s)?.iterator() ?: return
        while (iterator.hasNext()) {
            println("    ${iterator.next()}")
        }
    }
}

fun main() {
    inputPrompt()
    val degreesOfSeparation = DFSDegreesOfSeparation("./data/movies.txt", "/", "Bacon, Kevin")
    while (StdIn.hasNextLine()) {
        val line = StdIn.readLine()
        degreesOfSeparation.printPath(line)
    }
}