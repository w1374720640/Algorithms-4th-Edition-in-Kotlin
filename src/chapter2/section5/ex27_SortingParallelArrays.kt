package chapter2.section5

import chapter2.getDoubleArray
import chapter2.less
import chapter2.section2.ex20CheckAscOrder
import chapter2.swapCallbackList

/**
 * 平行数组的排序
 * 在将平行数组排序时，可以将索引排序并返回一个index[]数组
 * 为Insertion添加一个indirectSort()方法，接受一个Comparable的对象数组a[]作为参数，
 * 但它不会将a[]中的元素重新排列，而是返回一个整型数组index[]使得a[index[0]]到a[index[N-1]]正好是升序的
 *
 * 解：参考练习2.2.20，只是将归并排序替换为插入排序
 */
fun <T : Comparable<T>> indirectSort(array: Array<T>): IntArray {
    val index = IntArray(array.size) { it }
    for (i in 1 until array.size) {
        for (j in i downTo 1) {
            if (array.less(index[j], index[j - 1])) {
                index.swap(j, j - 1)
            } else {
                break
            }
        }
    }
    return index
}

fun IntArray.swap(i: Int, j: Int) {
    if (i == j) return
    swapCallbackList.forEach { it.before(this, i, j) }
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
    swapCallbackList.forEach { it.after(this, i, j) }
}

fun main() {
    val size = 10000
    val array = getDoubleArray(size)
    val index = indirectSort(array)
    println(ex20CheckAscOrder(array, index))
}