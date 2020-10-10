package chapter3.section2

import edu.princeton.cs.algs4.Queue
import extensions.shuffle

/**
 * 按层遍历
 * 编写一个方法printLevel()，接受一个结点Node作为参数，按照层级顺序打印以该结点为根的子树
 * （即按每个结点到根结点的距离的顺序，同一层的结点应该按从左至右的顺序）
 * 提示：使用队列Queue
 *
 * 解：二叉树的广度优先遍历，参考BinaryTreeTraversal文件中的breadthFirstTraversal()方法
 */
fun <K : Comparable<K>> printLevel(root: BinaryTreeST.Node<K, *>) {
    val queue = Queue<BinaryTreeST.Node<K, *>>()
    queue.enqueue(root)
    while (!queue.isEmpty) {
        val node = queue.dequeue()
        print("${node.key} ")
        if (node.left != null) {
            queue.enqueue(node.left!!)
        }
        if (node.right != null) {
            queue.enqueue(node.right!!)
        }
    }
}

fun main() {
    val size = 10
    val binaryTreeST = BinaryTreeST<Int, Int>()
    val array = Array(size) { it }
    array.shuffle()
    array.forEach { binaryTreeST.put(it, 0) }
    drawBinaryTree(binaryTreeST)

    printLevel(binaryTreeST.root!!)
}