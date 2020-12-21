package chapter4.section3

/**
 * 最小生成树的API
 */
interface MST {

    /**
     * 最小生成树的所有边
     */
    fun edges(): Iterable<Edge>

    /**
     * 最小生成树的权重
     */
    fun weight(): Double
}