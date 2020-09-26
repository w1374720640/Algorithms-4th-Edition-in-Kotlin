package chapter3.section2

import chapter1.section4.factorial
import chapter2.sleep
import chapter2.swap

/**
 * 对于N=2、3、4、5和6，画出用N个键可能构造出的所有不同形状的二叉查找树
 *
 * 解：对于N个不同的键，有N!种插入顺序，可以构造出N!个二叉查找树，
 * 但是构造的二叉查找树中会有结构相同的树，需要剔除结构相同的树
 * 自定义二叉树的比较器，从根节点开始依次比较每个结点，只有当结点结构相同、键相同时才认为二叉树相同，
 * 对二叉树组成的数组排序，然后去除重复的二叉树
 * 数组中删除重复元素可以参考练习2.5.4
 */
fun ex9(N: Int, delay: Long) {
    val allBinaryTree = createAllBinaryTree(N)
    val comparator = BinaryTreeComparator()
    allBinaryTree.sortWith(comparator)
    val array = dedupBinaryTree(allBinaryTree, comparator)
    println("N: $N   number of binary trees: ${array.size}")
    drawBinaryTreeArray(array, delay)
}

fun createAllBinaryTree(N: Int): Array<BinaryTreeST<Int, Int>> {
    val array = Array(N) { it + 1 }
    val list = ArrayList<Array<Int>>()
    fullArray(array, 0, list)
    check(list.size == factorial(N).toInt())
    return Array(list.size) { index ->
        BinaryTreeST<Int, Int>().apply {
            list[index].forEach { key ->
                put(key, 0)
            }
        }
    }
}

/**
 * 求长度为N，元素不重复数组的所有全排列
 *
 * 解：全排列P(N)=N*P(N-1)
 * 分别将0,1,2...N-1交换到第一位，对剩余元素求全排列P(N-1)，组合成全排列P(N)
 * 求全排列P(N-1)时，递归调用(N-1)*P(N-2)，直到P(1)只有一种可能性
 */
fun fullArray(array: Array<Int>, start: Int, list: ArrayList<Array<Int>>) {
    if (start == array.size - 1) {
        list.add(array.copyOf())
        return
    }
    for (i in start until array.size) {
        array.swap(start, i)
        fullArray(array, start + 1, list)
        array.swap(start, i)
    }
}

class BinaryTreeComparator : Comparator<BinaryTreeST<Int, Int>> {
    override fun compare(o1: BinaryTreeST<Int, Int>, o2: BinaryTreeST<Int, Int>): Int {
        return compare(o1.root, o2.root)
    }

    fun compare(node1: BinaryTreeST.Node<Int, Int>?, node2: BinaryTreeST.Node<Int, Int>?): Int {
        if (node1 == null && node2 == null) return 0
        if (node1 == null) return -1
        if (node2 == null) return 1
        val result1 = node1.key.compareTo(node2.key)
        if (result1 != 0) return result1
        val result2 = compare(node1.left, node2.left)
        if (result2 != 0) return result2
        return compare(node1.right, node2.right)
    }
}

/**
 * 去除重复元素
 */
fun dedupBinaryTree(array: Array<BinaryTreeST<Int, Int>>, comparator: Comparator<BinaryTreeST<Int, Int>>): Array<BinaryTreeST<Int, Int>> {
    var delCount = 0
    for (i in 1 until array.size) {
        if (comparator.compare(array[i], array[i - 1]) == 0) {
            delCount++
        }
    }
    if (delCount == 0) return array
    val result = array.copyOfRange(0, array.size - delCount)
    //从后向前遍历可以重复利用delCount变量，也可以减少部分循环次数
    for (i in array.size - 1 downTo 1) {
        if (comparator.compare(array[i], array[i - 1]) == 0) {
            delCount--
            //第一个重复元素前面的元素不需要重新赋值
            if (delCount == 0) break
        } else {
            result[i - delCount] = array[i]
        }
    }
    return result
}

fun drawBinaryTreeArray(array: Array<BinaryTreeST<Int, Int>>, delay: Long) {
    array.forEach { binaryTreeST ->
        drawBinaryTree(binaryTreeST)
        sleep(delay)
    }
}

fun main() {
    var N = 2
    val delay = 2000L
    repeat(5) {
        ex9(N, delay)
        N++
    }
}