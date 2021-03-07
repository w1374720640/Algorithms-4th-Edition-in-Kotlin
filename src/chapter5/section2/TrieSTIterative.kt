package chapter5.section2

import chapter5.section1.Alphabet
import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack

/**
 * 基于单词查找树的符号表（非递归实现）
 */
open class TrieSTIterative<V : Any>(protected val alphabet: Alphabet) : StringST<V> {

    protected inner class Node {
        val next = arrayOfNulls<Node>(alphabet.R())
        var value: V? = null

        fun nextNum(): Int {
            var i = 0
            next.forEach {
                if (it != null) i++
            }
            return i
        }
    }

    protected val root = Node()
    protected var size = 0

    override fun put(key: String, value: V) {
        var node = root
        for (i in key.indices) {
            val index = alphabet.toIndex(key[i])
            var nextNode = node.next[index]
            if (nextNode == null) {
                nextNode = Node()
                node.next[index] = nextNode
            }
            node = nextNode
        }
        if (node.value == null) size++
        node.value = value
    }

    override fun get(key: String): V? {
        var node = root
        for (i in key.indices) {
            val index = alphabet.toIndex(key[i])
            val nextNode = node.next[index] ?: return null
            node = nextNode
        }
        return node.value
    }

    override fun delete(key: String) {
        val parents = arrayOfNulls<Node>(key.length)
        var node = root
        for (i in key.indices) {
            parents[i] = node
            val index = alphabet.toIndex(key[i])
            val nextNode = node.next[index] ?: throw NoSuchElementException()
            node = nextNode
        }
        if (node.value == null) throw NoSuchElementException()
        node.value = null
        size--
        for (i in parents.size - 1 downTo 0) {
            if (node.value == null && node.nextNum() == 0) {
                val index = alphabet.toIndex(key[i])
                parents[i]!!.next[index] = null
                node = parents[i]!!
            } else {
                break
            }
        }
    }

    override fun contains(key: String): Boolean {
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun size(): Int {
        return size
    }

    override fun keys(): Iterable<String> {
        val queue = Queue<String>()
        // 树的非递归前序遍历
        val nodeStack = Stack<Node>()
        val keyStack = Stack<String>()
        nodeStack.push(root)
        keyStack.push("")
        while (!nodeStack.isEmpty) {
            val node = nodeStack.pop()
            val key = keyStack.pop()
            if (node.value != null) {
                queue.enqueue(key)
            }
            for (i in alphabet.R() - 1 downTo 0) {
                if (node.next[i] != null) {
                    nodeStack.push(node.next[i])
                    keyStack.push(key + alphabet.toChar(i))
                }
            }
        }
        return queue
    }

    override fun longestPrefixOf(s: String): String? {
        var node = root
        var longestKey: String? = if (node.value == null) null else ""
        for (i in s.indices) {
            val index = alphabet.toIndex(s[i])
            val nextNode = node.next[index] ?: break
            if (nextNode.value != null) {
                longestKey = s.substring(0, i + 1)
            }
            node = nextNode
        }
        return longestKey
    }

    override fun keysWithPrefix(s: String): Iterable<String> {
        val queue = Queue<String>()

        // get()方法中的代码
        var node = root
        for (i in s.indices) {
            val index = alphabet.toIndex(s[i])
            val nextNode = node.next[index] ?: return queue
            node = nextNode
        }

        // keys()方法中的代码，只是初始值不同
        val nodeStack = Stack<Node>()
        val keyStack = Stack<String>()
        nodeStack.push(node) // 这里从node结点开始遍历
        keyStack.push(s) // 这里起始值是s
        while (!nodeStack.isEmpty) {
            val topNode = nodeStack.pop()
            val key = keyStack.pop()
            if (topNode.value != null) {
                queue.enqueue(key)
            }
            for (i in alphabet.R() - 1 downTo 0) {
                if (topNode.next[i] != null) {
                    nodeStack.push(topNode.next[i])
                    keyStack.push(key + alphabet.toChar(i))
                }
            }
        }
        return queue
    }

    override fun keysThatMatch(s: String): Iterable<String> {
        val queue = Queue<String>()
        // 树的广度优先遍历（遍历时筛选）
        val nodeQueue = Queue<Node>()
        val keyQueue = Queue<String>()
        nodeQueue.enqueue(root)
        keyQueue.enqueue("")
        for (char in s) {
            // count为树每层符合条件的结点数
            val count = nodeQueue.size()
            // 遍历过程中会继续向nodeQueue中添加数据，不能用!nodeQueue.isEmpty()判断
            repeat(count) {
                val node = nodeQueue.dequeue()
                val key = keyQueue.dequeue()
                if (char == '.') {
                    for (j in 0 until alphabet.R()) {
                        val nextNode = node.next[j] ?: continue
                        nodeQueue.enqueue(nextNode)
                        keyQueue.enqueue(key + alphabet.toChar(j))
                    }
                } else {
                    val nextNode = node.next[alphabet.toIndex(char)]
                    if (nextNode != null) {
                        nodeQueue.enqueue(nextNode)
                        keyQueue.enqueue(key + char)
                    }
                }
            }
        }
        while (!nodeQueue.isEmpty) {
            val node = nodeQueue.dequeue()
            val key = keyQueue.dequeue()
            if (node.value != null) {
                queue.enqueue(key)
            }
        }
        return queue
    }
}

fun main() {
    testStringST { TrieSTIterative(Alphabet.EXTENDED_ASCII) }
}