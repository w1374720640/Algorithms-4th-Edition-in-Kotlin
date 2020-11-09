package chapter3.section2

import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack
import extensions.shuffle

/**
 * 通过各种方式遍历二叉查找树
 * 前序、中序、后序的递归与非递归实现
 * 广度优先的二叉查找树遍历（层次遍历）
 */

/**
 * 递归实现的前序遍历
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.preorderTraversal(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    preorderTraversal(root!!, list)
    return list
}

private fun <K : Comparable<K>> preorderTraversal(node: BinarySearchTree.Node<K, *>, list: ArrayList<K>) {
    list.add(node.key)
    if (node.left != null) {
        preorderTraversal(node.left!!, list)
    }
    if (node.right != null) {
        preorderTraversal(node.right!!, list)
    }
}

/**
 * 递归实现的中序遍历
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.inorderTraversal(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    inorderTraversal(root!!, list)
    return list
}

private fun <K : Comparable<K>> inorderTraversal(node: BinarySearchTree.Node<K, *>, list: ArrayList<K>) {
    if (node.left != null) {
        inorderTraversal(node.left!!, list)
    }
    list.add(node.key)
    if (node.right != null) {
        inorderTraversal(node.right!!, list)
    }
}

/**
 * 递归实现的后序遍历
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.postorderTraversal(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    postorderTraversal(root!!, list)
    return list
}

private fun <K : Comparable<K>> postorderTraversal(node: BinarySearchTree.Node<K, *>, list: ArrayList<K>) {
    if (node.left != null) {
        postorderTraversal(node.left!!, list)
    }
    if (node.right != null) {
        postorderTraversal(node.right!!, list)
    }
    list.add(node.key)
}

/**
 * 非递归实现的前序遍历
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.preorderTraversalNonRecursive(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    val stack = Stack<BinarySearchTree.Node<K, *>>()
    stack.push(root!!)
    while (!stack.isEmpty) {
        val node = stack.pop()
        list.add(node.key)
        if (node.right != null) {
            stack.push(node.right!!)
        }
        if (node.left != null) {
            stack.push(node.left!!)
        }
    }
    return list
}

/**
 * 非递归实现的中序遍历
 * 先从根节点开始，依次将左子结点、左子结点的左子结点...依次加入栈中，直到结点左子结点为空
 * 从栈中弹出结点时，如果结点的右子结点为空，则直接弹出该结点，
 * 否则弹出该结点后，需要以该结点的右子结点为根节点，将所有左子结点依次加入栈中
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.inorderTraversalNonRecursive(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    val stack = Stack<BinarySearchTree.Node<K, *>>()
    addAllLeftNode(root!!, stack)
    while (!stack.isEmpty) {
        val node = stack.pop()
        list.add(node.key)
        if (node.right != null) {
            addAllLeftNode(node.right!!, stack)
        }
    }
    return list
}

private fun <K : Comparable<K>> addAllLeftNode(root: BinarySearchTree.Node<K, *>, stack: Stack<BinarySearchTree.Node<K, *>>) {
    var node: BinarySearchTree.Node<K, *>? = root
    while (node != null) {
        stack.push(node)
        node = node.left
    }
}


/**
 * 非递归实现的后序遍历
 * 实现方式参考非递归实现的中序遍历，区别是：
 * 中序遍历直接从栈中弹出结点，后序遍历需要确认该结点是否有右结点，如果有右子结点，需要先将右结点加入栈中再依次弹出
 * 需要注意，为了防止重复添加一个结点的右子结点造成死循环，需要记录最后添加的结点，
 * 当最后添加的结点和该结点的右子结点相同时，说明该结点的所有右子结点都已经添加，可以直接返回
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.postorderTraversalNonRecursive1(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    val stack = Stack<BinarySearchTree.Node<K, *>>()
    var lastAddNode: BinarySearchTree.Node<K, *>? = null
    addAllLeftNode(root!!, stack)
    while (!stack.isEmpty) {
        //中序直接pop，后序先peek再判断是否需要加入右子结点
        val node = stack.peek()
        if (node.right != null && node.right !== lastAddNode) {
            addAllLeftNode(node.right!!, stack)
        } else {
            list.add(node.key)
            lastAddNode = node
            stack.pop()
        }
    }
    return list
}

/**
 * 非递归实现的后序遍历
 * 修改前序遍历的非递归实现，以根-右-左的形式遍历二叉查找树
 * 遍历完成后将列表数据前后颠倒，结果为左-右-根的形式
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.postorderTraversalNonRecursive2(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    val stack = Stack<BinarySearchTree.Node<K, *>>()
    stack.push(root!!)
    while (!stack.isEmpty) {
        val node = stack.pop()
        list.add(node.key)
        if (node.left != null) {
            stack.push(node.left!!)
        }
        if (node.right != null) {
            stack.push(node.right!!)
        }
    }
    for (i in 0 until list.size / 2) {
        val temp = list[i]
        list[i] = list[list.size - 1 - i]
        list[list.size - 1 - i] = temp
    }
    return list
}

/**
 * 广度优先的二叉查找树遍历
 */
fun <K : Comparable<K>> BinarySearchTree<K, *>.breadthFirstTraversal(): List<K> {
    if (root == null) return emptyList()
    val list = ArrayList<K>()
    val queue = Queue<BinarySearchTree.Node<K, *>>()
    queue.enqueue(root!!)
    while (!queue.isEmpty) {
        val node = queue.dequeue()
        list.add(node.key)
        if (node.left != null) {
            queue.enqueue(node.left!!)
        }
        if (node.right != null) {
            queue.enqueue(node.right!!)
        }
    }
    return list
}

fun main() {
    val size = 10
    val bst = BinarySearchTree<Int, Int>()
    val array = Array(size) { it }
    array.shuffle()
    array.forEach { bst.put(it, 0) }
    drawBST(bst)

    println("preorder : ${bst.preorderTraversal().joinToString()}")
    println("preorder : ${bst.preorderTraversalNonRecursive().joinToString()}")
    println("inorder  : ${bst.inorderTraversal().joinToString()}")
    println("inorder  : ${bst.inorderTraversalNonRecursive().joinToString()}")
    println("postorder: ${bst.postorderTraversal().joinToString()}")
    println("postorder: ${bst.postorderTraversalNonRecursive1().joinToString()}")
    println("postorder: ${bst.postorderTraversalNonRecursive2().joinToString()}")
    println("breadth  : ${bst.breadthFirstTraversal().joinToString()}")
}