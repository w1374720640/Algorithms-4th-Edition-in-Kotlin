package chapter1.exercise1_1

import extensions.inputPrompt
import extensions.readInt

//使用循环改进低效率的递归，消除重复的回溯
//低效版本
fun ex19a(N: Int): Long {
    require(N >= 0)
    if (N == 0) return 0
    if (N == 1) return 1
    return ex19a(N - 1) + ex19a(N - 2)
}

//高效版本
fun ex19b(N: Int): Long {
    require(N >= 0)
    if (N == 0) return 0
    if (N == 1) return 1
    val array = arrayOf<Long>(0, 1, 0)
    for (i in 2..N) {
        array[i % 3] = array[(i - 1) % 3] + array[(i - 2) % 3]
    }
    return array[N % 3]
}

fun main() {
    inputPrompt()
    val N = readInt()
    val startTime = System.currentTimeMillis()
    println("f(${N})=${ex19a(N)}")
    val endTime = System.currentTimeMillis()
    println("spend time: ${endTime - startTime} ms")
}