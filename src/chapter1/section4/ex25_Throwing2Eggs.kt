package chapter1.section4

import extensions.formatDouble
import extensions.inputPrompt
import extensions.readInt
import kotlin.math.sqrt

/**
 * 扔两个鸡蛋
 * 和上一题相同的问题，但现在假设你只有两个鸡蛋，而你的成本模型则是扔鸡蛋的次数。
 * 设计一种策略，最多扔2*sqrt(N)次鸡蛋即可判断出F的值，然后想办法把这个成本降低到~c*sqrt(F)次。
 * 这和查找命中（鸡蛋完好无损）比未命中（鸡蛋被摔碎）的成本小得多的情形类似。
 *
 * 解：先求出sqrt(N)的值，分别从1*sqrt(N)，2*sqrt(N)，3*sqrt(N)...k*sqrt(N)开始扔，直到鸡蛋摔碎
 * 因为sqrt(N)*sqrt(N)=N，所以在最坏情况下，k=sqrt(N)
 * 然后从(k-1)*sqrt(N)开始，逐层向上扔，最坏情况下，需要扔k*sqrt(N)-(k-1)*sqrt(N)=sqrt(N)次
 * 所以整体在最坏情况下需要扔2*sqrt(N)次
 */
fun ex25a_Throwing2Eggs(array: IntArray): Pair<Int, Int> {
    val sqrt = sqrt(array.size.toDouble())
    var k = 0
    var count = 0
    var floor: Int
    do {
        k++
        count++
        floor = (k * sqrt).toInt()
    } while (floor < array.size && array[floor] < 1)
    for (i in ((k - 1) * sqrt).toInt() + 1..minOf(array.size - 1, floor)) {
        count++
        if (array[i] == 1) {
            return i to count
        }
    }
    return -1 to count
}

/**
 * 条件和上面相同，把成本（扔的次数）降低到~c*sqrt(N)
 *
 * 解：因为1+2+3+4+...+k约等于(k^2)/2
 * 设Sk=1+2+3+...+k
 * k从1开始累加，分别在Sk层扔鸡蛋，当鸡蛋摔碎时Sk=F，需要尝试k次
 * 推导出(k^2)/2=F k=sqrt(2)*sqrt(F)
 * 然后从S(k-1)处开始逐层向上扔，需要尝试k次
 * 所以总的尝试的次数为2k=2*sqrt(2)*sqrt(F) c=2*sqrt(2)
 */
fun ex25b_Throwing2Eggs(array: IntArray): Pair<Int, Int> {
    var k = 1
    var S = k
    var count = 0
    while (S < array.size && array[S] < 1) {
        k++
        S += k
        count++
    }
    for (i in S - k + 1..minOf(array.size - 1, S)) {
        count++
        if (array[i] == 1) {
            return i to count
        }
    }
    return -1 to count
}

fun main() {
    inputPrompt()
    val total = readInt("total: ")
    val floor = readInt("floor: ")
    val array = IntArray(total) { if (it < floor) 0 else 1 }
    val resultA = ex25a_Throwing2Eggs(array)
    println(array.joinToString(limit = 100))
    println("ex25a index=${resultA.first} count=${resultA.second}  2*sqrt(N)=${formatDouble(2 * sqrt(total.toDouble()), 2)}")
    val resultB = ex25b_Throwing2Eggs(array)
    println("ex25a index=${resultB.first} count=${resultB.second}  2*sqrt(2)*sqrt(F)=${formatDouble(2 * sqrt(2.0) * sqrt(floor.toDouble()), 2)}")
}