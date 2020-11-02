package chapter3.section4

/**
 * 使用3.4.3.1节的delete()方法从标准索引测试用例使用的LinearProbingHashST中删除键C并给出结果散列表的内容
 *
 * 解：标准索引测试按以下顺序依次插入： S E A R C H E X A M P L E
 */
fun main() {
    val st = LinearProbingHashST<Char, Int>()
    "SEARCHEXAMPLE".toCharArray().forEach {
        st.put(it, 0)
    }
    println("before delete: ${st.joinToString()}")
    st.delete('C')
    println(" after delete: ${st.joinToString()}")
}