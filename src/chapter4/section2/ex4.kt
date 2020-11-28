package chapter4.section2

/**
 * 为Digraph添加一个方法hasEdge()，它接受两个整型参数v和w。
 * 如果图含有边v->w，方法返回true，否则返回false。
 */
fun Digraph.hasEdge(v: Int, w: Int): Boolean {
    adj(v).forEach {
        if (it == w) return true
    }
    return false
}

fun main() {
    val digraph = getTinyDG()
    println(digraph.hasEdge(0, 1))
    println(digraph.hasEdge(1, 0))
}