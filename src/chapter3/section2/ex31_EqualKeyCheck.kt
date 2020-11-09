package chapter3.section2

import edu.princeton.cs.algs4.Stack

/**
 * 等值键检查
 * 编写一个方法hasNoDuplicates()，接受一个结点Node为参数
 * 如果以该结点为根的二叉查找树中不含有等值的键则返回true，否则返回false
 * 假设树已经通过了前几道练习的检查
 *
 * 解：遍历该结点，并将每个结点的键加入到一个二叉查找树中
 * 加入前先判断该键是否存在，如果存在则返回false，如果所有结点都不存在则返回true
 */
fun <K : Comparable<K>> hasNoDuplicates(node: BinarySearchTree.Node<K, *>): Boolean {
    val bst = BinarySearchTree<K, Any>()

    //使用非递归的前序遍历遍历结点
    val stack = Stack<BinarySearchTree.Node<K, *>>()
    stack.push(node)
    while (!stack.isEmpty) {
        val parentNode = stack.pop()

        if (bst.contains(parentNode.key)) return false
        bst.put(parentNode.key, parentNode.value)

        if (parentNode.right != null) {
            stack.push(parentNode.right!!)
        }
        if (parentNode.left != null) {
            stack.push(parentNode.left!!)
        }
    }
    return true
}

fun main() {
    val node = BinarySearchTree.Node(5, 0)
    node.left = BinarySearchTree.Node(2, 0)
    node.left!!.left = BinarySearchTree.Node(1, 0)
    node.left!!.right = BinarySearchTree.Node(5, 0)
    //放开下面注释返回true
//    node.left!!.right = BinarySearchTree.Node(4, 0)
    node.right = BinarySearchTree.Node(8, 0)
    node.right!!.left = BinarySearchTree.Node(7, 0)
    node.right!!.right = BinarySearchTree.Node(9, 0)

    println(hasNoDuplicates(node))
}