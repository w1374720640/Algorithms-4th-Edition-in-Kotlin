package chapter3.section1

/**
 * 使用有序链表实现的有序符号表
 */
class OrderedSequentialSearchST<K : Comparable<K>, V : Any> : OrderedST<K, V> {
    class Node<K : Comparable<K>, V : Any>(val key: K, var value: V, var next: Node<K, V>? = null)

    private var first: Node<K, V>? = null
    private var size = 0

    override fun min(): K {
        if (isEmpty()) throw NoSuchElementException()
        return first!!.key
    }

    override fun max(): K {
        if (isEmpty()) throw NoSuchElementException()
        var node = first
        while (node?.next != null) {
            node = node.next
        }
        return node!!.key
    }

    override fun floor(key: K): K {
        if (first == null || first!!.key > key) throw NoSuchElementException()
        var node = first!!
        while (node.next != null && node.next!!.key <= key) {
            node = node.next!!
        }
        return node.key
    }

    override fun ceiling(key: K): K {
        if (first == null) throw NoSuchElementException()
        var node = first
        while (node != null && node.key < key) {
            node = node.next
        }
        if (node == null) {
            throw NoSuchElementException()
        } else {
            return node.key
        }
    }

    override fun rank(key: K): Int {
        var count = 0
        var node = first
        while (node != null && node.key < key) {
            node = node.next
            count++
        }
        return count
    }

    override fun select(k: Int): K {
        if (k < 0 || k >= size) throw NoSuchElementException()
        var index = 0
        var node = first
        while (node != null) {
            if (k == index) {
                return node.key
            }
            index++
            node = node.next
        }
        throw NoSuchElementException()
    }

    override fun deleteMin() {
        if (first == null) throw NoSuchElementException()
        first = first!!.next
        size--
    }

    override fun deleteMax() {
        if (first == null) throw NoSuchElementException()
        var node = first!!
        if (node.next == null) {
            first = null
            size = 0
            return
        }
        var nextNode = node.next!!
        var nextNextNode = nextNode.next
        while (nextNextNode != null) {
            node = nextNode
            nextNode = nextNextNode
            nextNextNode = nextNextNode.next
        }
        node.next = null
        size--
    }

    override fun size(low: K, high: K): Int {
        if (first == null || low > high) return 0
        var count = 0
        var node = first
        while (node != null) {
            if (node.key > high) break
            if (node.key >= low && node.key <= high) {
                count++
            }
            node = node.next
        }
        return count
    }

    override fun put(key: K, value: V) {
        if (first == null) {
            first = Node(key, value)
            size = 1
            return
        }
        var node = first!!
        if (node.key == key) {
            node.value = value
            return
        }
        if (node.key > key) {
            first = Node(key, value, first)
            size++
            return
        }
        var nextNode = node.next
        // node.key < key
        while (nextNode != null) {
            if (nextNode.key == key) {
                nextNode.value = value
                return
            }
            if (nextNode.key > key) {
                node.next = Node(key, value, nextNode)
                size++
                return
            }
            node = nextNode
            nextNode = nextNode.next
        }
        //所有元素都比key小
        node.next = Node(key, value)
        size++
    }

    override fun get(key: K): V? {
        var node = first
        while (node != null) {
            if (node.key == key) {
                return node.value
            }
            node = node.next
        }
        return null
    }

    override fun delete(key: K) {
        if (first == null) throw NoSuchElementException()
        if (first!!.key == key) {
            first = first!!.next
            size--
            return
        }
        var node = first!!
        var nextNode = node.next
        while (nextNode != null) {
            if (nextNode.key == key) {
                node.next = nextNode.next
                size--
                return
            }
            if (nextNode.key > key) throw NoSuchElementException()
            node = nextNode
            nextNode = nextNode.next
        }
        throw NoSuchElementException()
    }

    override fun isEmpty(): Boolean {
        return first == null
    }

    override fun size(): Int {
        return size
    }

    override fun keys(low: K, high: K): Iterable<K> {
        require(low <= high)
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    var node = first

                    //将node赋值为第一个大于等于low的值
                    init {
                        if (node != null) {
                            if (node!!.key > high) {
                                node = null
                            } else {
                                while (node != null) {
                                    if (node!!.key >= low) break
                                    node = node!!.next
                                }
                            }
                        }
                    }

                    override fun hasNext(): Boolean {
                        return node != null && node!!.key <= high
                    }

                    override fun next(): K {
                        if (node == null || node!!.key > high) throw NoSuchElementException()
                        val key = node!!.key
                        node = node!!.next
                        return key
                    }

                }
            }

        }
    }

    override fun contains(key: K): Boolean {
        var node = first
        while (node != null) {
            if (node.key == key) {
                return true
            }
            node = node.next
        }
        return false
    }

    override fun keys(): Iterable<K> {
        return object : Iterable<K> {
            override fun iterator(): Iterator<K> {
                return object : Iterator<K> {
                    var node = first

                    override fun hasNext(): Boolean {
                        return node != null
                    }

                    override fun next(): K {
                        if (node == null) throw NoSuchElementException()
                        val key = node!!.key
                        node = node!!.next
                        return key
                    }

                }
            }
        }
    }
}

fun main() {
    testOrderedST(OrderedSequentialSearchST())
}