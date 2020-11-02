package chapter3.section4

/**
 * 将键 E A S Y Q U T I O N 依次插入一张初始为空大小为M=4的基于线性探测法的散列表中，数组只要达到半满即自动将长度加倍
 * 使用散列函数 11 k % M 将第k个字母散列到某个数组索引上，给出得到的散列表的内容
 */
fun main() {
    val st = LinearProbingHashST<Char, Int>(4)
    "EASYQUTION".forEach {
        st.put(it, 0)
    }
    println(st.joinToString())
}