package chapter1.section4

import extensions.inputPrompt
import extensions.readAllInts
import extensions.readInt

/**
 * 给定一个大小为N，按升序排列的数组，判断它是否包含给定的整数，
 * 要求只能使用加减法及常数的额外内存空间，且程序在最坏情况下运行时间与logN成正比
 *
 * 解：使用斐波那契数列替代2的幂（二分法）进行查找
 * 斐波那契数列初始值为0和1，后一个值为前面两个值的和，0 1 1 2 3 5 8 13 21 43 55 89 144 233 ...
 * 原理：数列中倒数第一个数约为倒数第三个数的两倍
 * 从0开始累加，找到第一个比数组长度大的数列值，最后一位值为last1，倒数第二值为last2，倒数第三个值为last3...，
 * 记录最后两位数last1和last2，其余值可以通过减法获得
 * 设查找范围为[start, end]，中值为mid，区间长度（比点的数量少1）为last1
 * 区间可以分为左右两侧，左侧长度为last3，右侧长度为last2，总和last3+last2=last1
 * 所以end=start+last1 mid=start+last3 无需记录end和mid的值，每个循环中重新计算即可
 * 第一次查找时start=0,end=last1,mid=last3，比较中值和给定key的大小
 * 若key小于中值，则起始位置不变，区间长度等于中值左半侧的长度，start不变，last1=last3, last2=last2-last3
 * 若key大于中值，则起始位置为中值mid，区间长度等于中值右半侧的长度，start=mid, last1=last2, last2=last3
 * 若中值超出数组实际取值范围，则与key小于中值执行相同逻辑
 * 在新的区间内重新查找，不断循环，数列的间隔越来越小
 * 当中值与key相等时表示找到，last3小于等于0时中值仍不等于key表示未找到（注意边界情况）
 */
fun ex22_BinarySearchAddSub(key: Int, array: IntArray): Int {
    if (array.isEmpty()) return -1
    var last1 = 1
    var last2 = 0
    while (true) {
        val last3 = last2
        last2 = last1
        last1 = last2 + last3
        if (last1 >= array.size) {
            break
        }
    }
    var start = 0
    while (true) {
        val last3 = last1 - last2
        //实际上并没有用到end参数
        val end = start + last1
        val mid = start + last3
        if (last3 <= 0) return if (array[start] == key) start else -1
        if (mid > array.size - 1 || array[mid] > key) {
            last1 = last3
            last2 -= last3
        } else if (array[mid] < key) {
            start= mid
            last1 = last2
            last2 = last3
        } else if (array[mid] == key) {
            return mid
        }
    }
}

fun main() {
    inputPrompt()
    val key = readInt()
    val array = readAllInts()
    array.sort()
    val index = ex22_BinarySearchAddSub(key, array)
    println(array.joinToString())
    println("key = $key  index = $index")
}