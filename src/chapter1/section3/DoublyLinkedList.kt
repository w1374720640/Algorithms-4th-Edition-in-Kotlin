package chapter1.section3

/**
 * 双向链表
 * 在练习1.3.31中有完整的实现（通过扩展方法实现）
 */
class DoublyLinkedList<T>: Iterable<T> {
    open class Node<T>(var item: T, var previous: Node<T>? = null, var next: Node<T>? = null)

    var first: Node<T>? = null
    var last: Node<T>? = null
    var size = 0

    override fun iterator(): Iterator<T> {
        return forwardIterator()
    }
}