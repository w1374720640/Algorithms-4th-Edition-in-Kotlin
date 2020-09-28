package chapter3.section2

import chapter2.sleep
import extensions.shuffle

/**
 * 完美平衡
 * 编写一段程序，用一组键构造一颗和二分查找等价的二叉查找树
 * 也就是说，在这棵树中查找任意键所产生的比较序列和在这组键中使用二分查找所产生的比较序列完全相同
 *
 * 解：对数组排序，然后递归取范围内中值插入二叉树中
 */
fun <K : Comparable<K>, V : Any> ex25_PerfectBalance(keys: Array<K>, defaultValue: V): BinaryTreeST<K, V> {
    keys.sort()
    val binaryTreeST = BinaryTreeST<K, V>()
    ex25_PerfectBalance(keys, defaultValue, binaryTreeST, 0, keys.size - 1)
    return binaryTreeST
}

fun <K : Comparable<K>, V : Any> ex25_PerfectBalance(keys: Array<K>, defaultValue: V, binaryTreeST: BinaryTreeST<K, V>, low: Int, high: Int) {
    if (low > high) return
    if (low == high) {
        binaryTreeST.put(keys[low], defaultValue)
        return
    }
    val mid = (low + high) / 2
    binaryTreeST.put(keys[mid], defaultValue)
    ex25_PerfectBalance(keys, defaultValue, binaryTreeST, low, mid - 1)
    ex25_PerfectBalance(keys, defaultValue, binaryTreeST, mid + 1, high)
}

fun main() {
    val times = 50
    val delay = 1000L

    repeat(times) { index ->
        val array = Array(index + 1) { it }
        array.shuffle()
        drawBinaryTree(ex25_PerfectBalance(array, 0), false)
        sleep(delay)
    }
}