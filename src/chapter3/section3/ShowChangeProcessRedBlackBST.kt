package chapter3.section3

import chapter2.sleep
import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.Stack
import edu.princeton.cs.algs4.StdDraw
import extensions.random
import extensions.shuffle

/**
 * 可以在插入、删除数据时绘制变化过程
 * 需要注意：
 * 1、原来的put、delete方法需要函数返回后才能将新结点拼接到原来的树中，无法在中途绘制完整的树结构
 *    需要将返回后的赋值操作以lambda表达式的形式传入，在执行部分操作后直接拼接到树中，参考put()方法的实现
 * 2、为防止重复绘制相同的图形，每次绘制前根据每个结点的key和color值计算hashCode，hashCode不同时才绘制图形
 */
class ShowChangeProcessRedBlackBST<K : Comparable<K>, V : Any> : RedBlackBST<K, V>() {
    var showProcess = false
    var delay = 1000L
    var lastHashCode = 0
        private set
    var showFlatView = true

    fun draw() {
        //必须和上次图形不同时才绘制绘制图形
        val hashCode = hash()
        if (showProcess && hashCode != lastHashCode) {
            StdDraw.enableDoubleBuffering()
            drawRedBlackBST(this, showFlatView = showFlatView)
            StdDraw.show()
            StdDraw.pause(delay.toInt())
            lastHashCode = hashCode
        }
    }

    fun drawText(text: String) {
        if (showProcess) {
            StdDraw.enableDoubleBuffering()
            if (showFlatView) {
                if (RedBlackBSTGraphics.canvasWidth != (RedBlackBSTGraphics.CANVAS_DEFAULT_SIZE * (2 + RedBlackBSTGraphics.FLAT_X_EXPEND_RATIO)).toInt()) {
                    StdDraw.setCanvasSize((RedBlackBSTGraphics.CANVAS_DEFAULT_SIZE * (2 + RedBlackBSTGraphics.FLAT_X_EXPEND_RATIO)).toInt(), RedBlackBSTGraphics.CANVAS_DEFAULT_SIZE)
                    StdDraw.setXscale(0.0, (2 + RedBlackBSTGraphics.FLAT_X_EXPEND_RATIO))
                    RedBlackBSTGraphics.canvasWidth = (RedBlackBSTGraphics.CANVAS_DEFAULT_SIZE * (2 + RedBlackBSTGraphics.FLAT_X_EXPEND_RATIO)).toInt()
                }
            } else {
                if (RedBlackBSTGraphics.canvasWidth != RedBlackBSTGraphics.CANVAS_DEFAULT_SIZE) {
                    StdDraw.setCanvasSize()
                    StdDraw.setXscale()
                    RedBlackBSTGraphics.canvasWidth = RedBlackBSTGraphics.CANVAS_DEFAULT_SIZE
                }
            }
            StdDraw.setPenColor()
            StdDraw.textLeft(0.02, 0.94, text)
            StdDraw.show()
            StdDraw.pause(delay.toInt())
        }
    }

    /**
     * 使用自定义方法而不是重写hashCode()方法，因为重写hashCode()方法时需要对应修改equals方法
     * 可以使用前序遍历或后序遍历，但不能使用中序遍历，因为元素相同、结构不同的二叉查找树，中序遍历结果相同
     */
    fun hash(): Int {
        if (isEmpty()) return 1
        var code = 0
        val stack = Stack<Node<K, V>>()
        stack.push(root)
        while (!stack.isEmpty) {
            val node = stack.pop()
            //使用每个结点的key和color两个参数计算hashCode，其他参数在图形上不显示，所以不参与计算
            code = ((code * 31 + node.key.hashCode()) * 31 + if (node.color) 1 else 2) * 31
            if (node.right != null) {
                stack.push(node.right)
            }
            if (node.left != null) {
                stack.push(node.left)
            }
        }
        return code
    }

    fun moveRedLeft(node: Node<K, V>, action: (Node<K, V>) -> Unit) {
        require(node.isRed() && !node.left.isRed() && !node.left?.left.isRed())
        var h = node
        flipColors(h)
        action(h)

        if (h.right?.left.isRed()) {
            h.right = rotateRight(h.right!!)
            action(h)

            h = rotateLeft(h)
            action(h)

            flipColors(h)
            action(h)
        }
    }

    fun moveRedRight(node: Node<K, V>, action: (Node<K, V>) -> Unit) {
        require(node.isRed() && !node.right.isRed() && !node.right?.left.isRed())
        var h = node
        flipColors(h)
        action(h)

        if (h.left?.left.isRed()) {
            h = rotateRight(h)
            action(h)

            flipColors(h)
            action(h)
        }
    }


    fun balance(node: Node<K, V>, action: (Node<K, V>) -> Unit) {
        var h = node
        if (h.right.isRed()) {
            h = rotateLeft(h)
            action(h)
        }

        if (h.left.isRed() && h.left?.left.isRed()) {
            h = rotateRight(h)
            action(h)
        }
        if (h.left.isRed() && h.right.isRed()) {
            flipColors(h)
            action(h)
        }
        action(h)
    }

    override fun put(key: K, value: V) {
        if (showProcess) {
            drawText("put() key=${key}")
        }
        if (root == null) {
            root = Node(key, value, color = BLACK)
            draw()
        } else {
            put(root, key, value) {
                root = it
                resize(root!!)
                draw()
            }
            root!!.color = BLACK
            draw()
        }
    }

