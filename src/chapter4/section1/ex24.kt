package chapter4.section1

import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack
import edu.princeton.cs.algs4.StdIn
import extensions.inputPrompt
import extensions.readLine
import java.util.*
import java.util.regex.Pattern

/**
 * 修改DegreesOfSeparation，从命令行接受一个整型参数y，忽略上映年数超过y的电影
 *
 * 解：重写[DegreesOfSeparation]类，通过传入筛选条件[filter]，忽略不符合条件的顶点
 */
class FilterDegreesOfSeparation(stream: String, sp: String, val name: String, val filter: (String) -> Boolean) {
    private val sg = SymbolGraph(stream, sp)
    private val graph = sg.G()

    init {
        require(sg.contains(name))
        require(filter(name)) { "The starting point should meet the filter criteria." }
    }

    private val marked = BooleanArray(graph.V)
    private val edgeTo = IntArray(graph.V)
    private val distTo = IntArray(graph.V) { -1 }

    init {
        val s = sg.index(name)
        val queue = Queue<Int>()
        marked[s] = true
        distTo[s] = 0
        queue.enqueue(s)
        while (!queue.isEmpty) {
            val v = queue.dequeue()
            graph.adj(v).forEach { w ->
                if (!marked[w] && filter(sg.name(w))) {
                    marked[w] = true
                    edgeTo[w] = v
                    distTo[w] = distTo[v] + 1
                    queue.enqueue(w)
                }
            }
        }
    }

    fun degrees(s: String): Int {
        // 不相连的人用最大的Int值表示（无穷大只能用浮点型数据表示）
        if (!sg.contains(s)) return Int.MAX_VALUE
        // 需要注意，路径长度除以2才是真正的间隔度数
        return if (marked[sg.index(s)]) distTo[sg.index(s)] / 2 else Int.MAX_VALUE
    }

    fun pathTo(s: String): Iterable<String>? {
        if (!sg.contains(s) || !marked[sg.index(s)]) return null
        val stack = Stack<String>()
        var w = sg.index(s)
        val index = sg.index(name)
        while (w != index) {
            stack.push(sg.name(w))
            w = edgeTo[w]
        }
        stack.push(name)
        return stack
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
    // 通过正则表达式匹配电影上映年份
    val pattern = Pattern.compile("\\((\\d+)\\)")
    // 获取当前的年份信息
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    // 忽略y年之前上映的电影
    val y = readLine("y: ").toInt()

    val degreesOfSeparation = FilterDegreesOfSeparation("./data/movies.txt", "/", "Bacon, Kevin") {
        var result = true
        val matcher = pattern.matcher(it)
        // 电影名称可以匹配正则，演员名称不会匹配正则
        if (matcher.find()) {
            val year = matcher.group(1).toInt()
            if (currentYear - year > y) {
                result = false
            }
        }
        result
    }

    while (StdIn.hasNextLine()) {
        val line = StdIn.readLine()
        degreesOfSeparation.printPath(line)
    }
}