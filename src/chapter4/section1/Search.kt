package chapter4.section1

/**
 * 统计图中所有与点s连通的点
 */
abstract class Search(graph: Graph, s: Int) {
    /**
     * 判断点v是否与s连通
     */
    abstract fun marked(v: Int): Boolean

    /**
     * 与s点联通的顶点总数
     */
    abstract fun count(): Int
}