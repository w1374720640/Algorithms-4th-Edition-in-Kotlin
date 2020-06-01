package capter1.exercise1_1

import extensions.inputPrompt
import extensions.random
import extensions.readInt

//分别在N=1000,1_0000,10_0000,100_0000时，将以下实验运行T遍：
//生成两个大小为N的数组，数组值为随机6位正整数，使用二分查找找出同时存在与两个数组中的整数的数量
//打印出在不同的N中，T次实验该数量的平均值
fun ex39(T: Int) {
    fun setArrayToRandomValue(array: IntArray) {
        for (i in array.indices) {
            array[i] = random(100_0000)
        }
    }

    var N = 10 * 10
    for (i in 1..4) {
        N *= 10
        var total = 0
        repeat(T) {
            val array1 = IntArray(N)
            val array2 = IntArray(N)
            setArrayToRandomValue(array1)
            setArrayToRandomValue(array2)
            array1.sort()
            array2.sort()
            array1.forEach {
                if (binarySearch(it, array2) != -1) total++
            }
        }
        println("N=${N} T=${T} average=${total / T}")
    }
}

fun main() {
    inputPrompt()
    val times = readInt()
    ex39(times)
}