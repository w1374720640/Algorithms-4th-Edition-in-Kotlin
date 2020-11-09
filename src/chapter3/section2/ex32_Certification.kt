package chapter3.section2

/**
 * 验证
 * 编写一个方法isBST()，接受一个结点Node为参数，若该结点是一个二叉查找树的根节点则返回true，否则返回false
 * 提示：这个任务比看起来要困难，它和你调用前三题中各个方法的顺序有关
 *
 * 解：isBinaryTree必须第一个判断，否则有环的数据结构会导致后面的死循环
 * 在我的实现中isOrdered()和hasNoDuplicates()方法的顺序并不重要
 * hasNoDuplicates()不通过则isOrdered()也无法通过
 */
fun <K : Comparable<K>> isBST(node: BinarySearchTree.Node<K, *>): Boolean {
    if (!isBinaryTree(node)) return false
    if (!isOrdered(node, minNode(node).key, maxNode(node).key)) return false
    if (!hasNoDuplicates(node)) return false
    return true
}

fun <K : Comparable<K>> minNode(node: BinarySearchTree.Node<K, *>): BinarySearchTree.Node<K, *> {
    return if (node.left == null) node else minNode(node.left!!)
}

fun <K : Comparable<K>> maxNode(node: BinarySearchTree.Node<K, *>): BinarySearchTree.Node<K, *> {
    return if (node.right == null) node else maxNode(node.right!!)
}

fun main() {
    val charArray = "EASYQUESTION".toCharArray()
    val bst = BinarySearchTree<Char, Int>()
    for (i in charArray.indices) {
        bst.put(charArray[i], i)
    }
    println(isBST(bst.root!!))

    val node = BinarySearchTree.Node(5, 0)
    node.left = BinarySearchTree.Node(2, 0)
    node.left!!.left = BinarySearchTree.Node(1, 0)
    node.left!!.right = BinarySearchTree.Node(4, 0)
    node.right = BinarySearchTree.Node(8, 0)
    node.right!!.left = BinarySearchTree.Node(7, 0)
    node.right!!.right = BinarySearchTree.Node(9, 0)
    println(isBST(node))
}
