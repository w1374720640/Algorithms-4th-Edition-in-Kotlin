package chapter2.section3

import chapter2.section1.cornerCases
import edu.princeton.cs.algs4.Stack

/**
 * 非递归的快速排序
 * 实现一个非递归的快速排序，使用一个循环来将弹出栈的子数组切分并将结果子数组重新压入栈
 * 注意：先将较大的子数组压入栈，这样就能保证栈最多只会有lgN个元素
 *
 * 解：递归本质上也是用栈实现的，所以所有的递归都可以用循环和栈来实现
 * 从栈中读取需要排序的范围，分解成两部分放入栈中，继续循环，范围小于等于1时直接从栈中弹出，不操作
 */
fun <T : Comparable<T>> ex20(array: Array<T>) {
    val stack = Stack<IntRange>()
    stack.push(array.indices)
    while (!stack.isEmpty) {
        val range = stack.pop()
        if (range.first < range.last) {
            val mid = partition(array, range.first, range.last)
            stack.push(range.first until mid)
            stack.push(mid + 1..range.last)
        }
    }
}

fun main() {
    cornerCases(::ex20)
}