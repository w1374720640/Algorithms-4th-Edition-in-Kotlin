package chapter3.section2

/**
 * 高度为N含有N个结点的二叉查找树能有多少种形状？
 * 使用N个不同的键能有多少种不同的方式构造一颗高度为N的二叉查找树？（参考练习3.2.2）
 *
 * 解：可以参考练习3.2.9中求所有不同形状的二叉查找树的方式，
 * 区别是3.2.9要求全排列，这里要求最坏情况，最小值只能在最左侧或最右侧，所以只保留第一个和最后一个排列，
 * 例如全排列P(N) = 0P(N-1) + 1P(N-1) + 2P(N-1) + ... + (N-1)P(N-1)
 * 最坏情况  P(N) = 0P(N-1) + (N-1)P(N-1)
 */
fun ex11(N: Int, delay: Long) {
    val allBinaryTree = createWorstBinaryTree(N)
    //最坏二叉查找树无需去重
    println("N: $N   number of worst binary trees: ${allBinaryTree.size}")
    drawBSTArray(allBinaryTree, delay)
}

fun createWorstBinaryTree(N: Int): Array<BinarySearchTree<Int, Int>> {
    val array = Array(N) { it + 1 }
    val list = ArrayList<Array<Int>>()
    worstBinaryTree(array, 0, list)
    //最坏情况数量应该等于2^(N-1)
    check(list.size == 1 shl (N - 1))
    return Array(list.size) { index ->
        BinarySearchTree<Int, Int>().apply {
            list[index].forEach { key ->
                put(key, 0)
            }
        }
    }
}

fun worstBinaryTree(array: Array<Int>, start: Int, list: ArrayList<Array<Int>>) {
    if (start == array.size - 1) {
        list.add(array.copyOf())
        return
    }
    worstBinaryTree(array, start + 1, list)

    //最后一位放到第一位，第一位挪到第二位，第二位挪到第三位...
    val last = array.last()
    for (i in array.size - 2 downTo start) {
        array[i + 1] = array[i]
    }
    array[start] = last
    worstBinaryTree(array, start + 1, list)
    val first = array[start]
    for (i in start..array.size - 2) {
        array[i] = array[i + 1]
    }
    array[array.size - 1] = first
}

fun main() {
    var N = 2
    val delay = 500L
    repeat(5) {
        ex11(N, delay)
        N++
    }
}