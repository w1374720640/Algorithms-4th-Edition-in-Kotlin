package chapter1.section1

import extensions.inputPrompt
import extensions.readInt

//接收整形参数N，返回不大于log2 N 的最大整数
fun ex14(N: Int): Int {
    require(N > 0)
    var shrCount = 0
    //kotlin参数默认不能重新赋值，中转一下
    var result = N
    do {
        //右移一位
        result = result shr 1
        shrCount++
    } while (result != 0)
    return shrCount - 1
}

fun main() {
    inputPrompt()
    println(ex14(readInt()))
}