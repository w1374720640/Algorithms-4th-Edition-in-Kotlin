package chapter3.section3

import extensions.shuffle

/**
 * 自顶向下一遍完成
 * 修改你为练习3.3.25给出的答案，不使用递归
 * 在沿查找路径向下的过程中分解并平衡4-结点（以及3-结点），最后在树底插入新键即可
 *
 * 解：沿查找路径向下的过程中通过旋转和颜色翻转保证下一个查找点不是4-结点，
 * 这样插入后再经过旋转就可以得到3-结点或4-结点（不需要颜色翻转）
 * 对父结点或父结点的父结点进行旋转最多只会得到4-结点，不需要继续向上配平
 */
open class SingleTopDownPass234Tree<K : Comparable<K>, V : Any> : TopDown234Tree<K, V>() {

    /**
     * 通过旋转来得到3-结点或4-结点
     */
    private fun balanceNode(greatGrandParent: Node<K, V>?, grandParent: Node<K, V>?, parent: Node<K, V>?) {
        if (parent == null) return
        if (parent.left.isRed() && parent.right.isRed()) {
            return
        }
        var h: Node<K, V> = parent
        if (parent.right.isRed()) {
            h = rotateLeft(parent)
            if (grandParent == null) {
                root = h
            } else {
                if (grandParent.left == parent) {
                    grandParent.left = h
                } else {
                    grandParent.right = h
                }
            }
        }
        if (grandParent == null || !h.isRed()) return
        //连续两个左子结点（父结点如果是红色，则父结点一定是左子结点）
        //旋转后的结点为4-结点，只需要重新赋值，不需要翻转颜色
        h = rotateRight(grandParent)
        if (greatGrandParent == null) {
            root = h
        } else {
            if (greatGrandParent.left == grandParent) {
                greatGrandParent.left = h
            } else {
                greatGrandParent.right = h
            }
        }
    }

    override fun put(key: K, value: V) {
        if (isEmpty()) {
            root = Node(key, value, color = BLACK)
        } else {
            //因为不能使用递归，所以需要先判断是需要插入新结点还是更新结点
            val containKey = contains(key)

            var x = root!!
            var parent: Node<K, V>? = null
            var grandParent: Node<K, V>? = null
            var greatGrandParent: Node<K, V>? = null
            while (true) {
                if (x.left.isRed() && x.right.isRed()) {
                    flipColors(x)
                    balanceNode(greatGrandParent, grandParent, parent)
                }

                //通过balanceNode()方法进行旋转后可能导致parent结点变为grandParent结点的父结点，通过对比count大小确定父子关系
                //如果grandParent是parent的子结点，则greatGrandParent结点保持不变
                greatGrandParent = if (grandParent == null || size(grandParent) > size(parent)) grandParent else greatGrandParent
                grandParent = parent
                parent = x

                if (key < x.key) {
                    //旋转结点可能导致一个结点判断两次，需要先判断是否已经自增过
                    if (!containKey && x.count == size(x.left) + size(x.right) + 1) {
                        x.count++
                    }
                    if (x.left == null) {
                        x.left = Node(key, value)
                        balanceNode(greatGrandParent, grandParent, parent)
                        break
                    } else {
                        x = x.left!!
                    }
                } else if (key > x.key) {
                    if (!containKey && x.count == size(x.left) + size(x.right) + 1) {
                        x.count++
                    }
                    if (x.right == null) {
                        x.right = Node(key, value)
                        balanceNode(greatGrandParent, grandParent, parent)
                        break
                    } else {
                        x = x.right!!
                    }
                } else {
                    x.value = value
                    break
                }
            }
            root!!.color = BLACK
        }
    }
}

fun main() {
    test234Tree(SingleTopDownPass234Tree())

    val array = IntArray(50) { it }
    array.shuffle()
    val topDown234Tree = SingleTopDownPass234Tree<Int, Int>()
    array.forEach {
        topDown234Tree.put(it, 0)
    }
    drawRedBlackBST(topDown234Tree, showKey = false)
}