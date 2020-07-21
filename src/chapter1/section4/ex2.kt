package chapter1.section4

/**
 * 求数组中三元素和为0的组合数量，需要处理整数相加溢出的情况
 *
 * 解：两个数正负号相同才可能溢出，如果两个数正负号相同，相加后和的正负号和原来的符号不同，则发生溢出
 * 如果两个数相加溢出，第三个数在整数范围内无论取什么值，三个数的和都不可能为0
 * 唯一例外是前两个数的和溢出后的值为Int.MIN_VALUE，第三个值为Int.MIN_VALUE 前两个值溢出，但和为0
 * （因为最小负整数Int.MIN_VALUE的绝对值比最大正整数Int.MAX_VALUE的绝对值大1）
 * 也可以将Int转换为Long或BigInteger计算，这样比较简单一些
 */
fun ex2(array: IntArray): Long {
    val n = array.size
    var count = 0L
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            for (k in j + 1 until n) {
                val twoSum = array[i] + array[j]
                if (array[i] > 0 && array[j] > 0 && twoSum < 0) {
                    if (twoSum == Int.MIN_VALUE && array[k] == Int.MIN_VALUE) {
                        count++
                    } else {
                        continue
                    }
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
    //这组测试数据中三元素和为0的组合有
    //a[0]+a[2]+a[6] a[1]+a[2]+a[6] a[3]+a[4]+a[5]
    //被threeSum()函数误判的组合为 a[0]+a[1]+a[2]
    val array = intArrayOf(Int.MAX_VALUE, Int.MAX_VALUE, 1, 2, 3, -5, Int.MIN_VALUE)
    println("threeSumCount = ${threeSum(array)}")
    println("fixIntegerOverflowCount = ${ex2(array)}")
}