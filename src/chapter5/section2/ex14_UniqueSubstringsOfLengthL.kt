package chapter5.section2

/**
 * 长度为L的不同子字符串
 * 编写一个TST的用例，从标准输入读取文本并计算其中长度为L的不同子字符串的数量。
 * 例如，如果输入为cgcgggcgcg，那么长度为3的不同子字符串就有5个：cgc、cgg、gcg、ggc和ggg。
 * 提示：使用字符串方法substring(i,i+L)来提取第i个子字符串并将它插入到一张符号表中。
 */
fun ex14_UniqueSubstringsOfLengthL(string: String, L: Int): Iterable<String> {
    require(L > 0 && L <= string.length)
    val tst = TST<Int>()
    for (i in 0..string.length - L) {
        val key = string.substring(i, i + L)
        tst.put(key, 0)
    }
    return tst.keys()
}

fun main() {
    val string = "cgcgggcgcg"
    val L = 3
    println(ex14_UniqueSubstringsOfLengthL(string, L).joinToString())
}
