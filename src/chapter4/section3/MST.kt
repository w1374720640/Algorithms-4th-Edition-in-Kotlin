package chapter4.section3

/**
 * 最小生成树的API
 */
abstract class MST(graph: EdgeWeightedGraph) {

    /**
     * 最小生成树的所有边
     */
    abstract fun edges(): Iterable<Edge>

    /**
     * 最小生成树的权重
     */
    abstract fun weight(): Double
}