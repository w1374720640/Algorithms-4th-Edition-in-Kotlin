package chapter1.exercise1_4

/**
 * 求数组中三元素和为0的组合数量
 */
fun threeSum(array: IntArray): Long {
    val n = array.size
    var count = 0L
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            for (k in j + 1 until n) {
                if (array[i] + array[j] + array[k] == 0) {
                    count++
                }
            }
        }
    }
    return count
}

fun main() {
    timeRatio { threeSum(it) }
}