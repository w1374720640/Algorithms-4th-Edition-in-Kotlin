package chapter1.section3

/**
 * 单向链表
 * 通过kotlin的扩展方法为链表提供额外功能，从练习1.3.19开始
 */
class SinglyLinkedList<T> : Iterable<T> {
    open class Node<T>(var item: T, var next: Node<T>? = null)

    var first: Node<T>? = null
    var size = 0

    override fun iterator(): Iterator<T> {
        return object : Iterator<T> {
            private var node = first

            override fun hasNext() = node != null

            override fun next(): T {
                if (node == null) {
                    throw NoSuchElementException()
                }
                val result = node!!.item
                node = node!!.next
                return result
            }
        }
    }
}