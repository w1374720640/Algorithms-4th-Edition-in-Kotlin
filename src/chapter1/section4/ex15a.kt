package chapter1.section4

import extensions.inputPrompt
import extensions.readAllInts

/**
 * 计算"已排序"数组中和为0的整数对数量，要求使用线性级别的算法，而不是基于二分查找的线性对数级算法
 *
 * 解：先找到值为0的元素数量，当多于一个时，用组合公式计算组合数，需要查找N次
 * 从两侧向中间查找，如果和大于0，右侧向左移一位，和小于0，左侧向右移一位
 * 如果和为0，判断元素值是否为0，为0则直接结束（因为第一步已经计算过包含0的组合数了）
 * 如果元素不为0，分别判断左侧相等的元素数量和右侧相等的元素数量，相乘后就是总的组合数
 */
fun ex15a(array: IntArray): Long {
    require(array.size > 1)
    fun getZeroCount(array: IntArray): Int {
        var count = 0
        array.forEach {
            if (it == 0) count++
        }
        return count
    }

    var count = 0L
    var left = 0
    var right = array.size - 1

    val zeroCount = getZeroCount(array)
    if (zeroCount > 1) {
        count += combination(zeroCount, 2)
    }
    while (left < right) {
        when {
            array[left] + array[right] > 0 -> right--
            array[left] + array[right] < 0 -> left++
            else -> {
                val value = array[left]
                if (value == 0) return count
                var leftEqualCount = 1
                //因为上面value==0时直接return，当索引left>=right时和不可能为0
                while (array[++left] == value) {
                    leftEqualCount++
                }
                var rightEqualCount = 1
                while (array[--right] == -value) {
                    rightEqualCount++
                }
                count += leftEqualCount * rightEqualCount
            }
        }
    }
    return count
}


fun main() {
    inputPrompt()
    //-2 -1 -1 -1 0 0 0 1 1 2 3 4 5
    val array = readAllInts()
    println("twoSum: count=${twoSum(array)}")
    array.sort()
    println("ex15a: count=${ex15a(array)}")

}