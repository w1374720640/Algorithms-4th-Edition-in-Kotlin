package chapter3.section5

/**
 * 数学集合
 */
interface MathSET<K : Any>: SET<K> {
    /**
     * 求当前集合相对于全集set的补集
     *
     * 相对补集：若A和B是集合，则A在B中的相对补集是这样一个集合：其元素属于B但不属于A
     * 绝对补集：若给定全集U，A是U的子集，则A在U中的相对补集称为A的绝对补集（或简称补集）
     * 和原文中的接口不同，原文中没有参数
     * 这里无法保证当前集合是参数集合的子集，所以这里使用相对补集的概念
     */
    fun complement(set: MathSET<K>): MathSET<K>

    /**
     * 求并集
     */
    fun union(set: MathSET<K>): MathSET<K>

    /**
     * 求交集
     */
    fun intersection(set: MathSET<K>): MathSET<K>
}