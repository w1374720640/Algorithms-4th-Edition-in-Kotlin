package chapter4.section1

/**
 * 在图中找出所有起点为s的路径
 */
abstract class Paths(graph: Graph, s: Int) {
    /**
     * 是否存在从s到v的路径
     */
    abstract fun hasPathTo(v: Int): Boolean

    /**
     * s到v的路径，如果不存在则返回null
     */
    abstract fun pathTo(v: Int): Iterable<Int>?
}