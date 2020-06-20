package chapter1.exercise1_4

/**
 * 求数组中三元素和为0的组合数量，需要处理整数相加溢出的情况
 */
fun ex2(array: IntArray): Long {
    val n = array.size
    var count = 0L
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            for (k in j + 1 until n) {
                val twoSum = array[i] + array[j]
                //两个数符号相同才可能溢出，和的结果与相加的数正负号不同时溢出
                //如果两个数相加溢出，第三个数在整数范围内无论取什么值，三个数的和都不可能为0
                if (array[i] > 0 && array[j] > 0 && twoSum < 0) {
                    continue
                } else if (array[i] < 0 && array[j] < 0 && twoSum > 0) {
                    continue
                } else if (twoSum + array[k] == 0) {
                    count++
                }
            }
        }
    }
    return count
}

fun main() {
    val array = intArrayOf(Int.MAX_VALUE, Int.MAX_VALUE, 2, 3, -5)
    println("threeSumCount = ${threeSum(array)}")
    println("fixIntegerOverflowCount = ${ex2(array)}")
}