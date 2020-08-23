package chapter2.section3

import chapter2.ArrayInitialState
import chapter2.compare
import chapter2.section1.cornerCases
import chapter2.sortMethodsCompare
import chapter2.swap

/**
 * 快速三向切分
 * 用将重复元素放置于子数组两端的方式实现一个信息量最优的排序算法
 * 使用两个索引p和q，使得a[lo..p-1]和a[q+1..hi]的元素都和a[lo]相等
 * 使用另外两个索引i和j，使得a[p..i-1]小于a[lo]，a[j+i..q]大于a[lo]
 * 在内循环中加入代码，在a[i]和v相当时将其与a[p]交换（并将p加1）
 * 在a[j]和v相等且a[i]和a[j]尚未和v进行比较之前将其与a[q]交换
 * 添加在切分循环结束后将和v相等的元素交换到正确位置的代码，如图所示
 * 请注意：这里实现的代码和正文中给出的代码是等价的，因为这里额外的交换用于和切分元素相等的元素
 * 而正文中的代码将额外的交换用于和切分元素不等的元素
 * 排序前：
 * |v|                                           |
 *  ↑                                           ↑
 *  lo                                          hi
 *
 *  排序中：
 * |  =v  |  <v  |                 |  >v  |  =v  |
 *  ↑      ↑      ↑               ↑        ↑    ↑
 *  lo     p      i               j        q    hi
 *
 *  排序后（未将和a[lo]相等的值移动至数组中间）：
 * |    =v    |     <v    |    >v     |    =v    |
 *  ↑          ↑         ↑ ↑           ↑        ↑
 *  lo         p         j i           q        hi
 *
 *  排序后（最终状态）：
 * |    <v    |          =v           |    >v    |
 *  ↑        ↑                         ↑        ↑
 *  lo       j                         i        hi
 *
 *  解：这个算法是快速排序三路算法的升级版，需要从左右两侧同时向中间遍历
 *  需要注意上图中每个变量相对于分界点的位置
 */
fun <T : Comparable<T>> quickSortFast3Way(array: Array<T>) {
    quickSortFast3Way(array, 0, array.size - 1)
}

fun <T : Comparable<T>> quickSortFast3Way(array: Array<T>, start: Int, end: Int) {
    if (start >= end) return
    var p = start + 1
    var q = end + 1
    var i = p
    var j = end
    val value = array[start]
    while (true) {
        while (true) {
            val compare = array.compare(i, value)
            if (compare == 0) {
                array.swap(i++, p++)
            } else if (compare > 0) {
                array.swap(i, j--)
                break
            } else {
                i++
            }
            if (i > j) break
        }
        if (i > j) break
        while (true) {
            val compare = array.compare(j, value)
            if (compare == 0) {
                array.swap(j--, --q)
            } else if (compare > 0) {
                j--
            } else {
                array.swap(i++, j)
                break
            }
            if (i > j) break
        }
        if (i > j) break
    }
    //小于array[start]的部分有数据
    if (i > p) {
        while (p > start) {
            array.swap(--p, j--)
        }
    } else {
        j = start - 1
    }
    //大于array[start]的部分有数据
    if (j < q - 1) {
        while (q <= end) {
            array.swap(q++, i++)
        }
    } else {
        i = end + 1
    }
    quickSortFast3Way(array, start, j)
    quickSortFast3Way(array, i, end)
}

fun main() {
    cornerCases(::quickSortFast3Way)

    val sortMethods: Array<Pair<String, (Array<Double>) -> Unit>> = arrayOf(
            "quickSortNotShuffle" to ::quickSortNotShuffle,
            "quickSort3Way" to ::quickSort3Way,
            "quickSortFast3Way" to ::quickSortFast3Way
    )
    println("ArrayInitialState.REPEAT:")
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.REPEAT)
    println("ArrayInitialState.RANDOM:")
    sortMethodsCompare(sortMethods, 10, 100_0000, ArrayInitialState.RANDOM)
}