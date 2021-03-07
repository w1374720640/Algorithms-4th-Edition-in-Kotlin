package chapter5.section2

import chapter3.section5.LinearProbingHashSET
import chapter5.section1.Alphabet
import extensions.formatInt
import extensions.random
import extensions.spendTimeMillis

/**
 * 重复元素（再续）
 * 使用StringSET（请见练习5.2.6）代替HashSET重新完成练习3.5.30，比较两种方法的运行时间。
 * 然后使用dedup为N=10⁷、10⁸和10⁹运行实验，用随机long型字符串重复实验并讨论结果。
 *
 * 解：随机的long型字符串会导致极少的重复字符串，这里使用Int型字符串
 */
fun ex23_DuplicatesRevisitedAgain(M: Int, N: Int, T: Int, create: (Int) -> String): Int {
    var count = 0
    repeat(T) {
        // 基于小字符集的TrieST实现会比HashSET更快
        val set = TrieStringSET(Alphabet.DECIMAL)
        // 基于三向单词查找树TST的实现会比HashSET略慢
//        val set = ThreeWayStringSET()
        repeat(N) {
            set.add(create(M))
        }
        count += set.size()
    }
    return count / T
}

/**
 * 将练习3.5.30的答案复制过来，并修改使其可以支持字符串
 */
fun <E : Any> duplicatesRevisitedHashSET(M: Int, N: Int, T: Int, create: (Int) -> E): Int {
    var count = 0
    repeat(T) {
        val set = LinearProbingHashSET<E>()
        repeat(N) {
            set.add(create(M))
        }
        count += set.size()
    }
    return count / T
}

fun main() {
    val T = 10
    val N = 100_0000
    var M = N / 2
    repeat(3) {
        var count1 = 0
        var count2 = 0
        val time1 = spendTimeMillis {
            count1 = duplicatesRevisitedHashSET(M, N, T) {
                random(it).toString()
            }
        }
        val time2 = spendTimeMillis {
            count2 = ex23_DuplicatesRevisitedAgain(M, N, T) {
                random(it).toString()
            }
        }
        println("N:${formatInt(N, 7)} M:${formatInt(M, 7)}")
        println("count1=$count1 time1=$time1 ms")
        println("count2=$count2 time2=$time2 ms")
        println()
        M *= 2
    }
}