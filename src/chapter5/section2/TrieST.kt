package chapter5.section2

import chapter5.section1.Alphabet
import edu.princeton.cs.algs4.Queue

/**
 * 基于单词查找树的符号表
 */
open class TrieST<V : Any>(protected val alphabet: Alphabet) : StringST<V> {

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

    // 根结点不可重新赋值，用于存放空字符串对应的值
    protected val root = Node()
    protected var size = 0

    override fun put(key: String, value: V) {
        put(root, key, value, 0)
    }

    protected open fun put(node: Node, key: String, value: V, d: Int) {
        if (d == key.length) {
            if (node.value == null) size++
            node.value = value
            return
        }
        val index = alphabet.toIndex(key[d])
        var nextNode = node.next[index]
        if (nextNode == null) {
            nextNode = Node()
            node.next[index] = nextNode
        }
        put(nextNode, key, value, d + 1)
    }

    override fun get(key: String): V? {
        return get(root, key, 0)?.value
    }

    protected open fun get(node: Node, key: String, d: Int): Node? {
        if (d == key.length) return node
        val index = alphabet.toIndex(key[d])
        val nextNode = node.next[index] ?: return null
        return get(nextNode, key, d + 1)
    }

    override fun delete(key: String) {
        delete(root, key, 0)
    }

    protected open fun delete(node: Node, key: String, d: Int): Node? {
        if (d == key.length) {
            if (node.value == null) throw NoSuchElementException()
            node.value = null
            size--
            return if (node.nextNum() == 0) null else node
        }
        val index = alphabet.toIndex(key[d])
        val nextNode = node.next[index] ?: throw NoSuchElementException()
        node.next[index] = delete(nextNode, key, d + 1)
        return if (node.value == null && node.nextNum() == 0) null else node
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

    protected open fun keys(node: Node, queue: Queue<String>, key: String) {
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

    protected open fun longestPrefixOf(node: Node, s: String, d: Int, length: Int): Int {
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
        val node = get(root, s, 0) ?: return queue
        keys(node, queue, s)
        return queue
    }

    override fun keysThatMatch(s: String): Iterable<String> {
        val queue = Queue<String>()
        keysThatMatch(root, queue, s, "")
        return queue
    }

    protected open fun keysThatMatch(node: Node, queue: Queue<String>, match: String, real: String) {
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