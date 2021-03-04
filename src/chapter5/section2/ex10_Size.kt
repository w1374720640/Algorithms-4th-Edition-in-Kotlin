package chapter5.section2

import chapter5.section1.Alphabet
import edu.princeton.cs.algs4.Queue

/**
 * size()方法
 * 为TrieST和TST实现最为即时的size()方法（在每个结点中保存子树中的键的总数）
 *
 * 解：参考二叉查找树的实现[chapter3.section2.BinarySearchTree]
 */
class EagerTrieST<V : Any>(private val alphabet: Alphabet) : StringST<V> {

    private inner class Node {
        val next = arrayOfNulls<Node>(alphabet.R())
        var value: V? = null
        var size = 0

        fun isEmpty() = size == 0

        fun resize() {
            var count = if (value == null) 0 else 1
            next.forEach {
                if (it != null) count += it.size
            }
            size = count
        }
    }

    private val root = Node()

    override fun put(key: String, value: V) {
        put(root, key, value, 0)
    }

    private fun put(node: Node, key: String, value: V, d: Int) {
        if (d == key.length) {
            if (node.value == null) node.size++
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
        node.resize()
    }

    override fun get(key: String): V? {
        return get(root, key, 0)?.value
    }

    private fun get(node: Node, key: String, d: Int): Node? {
        if (d == key.length) return node
        val index = alphabet.toIndex(key[d])
        val nextNode = node.next[index] ?: return null
        return get(nextNode, key, d + 1)
    }

    override fun delete(key: String) {
        delete(root, key, 0)
    }

    private fun delete(node: Node, key: String, d: Int): Node? {
        if (d == key.length) {
            if (node.value == null) throw NoSuchElementException()
            node.value = null
            node.size--
            return if (node.isEmpty()) null else node
        }
        val index = alphabet.toIndex(key[d])
        val nextNode = node.next[index] ?: throw NoSuchElementException()
        node.next[index] = delete(nextNode, key, d + 1)
        node.resize()
        return if (node.isEmpty()) null else node
    }

    override fun contains(key: String): Boolean {
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return root.isEmpty()
    }

    override fun size(): Int {
        return root.size
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
        val node = get(root, s, 0) ?: return queue
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

class EagerTST<V : Any> : StringST<V> {

    private inner class Node(val char: Char) {
        var value: V? = null
        var left: Node? = null
        var mid: Node? = null
        var right: Node? = null
        var size = 0

        fun resize() {
            var count = if (value == null) 0 else 1
            left?.let { count += it.size }
            right?.let { count += it.size }
            mid?.let { count += it.size }
            size = count
        }
    }

    private var root: Node? = null

    // 符号表应该支持以空字符串为键，空字符串不含任何字符，需要特殊处理
    private var emptyKeyValue: V? = null

    override fun put(key: String, value: V) {
        if (key == "") {
            emptyKeyValue = value
            return
        }
        root = put(root, key, value, 0)
    }

    private fun put(node: Node?, key: String, value: V, d: Int): Node {
        val char = key[d]
        val result = node ?: Node(char)
        when {
            char < result.char -> result.left = put(result.left, key, value, d)
            char > result.char -> result.right = put(result.right, key, value, d)
            else -> {
                if (d == key.length - 1) {
                    if (result.value == null) result.size++
                    result.value = value
                } else {
                    result.mid = put(result.mid, key, value, d + 1)
                }
            }
        }
        result.resize()
        return result
    }

    override fun get(key: String): V? {
        if (key == "") return emptyKeyValue
        return get(root, key, 0)?.value
    }

    private fun get(node: Node?, key: String, d: Int): Node? {
        if (node == null) return null
        val char = key[d]
        return when {
            char < node.char -> get(node.left, key, d)
            char > node.char -> get(node.right, key, d)
            else -> if (d == key.length - 1) node else get(node.mid, key, d + 1)
        }
    }

    override fun delete(key: String) {
        if (key == "") {
            if (emptyKeyValue == null) throw NoSuchElementException()
            emptyKeyValue = null
            return
        }
        root = delete(root, key, 0)
    }

    private fun delete(node: Node?, key: String, d: Int): Node? {
        if (node == null) throw NoSuchElementException()
        val char = key[d]
        when {
            char < node.char -> node.left = delete(node.left, key, d)
            char > node.char -> node.right = delete(node.right, key, d)
            else -> {
                if (d == key.length - 1) {
                    if (node.value == null) throw NoSuchElementException()
                    node.value = null
                    node.size--
                } else {
                    node.mid = delete(node.mid, key, d + 1)
                }
                if (node.left == null && node.right == null && node.mid == null && node.value == null) {
                    return null
                }
            }
        }
        node.resize()
        return node
    }

    override fun contains(key: String): Boolean {
        return get(key) != null
    }

    override fun isEmpty(): Boolean {
        return size() == 0
    }

    override fun size(): Int {
        var size = if (emptyKeyValue == null) 0 else 1
        root?.let { size += it.size }
        return size
    }

    override fun keys(): Iterable<String> {
        val queue = Queue<String>()
        if (emptyKeyValue != null) {
            queue.enqueue("")
        }
        keys(root, queue, "")
        return queue
    }

    private fun keys(node: Node?, queue: Queue<String>, key: String) {
        if (node == null) return
        keys(node.left, queue, key)
        if (node.value != null) {
            queue.enqueue(key + node.char)
        }
        keys(node.mid, queue, key + node.char)
        keys(node.right, queue, key)
    }

    override fun longestPrefixOf(s: String): String? {
        val length = longestPrefixOf(root, s, 0, 0)
        return if (length == 0) {
            if (emptyKeyValue == null) null else ""
        } else {
            s.substring(0, length)
        }
    }

    private fun longestPrefixOf(node: Node?, key: String, d: Int, length: Int): Int {
        if (node == null || d == key.length) return length
        var newLength = length
        val char = key[d]
        when {
            char < node.char -> newLength = longestPrefixOf(node.left, key, d, newLength)
            char > node.char -> newLength = longestPrefixOf(node.right, key, d, newLength)
            else -> {
                if (node.value != null) {
                    newLength = d + 1
                }
                newLength = longestPrefixOf(node.mid, key, d + 1, newLength)
            }
        }
        return newLength
    }

    override fun keysWithPrefix(s: String): Iterable<String> {
        if (s == "") return keys()
        val queue = Queue<String>()
        keysWithPrefix(root, queue, s, 0)
        return queue
    }

    private fun keysWithPrefix(node: Node?, queue: Queue<String>, key: String, d: Int) {
        if (node == null) return
        if (d == key.length) {
            keys(node, queue, key)
            return
        }
        val char = key[d]
        when {
            char < node.char -> keysWithPrefix(node.left, queue, key, d)
            char > node.char -> keysWithPrefix(node.right, queue, key, d)
            else -> keysWithPrefix(node.mid, queue, key, d + 1)
        }
    }


    override fun keysThatMatch(s: String): Iterable<String> {
        val queue = Queue<String>()
        if (s == "") {
            if (emptyKeyValue != null) queue.enqueue("")
            return queue
        }
        keysThatMatch(root, queue, s, "")
        return queue
    }

    private fun keysThatMatch(node: Node?, queue: Queue<String>, match: String, real: String) {
        if (node == null) return
        val d = real.length
        val char = match[d]
        when {
            char == '.' -> {
                keysThatMatch(node.left, queue, match, real)
                if (d == match.length - 1) {
                    if (node.value != null) queue.enqueue(real + node.char)
                } else {
                    keysThatMatch(node.mid, queue, match, real + node.char)
                }
                keysThatMatch(node.right, queue, match, real)
            }
            char < node.char -> keysThatMatch(node.left, queue, match, real)
            char > node.char -> keysThatMatch(node.right, queue, match, real)
            else -> {
                if (d == match.length - 1) {
                    if (node.value != null) queue.enqueue(real + node.char)
                } else {
                    keysThatMatch(node.mid, queue, match, real + node.char)
                }
            }
        }
    }
}


fun main() {
    testStringST { EagerTrieST(Alphabet.EXTENDED_ASCII) }
    testStringST { EagerTST() }
}