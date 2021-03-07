package chapter5.section2

import edu.princeton.cs.algs4.Queue
import edu.princeton.cs.algs4.Stack

/**
 * 基于三向单词查找树的符号表（非递归实现）
 */
open class TSTIterative<V : Any> : StringST<V> {

    protected inner class Node(val char: Char) {
        var value: V? = null
        var left: Node? = null
        var mid: Node? = null
        var right: Node? = null
    }

    protected var root: Node? = null
    protected var size = 0

    // 符号表应该支持以空字符串为键，空字符串不含任何字符，需要特殊处理
    protected var emptyKeyValue: V? = null

    override fun put(key: String, value: V) {
        if (key == "") {
            if (emptyKeyValue == null) size++
            emptyKeyValue = value
            return
        }
        if (root == null) {
            root = Node(key[0])
        }
        var node = root!!
        var i = 0
        while (true) {
            val char = key[i]
            if (char < node.char) {
                if (node.left == null) {
                    node.left = Node(char)
                }
                node = node.left!!
            } else if (char > node.char) {
                if (node.right == null) {
                    node.right = Node(char)
                }
                node = node.right!!
            } else {
                i++
                if (i == key.length) break
                if (node.mid == null) {
                    node.mid = Node(key[i])
                }
                node = node.mid!!
            }
        }
        if (node.value == null) size++
        node.value = value
    }

    override fun get(key: String): V? {
        if (key == "") return emptyKeyValue
        val node = getNode(key)
        return node?.value
    }

    protected open fun getNode(key: String): Node? {
        var node = root
        var i = 0
        while (node != null) {
            val char = key[i]
            if (char < node.char) {
                node = node.left
            } else if (char > node.char) {
                node = node.right
            } else {
                i++
                if (i == key.length) break
                node = node.mid
            }
        }
        return node
    }

    override fun delete(key: String) {
        if (key == "") {
            if (emptyKeyValue == null) throw NoSuchElementException()
            emptyKeyValue = null
            size--
            return
        }
        var node = root
        var i = 0
        val parents = Stack<Node>()
        val relations = Stack<Int>() // 表示父结点和子结点的关系，-1表示left，0表示mid，1表示right
        while (node != null) {
            val char = key[i]
            if (char < node.char) {
                parents.push(node)
                relations.push(-1)
                node = node.left
            } else if (char > node.char) {
                parents.push(node)
                relations.push(1)
                node = node.right
            } else {
                i++
                if (i == key.length) break
                parents.push(node)
                relations.push(0)
                node = node.mid
            }
        }
        if (node?.value == null) throw NoSuchElementException()
        size--
        node.value = null
        while (!parents.isEmpty) {
            val parent = parents.pop()
            val relation = relations.pop()
            check(node != null)
            if (node.left == null && node.right == null && node.mid == null && node.value == null) {
                when (relation) {
                    -1 -> parent.left = null
                    0 -> parent.mid = null
                    1 -> parent.right = null
                }
                node = parent
            } else break
        }
        if (root?.left == null && root?.right == null && root?.mid == null && root?.value == null) {
            root = null
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
        if (emptyKeyValue != null) {
            queue.enqueue("")
        }
        if (root == null) return queue
        keys(root!!, queue, root!!.char.toString())
        return queue
    }

    protected open fun keys(root: Node, queue: Queue<String>, rootKey: String) {
        require(!rootKey.isEmpty())
        val nodeStack = Stack<Node>()
        val keyStack = Stack<String>()
        nodeStack.push(root)
        keyStack.push(rootKey)
        while (!nodeStack.isEmpty) {
            val node = nodeStack.pop()
            val key = keyStack.pop()
            if (node.value != null) {
                queue.enqueue(key)
            }
            if (node.right != null) {
                nodeStack.push(node.right!!)
                keyStack.push(key.substring(0, key.length - 1) + node.right!!.char)
            }
            if (node.mid != null) {
                nodeStack.push(node.mid!!)
                keyStack.push(key + node.mid!!.char)
            }
            if (node.left != null) {
                nodeStack.push(node.left!!)
                keyStack.push(key.substring(0, key.length - 1) + node.left!!.char)
            }
        }
    }

    override fun longestPrefixOf(s: String): String? {
        var longestLength = 0
        var i = 0
        var node = root
        while (node != null) {
            val char = s[i]
            if (char < node.char) {
                node = node.left
            } else if (char > node.char) {
                node = node.right
            } else {
                i++
                if (node.value != null) longestLength = i
                if (i == s.length) break
                node = node.mid
            }
        }
        return if (longestLength == 0) {
            if (emptyKeyValue == null) null else ""
        } else {
            s.substring(0, longestLength)
        }
    }

    override fun keysWithPrefix(s: String): Iterable<String> {
        if (s == "") return keys()
        val queue = Queue<String>()
        val node = getNode(s)
        if (node != null) {
            if (node.value != null) {
                queue.enqueue(s)
            }
            if (node.mid != null) {
                keys(node.mid!!, queue, s + node.mid!!.char)
            }
        }
        return queue
    }

    override fun keysThatMatch(s: String): Iterable<String> {
        val queue = Queue<String>()
        if (s == "") {
            if (emptyKeyValue != null) queue.enqueue("")
            return queue
        }
        if (root == null) return queue
        val nodeStack = Stack<Node>()
        val keyStack = Stack<String>()
        nodeStack.push(root!!)
        keyStack.push(root!!.char.toString())
        while (!nodeStack.isEmpty) {
            val node = nodeStack.pop()
            val key = keyStack.pop()
            val i = key.length - 1
            val char = s[i]
            if (char == '.' || char > node.char) {
                if (node.right != null) {
                    nodeStack.push(node.right!!)
                    keyStack.push(key.substring(0, i) + node.right!!.char)
                }
            }
            if (char == '.' || char == node.char) {
                if (i == s.length - 1) {
                    if (node.value != null) {
                        queue.enqueue(key)
                    }
                } else if (node.mid != null) {
                    nodeStack.push(node.mid!!)
                    keyStack.push(key + node.mid!!.char)
                }
            }
            if (char == '.' || char < node.char) {
                if (node.left != null) {
                    nodeStack.push(node.left!!)
                    keyStack.push(key.substring(0, i) + node.left!!.char)
                }
            }
        }
        return queue
    }
}

fun main() {
    testStringST { TSTIterative() }
}