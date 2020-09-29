package chapter3.section2

import edu.princeton.cs.algs4.Queue

/**
 * 有序性检查
 * 编写一个递归的方法isOrdered()，接受一个结点Node和min、max两个键作为参数。
 * 如果以该结点为根的子树中所有结点都在min和max之间，min和max的确分别是树中最小和最大的结点
 * 且二叉查找树的有序性对树中的所有键都成立，返回true，否则返回false
 */
fun <K : Comparable<K>> isOrdered(node: BinaryTreeST.Node<K, *>, min: K, max: K): Boolean {
    val queue = Queue<BinaryTreeST.Node<K, *>>()
    collectAllNode(node, queue)
    var lastNode = queue.dequeue()
    if (lastNode.key != min) return false
    while (!queue.isEmpty) {
        val nextNode = queue.dequeue()
        if (lastNode.key >= nextNode.key) return false
        lastNode = nextNode
    }
    return lastNode.key == max
}

/**
 * 从小到大将所有结点依次加入队列中
 */
fun <K : Comparable<K>> collectAllNode(node: BinaryTreeST.Node<K, *>, queue: Queue<BinaryTreeST.Node<K, *>>) {
    if (node.left != null) {
        collectAllNode(node.left!!, queue)
    }
    queue.enqueue(node)
    if (node.right != null) {
        collectAllNode(node.right!!, queue)
    }
}

fun main() {
    val charArray = "EASYQUESTION".toCharArray()
    val binaryTreeST = BinaryTreeST<Char, Int>()
    for (i in charArray.indices) {
        binaryTreeST.put(charArray[i], i)
    }
    println(isOrdered(binaryTreeST.root!!, 'A', 'Y'))
    println(isOrdered(binaryTreeST.root!!, 'A', 'S'))
    println(isOrdered(binaryTreeST.root!!, 'E', 'Y'))
    println(isOrdered(binaryTreeST.root!!, 'A', 'Z'))

    val node = BinaryTreeST.Node(5, 0)
    node.left = BinaryTreeST.Node(2, 0)
    node.left!!.left = BinaryTreeST.Node(1, 0)
    node.left!!.right = BinaryTreeST.Node(5, 0)
    //放开下面注释返回true
//    node.left!!.right = BinaryTreeST.Node(4, 0)
    node.right = BinaryTreeST.Node(8, 0)
    node.right!!.left = BinaryTreeST.Node(7, 0)
    node.right!!.right = BinaryTreeST.Node(9, 0)
    println(isOrdered(node, 1, 9))
}