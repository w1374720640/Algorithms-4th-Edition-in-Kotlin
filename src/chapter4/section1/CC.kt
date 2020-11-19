package chapter4.section1

/**
 * 找出一幅图中的所有连通分量
 */
abstract class CC(graph: Graph) {

    /**
     * v和w是否相连
     */
    abstract fun connected(v: Int, w: Int): Boolean

    /**
     * 连通分量的数量
     */
    abstract fun count(): Int

    /**
     * v所在连通分量的标识符(0 ~ count() - 1)
     */
    abstract fun id(v: Int): Int
}