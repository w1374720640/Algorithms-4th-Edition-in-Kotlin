package chapter5.section2

import chapter5.section1.Alphabet
import edu.princeton.cs.algs4.StdRandom
import extensions.formatDouble
import extensions.formatInt

/**
 * 打字的猴子
 * 假设有一只会打字的猴子，它打出每个字母的概率为p，结束一个单词的概率为1-26p。
 * 编写一个程序，计算产生各种长度的单词的概率分布。
 * 其中如果"abc"出现了多次，只计算一次。
 *
 * 解：p的大小应该小于1/26，才会有概率结束单词
 * 根据给定概率生成索引可以可以参考StdRandom的discrete()方法或练习1.1.36
 */
fun ex22_TypingMonkeys(p: Double, N: Int): Array<Double> {
    require(p > 0.0 && p < 1.0 / 26)
    val probabilities = DoubleArray(27) {
        if (it == 26) {
            1.0 - 26 * p
        } else {
            p
        }
    }
    val st = TrieST<Int>(Alphabet.LOWERCASE)
    val stringBuilder = StringBuilder()
    repeat(N) {
        val index = StdRandom.discrete(probabilities)
        if (index == 26) {
            st.put(stringBuilder.toString(), 0)
            stringBuilder.clear()
        } else {
            stringBuilder.append(Alphabet.LOWERCASE.toChar(index))
        }
    }
    if (stringBuilder.isNotEmpty()) {
        st.put(stringBuilder.toString(), 0)
    }
    val iterable = st.keys()
    var maxLength = 0
    iterable.forEach {
        if (it.length > maxLength) {
            maxLength = it.length
        }
    }
    val array = Array(maxLength + 1) { 0.0 }
    var count = 0
    iterable.forEach {
        array[it.length]++
        count++
    }
    for (i in array.indices) {
        array[i] = array[i] / count
    }
    return array
}

fun main() {
    // 1/26 约等于 0.03846
    val p = 0.02
    val N = 100_0000
    val array = ex22_TypingMonkeys(p, N)
    var length = 0
    println(array.joinToString(separator = "\n") { "length=${formatInt(length++, 3, alignLeft = true)} ${formatDouble(it, 10)}" })
}
