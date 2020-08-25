package chapter2.section3

import chapter2.ArrayInitialState
import chapter2.section1.cornerCases
import chapter2.sortMethodsCompare
import edu.princeton.cs.algs4.Stack
import edu.princeton.cs.algs4.StdRandom

/**
 * 非递归的快速排序
 * 实现一个非递归的快速排序，使用一个循环来将弹出栈的子数组切分并将结果子数组重新压入栈
 * 注意：先将较大的子数组压入栈，这样就能保证栈最多只会有lgN个元素
 *
 * 解：递归本质上也是用栈实现的，所以所有的递归都可以用循环和栈来实现
 * 从栈中读取需要排序的范围，分解成两部分放入栈中，继续循环，范围小于等于1时直接从栈中弹出，不操作
 */
fun <T : Comparable<T>> ex20_NonrecursiveQuicksort(array: Array<T>) {
    if (array.size < 2) return
    StdRandom.shuffle(array)
    val stack = Stack<IntRange>()
    stack.push(array.indices)
    while (!stack.isEmpty) {
        val range = stack.pop()
        val mid = partition(array, range.first, range.last)
        val leftRange = range.first until mid
        val rightRange = mid + 1..range.last
        //让较大的范围先入栈，使栈中的元素数量最少
        if (leftRange.size() > rightRange.size()) {
            if (leftRange.size() > 1) {
                stack.push(leftRange)
            }
            if (rightRange.size() > 1) {
                stack.push(rightRange)
            }
        } else {
            if (rightRange.size() > 1) {
                stack.push(rightRange)
            }
            if (leftRange.size() > 1) {
                stack.push(leftRange)
            }
        }
    }
}

fun IntRange.size(): Int {
    if (first > last) return 0
    return last - first + 1
}

fun main() {
    cornerCases(::ex20_NonrecursiveQuicksort)
    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "quickSort" to ::quickSort,
            "ex20" to ::ex20_NonrecursiveQuicksort
    )
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}