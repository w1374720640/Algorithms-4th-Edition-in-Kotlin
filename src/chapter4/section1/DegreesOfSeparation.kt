package chapter4.section1

import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.StdIn
import extensions.inputPrompt

/**
 * 间隔的度数
 */
class DegreesOfSeparation(stream: String, sp: String, name: String) {
    private val sg = SymbolGraph(stream, sp)

    init {
        require(sg.contains(name))
    }

    private val paths = BreadthFirstPaths(sg.G(), sg.index(name))

    fun degrees(s: String): Int {
        // 不相连的人用最大的Int值表示（无穷大只能用浮点型数据表示）
        if (!sg.contains(s)) return Int.MAX_VALUE
        // 需要注意，路径长度除以2才是真正的间隔度数
        return paths.pathTo(sg.index(s))?.count()?.div(2) ?: Int.MAX_VALUE
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
    val degreesOfSeparation = DegreesOfSeparation("./data/movies.txt", "/", "Bacon, Kevin")
    while (StdIn.hasNextLine()) {
        val line = StdIn.readLine()
        degreesOfSeparation.printPath(line)
    }
}