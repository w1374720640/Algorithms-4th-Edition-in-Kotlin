package chapter2.section5

import chapter2.section1.checkAscOrder
import chapter2.section3.quickSort
import chapter2.section4.heapSort
import extensions.random
import extensions.shuffle
import extensions.spendTimeMillis

/**
 * 实现一个方法String[] dedup(String[] a)，返回一个有序的a[]，并删去其中重复的元素
 *
 * 解：复制一个新数组，对新数组用快速排序排序，
 * 遍历数组，设删除的重复元素数量为i，遍历时的索引为j，
 * 若a[j]==a[j-1]，则a[j]=a[j-1],a[j-1]=a[i],i++，直到数组遍历完成
 * 复制数组[i,size)范围内的元素到新数组，用堆排序后返回
 * 时间复杂度为: 复制数组N + 快速排序NlgN + 遍历数组N + 复制数组N + 堆排序NlgN ~NlgN
 * 因为需要复制两次数组，所以空间复杂度为2N
 * 注意：重复元素要和数组前半部分交换，不能和后半部分交换，因为前半部分元素已去重，后半部分元素未去重
 * 也不能直接交换a[j]和a[i]，直接交换后a[j+1]无法和a[j]比较是否重复
 * 去重后的数组用堆排序比用快速排序更快（用堆排序正序数组和逆序数组都比随机数组要快）
 */
inline fun <reified T : Comparable<T>> Array<T>.dedup(): Array<T> {
    val copyArray = this.copyOf()
    quickSort(copyArray)
    var i = 0
    for (j in 1 until copyArray.size) {
        if (copyArray[j] == copyArray[j - 1]) {
            copyArray[j] = copyArray[j - 1]
            copyArray[j - 1] = copyArray[i]
            i++
        }
    }
    val copyArray2 = copyArray.copyOfRange(i, copyArray.size)
    heapSort(copyArray2)
    return copyArray2
}

fun main() {
    val size = 100_0000
    val array = Array(size) { it.toString() }
    shuffle(array)
    repeat(size / 10) {
        array[random(size)] = random(size).toString()
    }
    var result: Array<String> = emptyArray()
    val time = spendTimeMillis {
        result = array.dedup()
    }
    val isAscOrder = result.checkAscOrder()
    var hasDup = false
    for (i in 1 until result.size) {
        if (result[i] == result[i - 1]) {
            hasDup = true
            break
        }
    }
    println("size=$size resultSize=${result.size} isAscOrder=${isAscOrder} hasDup=$hasDup time=$time ms")
}