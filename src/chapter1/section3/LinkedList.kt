package chapter1.section3

/**
 * 单向链表结点
 */
open class Node<T>(var item: T, var next: Node<T>? = null)

/**
 * 双向链表结点
 */
open class DoubleNode<T>(var item: T, var previous: DoubleNode<T>? = null, var next: DoubleNode<T>? = null)

/**
 * 单向链表
 * 通过kotlin的扩展方法为链表提供额外功能，从练习1.3.19开始
 */
class SinglyLinkedList<T> {
    var first: Node<T>? = null
}

/**
 * 双向链表
 * 在练习1.3.31中有完整的实现（通过扩展方法实现）
 */
class DoublyLinkedList<T> {
    var first: DoubleNode<T>? = null
    var last: DoubleNode<T>? = null
}