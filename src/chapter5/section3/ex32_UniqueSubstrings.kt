package chapter5.section3

import chapter3.section5.LinearProbingHashSET
import edu.princeton.cs.algs4.Queue
import java.math.BigInteger
import java.util.*

/**
 * 不同的子字符串
 * 使用Rabin-Karp算法的思想完成练习5.2.14
 *
 * 解：5.2.14中使用了一个字符串符号表存储子字符串，这里使用存储子字符串hash值的符号表判断
 */
fun ex32_UniqueSubstrings(string: String, L: Int): Iterable<String> {
    require(L > 0 && L <= string.length)
    val Q = BigInteger.probablePrime(31, Random()).longValueExact()
    val R = 65536
    var RM = 1L
    repeat(L - 1) {
        RM = (RM * R) % Q
    }

    val set = LinearProbingHashSET<Long>()
    val queue = Queue<String>()
    var hash = 0L
    repeat(L) {
        hash = (hash * R + string[it].toLong()) % Q
    }
    set.add(hash)
    queue.enqueue(string.substring(0, L))

    for (i in 1 until string.length - L) {
        hash = (hash + Q - (RM * string[i - 1].toLong()) % Q) % Q
        hash = (hash * R + string[i + L - 1].toLong()) % Q
        if (!set.contains(hash)) {
            queue.enqueue(string.substring(i, i + L))
            set.add(hash)
        }
    }
    return queue
}

fun main() {
    val string = "cgcgggcgcg"
    val L = 3
    println(ex32_UniqueSubstrings(string, L).joinToString())
}
