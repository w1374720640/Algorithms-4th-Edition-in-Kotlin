package chapter1.exercise1_4

//暴力计算数组中相等的整数对数量
fun ex8a(array: IntArray): Long {
    var count = 0L
    for (i in array.indices) {
        for (j in i + 1 until array.size) {
            if (array[i] == array[j]) {
                count++
            }
        }
    }
    return count
}

/**
 * 优化后的方法
 * 先排序，再统计每个数字的重复次数，再用组合公式计算组合数
 * array.sort()排序算法复杂度为NlgN，遍历所有元素复杂度为N，用组合公式计算组合数复杂度为常数，所以总复杂度为NlgN
 */
fun ex8b(array: IntArray): Long {
    array.sort()
    var count = 0L
    var index = 0
    while (index < array.size) {
        val value = array[index]
        var equalNum = 1
        while (index + equalNum < array.size && array[index + equalNum] == value) {
            equalNum++
        }
        if (equalNum > 1) {
            count += combination(equalNum, 2)
        }
        index += equalNum
    }
    return count
}

fun main() {
    println("fast method:")
    timeRatio(path = "./data/largeT.txt") { ex8b(it) }
    println("slow method:")
    timeRatio(path = "./data/largeT.txt") { ex8a(it) }
}
