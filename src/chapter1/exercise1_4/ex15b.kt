package chapter1.exercise1_4

/**
 * 求三个数相加和为0的组合数量，要求使用平方级算法，参考ex15a
 */
fun ex15b(array: IntArray): Long {
    array.sort()
    var count = 0L
    for (i in array.indices) {
        if (array[i] > 0) break
        //若两个数相加等于A，那这两个值一定在二分之A的两侧
        val pair = findLeftAndRight(-array[i] / 2, array)
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