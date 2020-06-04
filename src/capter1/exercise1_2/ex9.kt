package capter1.exercise1_2

import capter1.exercise1_1.ex14
import edu.princeton.cs.algs4.Counter
import edu.princeton.cs.algs4.In
import extensions.inputPrompt
import extensions.readInt

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
    val key = readInt()
    //largeT.txt文件中，126786 这个值只需要查询一次
    val array = In("./data/largeT.txt").readAllInts()
    val counter = Counter("times")
    ex9(key, array, counter)
    println(counter.toString())
    //二分查找理论上的平均查找次数为logN（底为2）
    println("log${array.size}=${ex14(array.size)}")
}