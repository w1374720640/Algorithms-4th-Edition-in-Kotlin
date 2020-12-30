package chapter4.section3

/**
 * 加权无向图的API
 */
abstract class EWG(val V: Int) {
    var E: Int = 0
        protected set

    /**
     * 添加一条边
     */
    abstract fun addEdge(edge: Edge)

    /**
     * 获取一个顶点连接的所有边
     */
    abstract fun adj(v: Int): Iterable<Edge>

    /**
     * 获取图的所有边
     */
    abstract fun edges(): Iterable<Edge>
}