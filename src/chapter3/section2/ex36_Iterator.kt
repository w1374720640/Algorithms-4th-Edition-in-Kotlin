package chapter3.section2

import chapter3.section1.testOrderedST
import edu.princeton.cs.algs4.Stack
import extensions.setSeed
import extensions.shuffle

/**
 * 迭代器
 * 能否实现一个非递归版本的keys()方法，其使用的额外空间和树的高度成正比（和查找范围内的键的多少无关）？
 *
 * 解：使用二叉树的非递归中序遍历实现
 */
class NoRecursionKeysBinaryTreeST<K : Comparable<K>, V : Any> : BinaryTreeST<K, V>() {
    override fun keys(low: K, high: K): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    val stack = Stack<Node<K, V>>()

                    init {
                        if (root != null) {
                            addAllLeftNode(root!!)
                        }
                    }

                    private fun addAllLeftNode(root: Node<K, V>) {
                        when {
                            root.key < low -> {
                                var node = root.right
                                while (node != null) {
                                    when {
                                        node.key < low -> node = node.right
                                        node.key == low -> {
                                            stack.push(node)
                                            node = null
                                        }
                                        else -> {
                                            if (node.key in low..high) {
                                                stack.push(node)
                                            }
                                            node = node.left
                                        }
                                    }
                                }
                            }
                            root.key == low -> stack.push(root)
                            else -> {
                                var node: Node<K, V>? = root
                                while (node != null) {
                                    if (node.key in low..high) {
                                        stack.push(node)
                                    }
                                    node = node.left
                                }
                            }
                        }
                    }

                    override fun hasNext(): Boolean {
                        return !stack.isEmpty
                    }

                    override fun next(): K {
                        if (stack.isEmpty) throw NoSuchElementException()
                        val node = stack.pop()
                        if (node.right != null) {
                            addAllLeftNode(node.right!!)
                        }
                        return node.key
                    }

                }
            }

        }
    }

    override fun keys(): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    val stack = Stack<Node<K, V>>()

                    init {
                        if (root != null) {
                            addAllLeftNode(root!!)
                        }
                    }

                    private fun addAllLeftNode(root: Node<K, V>) {
                        var node: Node<K, V>? = root
                        while (node != null) {
                            stack.push(node)
                            node = node.left
                        }
                    }

                    override fun hasNext(): Boolean {
                        return !stack.isEmpty
                    }

                    override fun next(): K {
                        if (stack.isEmpty) throw NoSuchElementException()
                        val node = stack.pop()
                        if (node.right != null) {
                            addAllLeftNode(node.right!!)
                        }
                        return node.key
                    }

                }
            }
        }
    }
}

fun main() {
    testOrderedST(NoRecursionKeysBinaryTreeST())

    setSeed(10)
    val size = 10
    val array = Array(size) { it }
    array.shuffle()
    val st = NoRecursionKeysBinaryTreeST<Int, Int>()
    array.forEach { st.put(it, 0) }
    println(st.keys(4, 22).joinToString())
    drawBinaryTree(st)
}