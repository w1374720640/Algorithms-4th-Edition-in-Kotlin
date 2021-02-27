package chapter5.section2

import chapter5.section1.Alphabet
import edu.princeton.cs.algs4.Queue

/**
 * 基于单词查找树的符号表
 */
class TrieST<V : Any>(private val alphabet: Alphabet) : StringST<V> {

    private inner class Node {
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

    private val root = Node()
    private var size = 0

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
        keys(root, queue, "")
        return queue
    }

    private fun keys(node: Node, queue: Queue<String>, key: String) {
        if (node.value != null) {
            queue.enqueue(key)
        }
        for (i in node.next.indices) {
            val nextNode = node.next[i] ?: continue
            keys(nextNode, queue, key + alphabet.toChar(i))
        }
    }

    override fun longestPrefixOf(s: String): String? {
        val length = longestPrefixOf(root, s, 0, 0)
        return if (length == 0) null else s.substring(0, length)
    }

    private fun longestPrefixOf(node: Node, s: String, d: Int, length: Int): Int {
        val index = alphabet.toIndex(s[d])
        val nextNode = node.next[index] ?: return length
        var newLength = length
        if (nextNode.value != null) {
            newLength = d + 1
        }
        return if (d == s.length - 1) newLength else longestPrefixOf(nextNode, s, d + 1, newLength)
    }

    override fun keysWithPrefix(s: String): Iterable<String> {
        val queue = Queue<String>()
        var node = root
        for (i in s.indices) {
            val index = alphabet.toIndex(s[i])
            val nextNode = node.next[index] ?: return queue
            node = nextNode
        }
        keys(node, queue, s)
        return queue
    }

    override fun keysThatMatch(s: String): Iterable<String> {
        val queue = Queue<String>()
        keysThatMatch(root, queue, s, "")
        return queue
    }

    private fun keysThatMatch(node: Node, queue: Queue<String>, match: String, real: String) {
        val d = real.length
        if (d == match.length) {
            if (node.value != null) {
                queue.enqueue(real)
            }
            return
        }
        val char = match[d]
        if (char == '.') {
            for (i in node.next.indices) {
                val nextNode = node.next[i] ?: continue
                keysThatMatch(nextNode, queue, match, real + alphabet.toChar(i))
            }
        } else {
            val index = alphabet.toIndex(char)
            val nextNode = node.next[index] ?: return
            keysThatMatch(nextNode, queue, match, real + char)
        }
    }
}

fun main() {
    testStringST { TrieST(Alphabet.EXTENDED_ASCII) }
}