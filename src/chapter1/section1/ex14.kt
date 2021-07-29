package chapter1.section1

import extensions.inputPrompt
import extensions.readInt

/**
 * 编写一个静态方法lg()，接收整形参数N，返回不大于log2 N 的最大整数。不要使用Math库
 */
fun lg(N: Int): Int {
    require(N > 0)
    var shrCount = 1
    while (N shr shrCount != 0) {
        shrCount++
    }
    return shrCount - 1
}

fun main() {
    inputPrompt()
    println(lg(readInt()))
}