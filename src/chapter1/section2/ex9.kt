package chapter1.section2

import chapter1.section1.lg
import edu.princeton.cs.algs4.Counter
import edu.princeton.cs.algs4.In
import extensions.inputPrompt
import extensions.readInt

/**
 * 修改BinarySearch（请见1.1.10.1节中的二分查找代码），使用Counter统计在有查找中被检查的键的总数并在查找全部结束后打印该值。
 * 提示：在main()中创建一个Counter对象并将它作为参数传递给rank()。
 */
fun ex9(key: Int, array: IntArray, counter: Counter): Int {
    var low = 0
    var high = array.size - 1
    while (low <= high) {
        counter.increment()
        val mid = (low + high) / 2
        when {
            key < array[mid] -> high = mid - 1
            key > array[mid] -> low = mid + 1
            else -> return mid
        }
    }
    return -1
}

fun main() {
    inputPrompt()
    val key = readInt("key: ")
    val array = In("./data/largeT.txt").readAllInts()
    val counter = Counter("times")
    ex9(key, array, counter)
    println(counter.toString())
    //二分查找理论上的平均查找次数为logN（底为2）
    println("log${array.size}=${lg(array.size)}")
}