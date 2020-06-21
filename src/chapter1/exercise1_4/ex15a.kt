package chapter1.exercise1_4

/**
 * 用二分查找找到一个数左右两侧的值
 */
fun findLeftAndRight(value: Int, array: IntArray): Pair<Int, Int> {
    require(array.isNotEmpty())
    var start = 0
    var end = array.size - 1
    while (start <= end) {
        val mid = (start + end) / 2
        when {
            array[mid] >= value -> end = mid - 1
            array[mid] < value -> start = mid + 1
        }
    }
    return end to start
}

/**
 * 计算数组中和为0的整数对数量，要求使用线性级别的算法，而不是基于二分查找的线性对数级算法
 */
fun ex15a(array: IntArray): Long {
    array.sort()
    val pair = findLeftAndRight(0, array)
    var left = pair.first
    var right = pair.second
    var count = 0L
    while (left >= 0 && right < array.size) {
        when {
            array[left] + array[right] == 0 -> {
                count++
                //处理有重复数据的情况
                if (left > 0 && array[left] == array[left - 1]) {
                    left--
                } else {
                    right++
                }
            }
            array[left] + array[right] > 0 -> {
                left--
            }
            array[left] + array[right] < 0 -> {
                right++
            }
        }
    }
    return count
}

fun main() {
    timeRatio { ex15a(it) }
}