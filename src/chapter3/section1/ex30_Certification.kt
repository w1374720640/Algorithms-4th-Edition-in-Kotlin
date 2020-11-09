package chapter3.section1

/**
 * 向BinarySearchST中加入断言（assert）语句，在每次插入和删除数据后检查算法的有效性和数据结构的完整性
 * 例如，对于每个索引必有i==rank(select(i))且数组应该总是有序的
 */
class CertificationBinarySearchST<K: Comparable<K>, V: Any> : BinarySearchST<K, V>() {

    override fun put(key: K, value: V) {
        super.put(key, value)
        certification()
    }

    override fun delete(key: K) {
        super.delete(key)
        certification()
    }

    /**
     * 使用check()函数替代assert()函数，assert()函数需要添加额外编译参数才能生效
     */
    fun certification() {
        if (isEmpty()) return
        check(rank(select(0)) == 0)
        for (i in 1 until size - 1) {
            @Suppress("UNCHECKED_CAST")
            check(keys[i - 1]!! < keys[i]!! as K)
            check(rank(select(i)) == i)
        }
    }
}

fun main() {
    testOrderedST(CertificationBinarySearchST())
}