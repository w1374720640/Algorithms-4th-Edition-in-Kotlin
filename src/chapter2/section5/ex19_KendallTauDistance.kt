package chapter2.section5

import chapter2.less
import chapter2.section2.mergeSortCallbackList

/**
 * 编写一段程序，在线性对数时间内计算两组排列之间的Kendall tau距离
 * Kendall tau距离就是两个排列之间的逆序数，它反应了两个排列的相似程度，例如两个在区间[0,6]的排列
 * a={0, 3, 1, 6, 2, 5, 4}
 * b={1, 0, 3, 6, 4, 2, 5}
 * 求a，b的Kendall tau距离，就是求两个排列之间的逆序{0, 1}, {3, 1}, {2, 4}, {5, 4}，一共为4对，故Kendall tau距离为4
 * 类似的算法如2.2.19，统计一个数组中的“倒置”数量
 *
 * 解：需要结合练习2.5.16和练习2.2.19的思想
 * 创建一个数组c，a数组中元素的索引为i，值为a[i]，使得c[a[i]]=i
 * 对数组b排序，不用元素的自然大小排序，以练习2.5.16中的方法，用数组c为每个数设置不同的大小顺序
 * 因为要线性对数时间内计算，所以用练习2.2.19中的方法计算计算倒置数量，这个倒置数量就是Kendall tau距离
 * 时间复杂度: 创建数组c:O(N) 归并排序:O(NlgN) 总:O(NlgN)
 * 空间复杂度: 创建数组c:O(N) 归并排序:O(N) 总:O(N)
 * 这里的函数参数用Array<Int>，且要求数组内元素小于数组大小，否则要用HashMap存储，参考练习2.5.16
 */
fun ex19_KendallTauDistance(a: Array<Int>, b: Array<Int>): Long {
    val c = IntArray(a.size)
    for (i in a.indices) {
        require(a[i] < a.size)
        c[a[i]] = i
    }
    val comparator = Comparator<Int> { o1, o2 ->
        c[o1].compareTo(c[o2])
    }
    return topDownMergeSortWithComparator(b, comparator)
}


/**
 * 基于自定义比较器的归并排序（自顶向下）
 * 返回数组中“倒置”的数量，参考练习2.2.19
 */
fun <T> topDownMergeSortWithComparator(originalArray: Array<T>, comparator: Comparator<T>): Long {
    if (originalArray.size <= 1) return 0
    val extraArray = originalArray.copyOf()
    return topDownMergeSortWithComparator(originalArray, extraArray, 0, originalArray.size - 1, comparator)
}

fun <T> topDownMergeSortWithComparator(originalArray: Array<T>, extraArray: Array<T>, start: Int, end: Int, comparator: Comparator<T>): Long {
    if (start >= end) return 0
    val mid = (start + end) / 2
    val leftCount = topDownMergeSortWithComparator(originalArray, extraArray, start, mid, comparator)
    val rightCount = topDownMergeSortWithComparator(originalArray, extraArray, mid + 1, end, comparator)
    val mergeCount = mergeWithComparator(originalArray, extraArray, start, mid, end, comparator)
    return leftCount + rightCount + mergeCount
}

fun <T> mergeWithComparator(originalArray: Array<T>, extraArray: Array<T>, start: Int, mid: Int, end: Int, comparator: Comparator<T>): Long {
    mergeSortCallbackList.forEach {
        it.mergeStart(start, end)
    }
    for (i in start..end) {
        extraArray[i] = originalArray[i]
    }
    var i = start
    var j = mid + 1
    var k = start
    var count = 0L
    while (k <= end) {
        when {
            i > mid -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                originalArray[k++] = extraArray[j++]
            }
            j > end -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                originalArray[k++] = extraArray[i++]
            }
            //这里必须先处理a[j]<a[i]的情况，保证a[i]==a[j]时先将左侧的a[i]赋值到a[k]中，保证算法的稳定
            extraArray.less(j, i, comparator) -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(j, k) }
                originalArray[k++] = extraArray[j++]
                //总数加上左半边剩余未排序的数量
                count += mid - i + 1
            }
            else -> {
                mergeSortCallbackList.forEach { it.copyToOriginal(i, k) }
                originalArray[k++] = extraArray[i++]
            }
        }
    }
    return count
}

fun main() {
    val a = arrayOf(0, 3, 1, 6, 2, 5, 4)
    val b = arrayOf(1, 0, 3, 6, 4, 2, 5)
    println("KendallTauDistance=${ex19_KendallTauDistance(a, b)}")
}
