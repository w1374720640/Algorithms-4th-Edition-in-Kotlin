package chapter1.exercise1_1

import edu.princeton.cs.algs4.In
import extensions.inputPrompt
import extensions.readInt

//从largeT.txt中读取数据，分别用暴力查找和二分查找来查找数据
//为了让效果更明显，可设置同一个key的查找次数
fun ex38(array: IntArray, key: Int, times: Int) {
    var hasFound = false
    var startTime = 0L
    var endTime = 0L
    //暴力查找
    startTime = System.currentTimeMillis()
    repeat(times) {
        for (i in array.indices) {
            if (array[i] == key) {
                hasFound = true
                break
            }
        }
    }
    endTime = System.currentTimeMillis()
    println("brute force search ${if (hasFound) "has found" else "not found"} ${key}, spend time ${endTime - startTime}ms")

    //排序
    startTime = System.currentTimeMillis()
    array.sort()
    endTime = System.currentTimeMillis()
    println("sort time=${endTime - startTime}ms")

    //二分查找
    startTime = System.currentTimeMillis()
    repeat(times) {
        hasFound = binarySearch(key, array) != -1
    }
    endTime = System.currentTimeMillis()
    println("binary search ${if (hasFound) "has found" else "not found"} ${key}, spend time ${endTime - startTime}ms")
}

fun main() {
    inputPrompt()
    val key = readInt()
    val times = readInt()
    val array = In("./data/largeT.txt").readAllInts()
    ex38(array, key, times)
}
