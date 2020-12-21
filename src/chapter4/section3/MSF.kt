package chapter4.section3

/**
 * 最小生成森林的API
 */
interface MSF {

    /**
     * 最小生成树的数量
     */
    fun count(): Int

    /**
     * 找到某个顶点所在的最小生成树
     */
    fun tree(v: Int): MST

    /**
     * 所有的最小生成树
     */
    fun trees(): Iterable<MST>
}