    /**
     * 原来的put方法需要函数返回后才能将新结点拼接到原来的树中，无法在中途绘制完整的树结构
     * 将返回后的赋值操作以lambda表达式的形式传入，在执行部分操作后直接拼接到树中
     */
    fun put(node: Node<K, V>?, key: K, value: V, action: (Node<K, V>) -> Unit) {
        if (node == null) {
            action(Node(key, value))
            return
        }
        when {
            key < node.key -> put(node.left, key, value) {
                node.left = it
                resize(node)
                draw()
            }
            key > node.key -> put(node.right, key, value) {
                node.right = it
                resize(node)
                draw()
            }
            else -> {
                node.value = value
                draw()
            }
        }

        var h: Node<K, V> = node
        if (!h.left.isRed() && h.right.isRed()) {
            h = rotateLeft(h)
            action(h)
        }
        if (h.left.isRed() && h.left?.left.isRed()) {
            h = rotateRight(h)
            action(h)
        }
        if (h.left.isRed() && h.right.isRed()) {
            flipColors(h)
            action(h)
        }
        action(h)
    }

    override fun deleteMin() {
        if (isEmpty()) throw NoSuchElementException()
        if (showProcess) {
            drawText("deleteMin() min=${min()}")
        }
        if (!root?.left.isRed() && !root?.right.isRed()) {
            root!!.color = RED
            draw()
        }
        deleteMin(root!!) {
            root = it
            if (root != null) {
                resize(root!!)
            }
            draw()
        }
        if (!isEmpty()) {
            root!!.color = BLACK
            draw()
        }
    }

    fun deleteMin(node: Node<K, V>, action: (Node<K, V>?) -> Unit) {
        var h = node
        if (h.left == null) {
            action(null)
            return
        }
        if (!h.left.isRed() && !h.left?.left.isRed()) {
            moveRedLeft(h) {
                h = it
                action(h)
            }
        }
        deleteMin(h.left!!) {
            h.left = it
            resize(h)
            draw()
        }
        balance(h, action)
    }

    override fun deleteMax() {
        if (isEmpty()) throw NoSuchElementException()
        if (showProcess) {
            drawText("deleteMax() max=${max()}")
        }
        if (!root!!.left.isRed() && !root!!.right.isRed()) {
            root!!.color = RED
            draw()
        }
        deleteMax(root!!) {
            root = it
            if (root != null) {
                resize(root!!)
            }
            draw()
        }
        if (!isEmpty()) {
            root!!.color = BLACK
            draw()
        }
    }

    fun deleteMax(node: Node<K, V>, action: (Node<K, V>?) -> Unit) {
        var h = node
        if (h.left.isRed()) {
            h = rotateRight(h)
            action(h)
        }
        if (h.right == null) {
            action(null)
            return
        }
        if (!h.right.isRed() && !h.right?.left.isRed()) {
            moveRedRight(h) {
                h = it
                action(h)
            }
        }
        deleteMax(h.right!!) {
            h.right = it
            resize(h)
            draw()
        }
        balance(h, action)
    }

    override fun delete(key: K) {
        if (!contains(key)) throw NoSuchElementException()
        if (showProcess) {
            drawText("delete() key=$key")
        }
        if (!root!!.left.isRed() && !root!!.right.isRed()) {
            root!!.color = RED
            draw()
        }
        delete(root!!, key) {
            root = it
            if (root != null) {
                resize(root!!)
            }
            draw()
        }
        if (!isEmpty()) {
            root!!.color = BLACK
            draw()
        }
    }

    fun delete(node: Node<K, V>, key: K, action: (Node<K, V>?) -> Unit) {
        var h = node
        if (key < h.key) {
            if (!h.left.isRed() && !h.left?.left.isRed()) {
                moveRedLeft(h) {
                    h = it
                    action(h)
                }
            }
            delete(h.left!!, key) {
                h.left = it
                resize(h)
                draw()
            }
        } else {
            if (h.left.isRed()) {
                h = rotateRight(h)
                action(h)
            }
            if (key == h.key && h.right == null) {
                action(null)
                return
            }
            if (!h.right.isRed() && !h.right?.left.isRed()) {
                moveRedRight(h) {
                    h = it
                    action(h)
                }
            }
            if (key == h.key) {
                val rightMinNode = get(h.right!!, min(h.right!!).key)!!
                h.value = rightMinNode.value
                h.key = rightMinNode.key
                deleteMin(h.right!!) {
                    h.right = it
                    resize(h)
                    draw()
                }
            } else {
                delete(h.right!!, key) {
                    h.right = it
                    resize(h)
                    draw()
                }
            }
        }
        balance(h, action)
    }
}

fun main() {
    testOrderedST(ShowChangeProcessRedBlackBST())
    testRedBlackBST(ShowChangeProcessRedBlackBST())

    val bst = ShowChangeProcessRedBlackBST<Int, Int>()
    bst.showProcess = true
    bst.delay = 1000
    val array = IntArray(10) { it }
    array.shuffle()
    for (i in array.indices) {
        bst.put(array[i], 0)
    }
    bst.deleteMin()
    bst.deleteMax()
    while (!bst.isEmpty()) {
        val key = bst.select(random(bst.size()))
        bst.delete(key)
    }

    bst.drawText("end")
}