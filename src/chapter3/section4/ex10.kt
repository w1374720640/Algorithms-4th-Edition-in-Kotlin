package chapter3.section4

/**
 * 将键 E A S Y Q U T I O N 依次插入一张初始为空且大小为M=16的基于线性探测法的散列表中
 * 使用散列函数 11 k % M 将第K个字母散列到某个数组索引上
 * 对于M=10将本题重新完成一遍
 *
 * 解：散列函数为 (11 * key.hashCode()) % M
 * 散列表不自动扩容，如果M小于数据源长度，会出现死循环
 */
fun ex10(m: Int) {
    val st = object : LinearProbingHashST<Char, Int>(m) {
        override fun resize(size: Int) {
            //不自动扩容
        }
    }
    "EASYQUTION".forEach {
        st.put(it, 0)
    }
    println(st.joinToString())
}

fun main() {
    ex10(16)
    ex10(10)
}