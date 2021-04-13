package chapter5.section3

import edu.princeton.cs.algs4.In
import extensions.spendTimeMillis

/**
 * 运行时间
 * 编写一段程序，用本节学习的4种算法在《双城记》（tale.txt）种查找以下字符串并记录时间：
 * it is a far far better thing that i do than i have ever done
 * 讨论你的结果在何种程度上验证了正文对这几种算法的性能猜想。
 */
fun ex39_Timings(create: (String) -> StringSearch): Long {
    val pat = "it is a far far better thing that i do than i have ever done"
    val input = In("./data/tale.txt")
    val txt = input.readAll()
    return spendTimeMillis {
        val index = create(pat).search(txt)
        // 所有算法应该都能找到
        check(index != txt.length)
    }
}

fun main() {
    println("Brute:      ${ex39_Timings { BruteForceSearch(it) }} ms")
    println("KMP:        ${ex39_Timings { KMP(it) }} ms")
    println("BoyerMoore: ${ex39_Timings { BoyerMoore(it) }} ms")
    println("RabinKarp:  ${ex39_Timings { RabinKarp(it) }} ms")
}