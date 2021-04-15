package chapter5.section3

/**
 * 为算法5.7实现的Boyer-Moore算法构造一个最坏情况下的输入（说明它的运行时间不是线性级别的）。
 *
 * 解：待匹配字符串所有字符完全相同，匹配模板只有第一位不同时，
 * 每次只前进一位，和暴力子字符串查找算法效率相同O(MN)
 */
fun main() {
    val pat = "BAAAAA"
    val txt = "AAAAAAAAAAAAAAAAAA"
    val boyerMoore = BoyerMoore(pat)
    println(boyerMoore.search(txt))
}