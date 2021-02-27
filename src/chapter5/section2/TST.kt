package chapter5.section2

import edu.princeton.cs.algs4.Queue

/**
 * 基于三向单词查找树的符号表
 */
class TST<V : Any> : StringST<V> {

    private inner class Node(val char: Char) {
        var value: V? = null
        var left: Node? = null
        var mid: Node? = null
        var right: Node? = null
    }

    private var root: Node? = null
    private var size = 0

    // 符号表应该支持以空字符串为键，空字符串不含任何字符，需要特殊处理
    private var emptyKeyValue: V? = null

    override fun put(key: String, value: V) {
        if (key == "") {
            if (emptyKeyValue == null) size++
            emptyKeyValue = value
            return
        }
        root = put(root, key, value, 0)
    }

    private fun put(node: Node?, key: String, value: V, d: Int): Node? {
        val char = key[d]
        val result = node ?: Node(char)
        when {
            char < result.char -> result.left = put(result.left, key, value, d)
            char > result.char -> result.right = put(result.right, key, value, d)
            else -> {
                if (d == key.length - 1) {
                    if (result.value == null) size++
                    result.value = value
                } else {
                    result.mid = put(result.mid, key, value, d + 1)
                }
            }
        }
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
            size--
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
                    size--
                } else {
                    node.mid = delete(node.mid, key, d + 1)
                }
                if (node.left == null && node.right == null && node.mid == null && node.value == null) {
                    return null
                }
            }
        }
        return node
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
    testStringST { TST() }
}