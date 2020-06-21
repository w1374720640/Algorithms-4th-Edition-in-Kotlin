package chapter1.exercise1_4

/**
 * 求三个数相加和为0的组合数量，要求使用平方级算法，参考ex15a
 * FIXME 不知道哪里有错误，结果约为实际值的一半
 */
fun ex15b(array: IntArray): Long {
    array.sort()
    var count = 0L
    for (i in array.indices) {
        if (array[i] > 0) break
        val pair = findLeftAndRight(-array[i], array)
        var left = pair.first
        var right = pair.second
        while (left > i && right < array.size) {
            when {
                array[left] + array[right] + array[i] == 0 -> {
                    count++
                    if (left - 1 > i && array[left] == array[left - 1]) {
                        left--
                    } else {
                        right++
                    }
                }
                array[left] + array[right] + array[i] > 0 -> {
                    left--
                }
                array[left] + array[right] + array[i] < 0 -> {
                    right++
                }
            }
        }
    }
    return count
}

fun main() {
    timeRatio { ex15b(it) }
}