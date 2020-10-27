package chapter3.section3

import extensions.shuffle

/**
 * 允许红色右链接
 * 修改你为练习3.3.25给出的答案，允许红色右链接的存在
 */
class AllowRightLeaningRedLinksTree<K : Comparable<K>, V : Any> : TopDown234Tree<K, V>() {
    override fun put(key: K, value: V) {
        if (isEmpty()) {
            root = Node(key, value, color = BLACK)
        } else {
            root = put(root, key, value)
            root!!.color = BLACK
        }
    }

    override fun put(node: Node<K, V>?, key: K, value: V): Node<K, V> {
        if (node == null) return Node(key, value)
        var h: Node<K, V> = node
        if (h.left.isRed() && h.right.isRed()) flipColors(h)

        when {
            key > h.key -> h.right = put(h.right, key, value)
            key < h.key -> h.left = put(h.left, key, value)
            else -> h.value = value
        }

        if (h.left.isRed() && h.left?.right.isRed()) h.left = rotateLeft(h.left!!)
        if (h.left.isRed() && h.left?.left.isRed()) h = rotateRight(h)
        if (h.right.isRed() && h.right?.left.isRed()) h.right = rotateRight(h.right!!)
        if (h.right.isRed() && h.right?.right.isRed()) h = rotateLeft(h)

        resize(h)
        return h
    }
}

fun main() {
    test234Tree(AllowRightLeaningRedLinksTree())

    val array = IntArray(50) { it }
    array.shuffle()
    val topDown234Tree = AllowRightLeaningRedLinksTree<Int, Int>()
    array.forEach {
        topDown234Tree.put(it, 0)
    }
    drawRedBlackBST(topDown234Tree, showKey = false)
}