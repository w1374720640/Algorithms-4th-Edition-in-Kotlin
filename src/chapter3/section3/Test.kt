package chapter3.section3

import edu.princeton.cs.algs4.Stack
import extensions.random
import extensions.shuffle

/**
 * 红黑树的特性一：红链接均为左链接
 * （红黑树本身没有这个特性，但是这里的实现默认红链接为左链接，也可以用右链接实现，但是整个实现中要统一）
 */
fun <K : Comparable<K>, V : Any> allRedLinksAreLeftLinks(bst: RedBlackBST<K, V>): Boolean {
    if (bst.isEmpty()) return true
    val stack = Stack<RedBlackBST.Node<K, V>>()
    stack.push(bst.root!!)
    while (!stack.isEmpty) {
        val node = stack.pop()

        if (node.right.isRed()) return false
        if (node.right != null) {
            stack.push(node.right)
        }
        if (node.left != null) {
            stack.push(node.left)
        }
    }
    return true
}

fun <K : Comparable<K>, V : Any> RedBlackBST.Node<K, V>?.isRed(): Boolean {
    if (this == null) return false
    return color == RedBlackBST.RED
}

/**
 * 红黑树的特性二：没有任何一个结点同时和两条红链接相连
 */
fun <K : Comparable<K>, V : Any> noNodesConnectedTwoRedLinks(bst: RedBlackBST<K, V>): Boolean {
    if (bst.isEmpty()) return true
    val stack = Stack<RedBlackBST.Node<K, V>>()
    stack.push(bst.root!!)
    while (!stack.isEmpty) {
        val node = stack.pop()

        if ((node.isRed() && (node.left.isRed() || node.right.isRed()))
                || (!node.isRed() && node.left.isRed() && node.right.isRed())) return false

        if (node.right != null) {
            stack.push(node.right)
        }
        if (node.left != null) {
            stack.push(node.left)
        }
    }
    return true
}

/**
 * 红黑树的特性三：该树是黑色完美平衡的，即任意空链接到根结点的路径上的黑链接数量相同
 */
fun <K : Comparable<K>, V : Any> perfectBlackBalance(bst: RedBlackBST<K, V>): Boolean {
    if (bst.isEmpty()) return true
    var numOfBlackLinks: Int? = null

    val nodeStack = Stack<RedBlackBST.Node<K, V>>()
    nodeStack.push(bst.root!!)
    val numStack = Stack<Int>()
    numStack.push(0)

    while (!nodeStack.isEmpty) {
        val node = nodeStack.pop()
        val num = numStack.pop()

        if (node.left == null && node.right == null) {
            if (numOfBlackLinks == null) {
                numOfBlackLinks = num
            } else {
                if (numOfBlackLinks != num) return false
            }
        }
        if (node.right != null) {
            nodeStack.push(node.right)
            numStack.push(if (node.right.isRed()) num else num + 1)
        }
        if (node.left != null) {
            nodeStack.push(node.left)
            numStack.push(if (node.left.isRed()) num else num + 1)
        }
    }
    return true
}

/**
 * 测试红黑树是否符合上述三个特性
 */
fun testRedBlackBST(bst: RedBlackBST<Int, Int>) {
    require(bst.isEmpty())

    val array = IntArray(100) { it }
    array.shuffle()
    array.forEach {
        bst.put(it, 0)
    }

    check(bst.size() == 100)
    check(allRedLinksAreLeftLinks(bst))
    check(noNodesConnectedTwoRedLinks(bst))
    check(perfectBlackBalance(bst))

    repeat(10) {
        bst.delete(bst.select(random(bst.size())))
    }
    check(bst.size() == 90)
    check(allRedLinksAreLeftLinks(bst))
    check(noNodesConnectedTwoRedLinks(bst))
    check(perfectBlackBalance(bst))

    repeat(10) {
        bst.deleteMin()
    }
    check(bst.size() == 80)
    check(allRedLinksAreLeftLinks(bst))
    check(noNodesConnectedTwoRedLinks(bst))
    check(perfectBlackBalance(bst))

    repeat(10) {
        bst.deleteMin()
    }
    check(bst.size() == 70)
    check(allRedLinksAreLeftLinks(bst))
    check(noNodesConnectedTwoRedLinks(bst))
    check(perfectBlackBalance(bst))

    println("RedBlackBST check succeed.")
}