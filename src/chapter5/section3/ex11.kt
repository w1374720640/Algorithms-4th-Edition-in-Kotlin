package chapter5.section3

/**
 * 为算法5.7实现的Boyer-Moore算法构造一个最坏情况下的输入（说明它的运行时间不是线性级别的）。
 */
fun main() {
    val pat = "BAAAAA"
    val txt = "AAAAAAAAAAAAAAAAAA"
    val boyerMoore = BoyerMoore(pat)
    println(boyerMoore.search(txt))
}