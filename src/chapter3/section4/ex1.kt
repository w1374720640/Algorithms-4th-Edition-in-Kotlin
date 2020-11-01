package chapter3.section4

/**
 * 将键 E A S Y Q U E S T I O N 依次插入一张初始为空且含有M=5条链表的基于拉链法的散列表中
 * 使用散列函数 11 k % M 将第k个字母散列到某个数组索引上
 *
 * 解：“11 k % M” 应该改为 “(11 * k) % M” ，不然没有意义
 */
fun main() {
    val st = object : SeparateChainingHashST<Char, Int>(5) {
        override fun hash(key: Char): Int {
            return (11 * n) % m
        }
    }
    "EASYQUESTION".forEach {
        st.put(it, 0)
    }
    println(st.joinToString())
}