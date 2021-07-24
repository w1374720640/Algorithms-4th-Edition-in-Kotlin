package chapter2.section1

import chapter2.SwapCallback
import chapter2.swapCallbackList
import extensions.inputPrompt
import extensions.random
import extensions.readInt

/**
 * 在选择排序中，一个元素最多可能会被交换多少次？平均可能会被交换多少次？
 *
 * 解：当最大值在第一位，其余值按升序排列时，会被交换N-1次
 */
fun ex2a(size: Int): Int {
    val array = Array(size) { if (it == 0) size.toDouble() else it.toDouble() }
    var count = 0
    val callback = object : SwapCallback {
        override fun before(tag: Any, i: Int, j: Int) {
            if (tag !== array) return
            if (array[i] == size.toDouble() || array[j] == size.toDouble()) count++
        }

        override fun after(tag: Any, i: Int, j: Int) {
        }
    }
    swapCallbackList.add(callback)
    selectionSort(array)
    swapCallbackList.remove(callback)
    return count
}

/**
 * 选择排序中，一个元素平均被交换多少次
 * 平均为2次，因为平均进行了N次交换，每次交换两个值，平均每个值参与了两次交换
 */
fun ex2b(size: Int): Double {
    val map = mutableMapOf<Double, Int>()
    val array = Array(size) { random() }
    val callback = object : SwapCallback {
        override fun before(tag: Any, i: Int, j: Int) {
            if (tag !== array) return
            map[array[i]] = map.getOrDefault(array[i], 0) + 1
            map[array[j]] = map.getOrDefault(array[j], 0) + 1
        }

        override fun after(tag: Any, i: Int, j: Int) {
        }
    }
    swapCallbackList.add(callback)
    selectionSort(array)
    swapCallbackList.remove(callback)
    var total = 0.0
    map.forEach {
        total += it.value
    }
    return total / size
}

fun main() {
    inputPrompt()
    val size = readInt("size: ")
    println("Max swap times is ${ex2a(size)}")
    println("Average swap times is ${ex2b(size)}")
}