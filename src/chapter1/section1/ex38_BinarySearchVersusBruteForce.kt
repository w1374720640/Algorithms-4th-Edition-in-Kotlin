package chapter1.section1

import edu.princeton.cs.algs4.In
import extensions.inputPrompt
import extensions.readInt
import extensions.spendTimeMillis

/**
 * 二分查找与暴力查找
 * 根据1.1.10.4节给出的暴力查找法编写一个程序BruteForceSearch，
 * 在你的计算机上比较它和BinarySearch处理largeW.txt和largeT.txt所需的时间
 */
fun ex38_BinarySearchVersusBruteForce(array: IntArray, key: Int, times: Int) {
    var hasFound = false
    val bruteForceTime = spendTimeMillis {
        repeat(times) {
            for (i in array.indices) {
                if (array[i] == key) {
                    hasFound = true
                    break
                }
            }
        }
    }
    println("brute force search ${if (hasFound) "has found" else "not found"} ${key}, spend time $bruteForceTime ms")

    val sortTime = spendTimeMillis {
        array.sort()
    }
    println("sort time=$sortTime ms")

    val binarySearchTime = spendTimeMillis {
        repeat(times) {
            hasFound = binarySearch(key, array) != -1
        }
    }
    println("binary search ${if (hasFound) "has found" else "not found"} ${key}, spend time $binarySearchTime ms")
}

fun main() {
    inputPrompt()
    val key = readInt("key: ")
    val times = readInt("times: ") // 为了让效果更明显，重复一定次数
    val array = In("./data/largeT.txt").readAllInts()
    ex38_BinarySearchVersusBruteForce(array, key, times)
}
