package chapter1.section3

import extensions.inputPrompt
import extensions.readAllStrings

/**
 * 用环形链表实现Queue。
 * 环形链表也是一条链表，只是没有任何结点的链接为空，且只要链表非空则last.next的值为first。
 * 只能使用一个Node类型的实例变量（last）。
 */
class RingLinkedQueue<T> {
    private var last: SinglyLinkedList.Node<T>? = null
    var size = 0
        private set

    fun enqueue(value: T) {
        if (last == null) {
            last = SinglyLinkedList.Node(value)
            last?.next = last
        } else {
            val first = last?.next
            last?.next = SinglyLinkedList.Node(value)
            last = last?.next
            last?.next = first
        }
        size++
    }

    fun dequeue(): T {
        if (last == null) throw NoSuchElementException()
        val value = last?.next?.item
        if (last == last?.next) {
            last = null
        } else {
            last?.next = last?.next?.next
        }
        size--
        return value!!
    }

    fun peek(): T {
        if (last == null) throw NoSuchElementException()
        return last?.next?.item!!
    }

    fun isEmpty() = last == null

    fun joinToString(): String {
        var result = ""
        if (last == null) return result
        var node = last?.next
        do {
            result += node?.item.toString() + " "
            node = node?.next
        } while (node != last?.next)
        return result
    }
}

fun main() {
    inputPrompt()
    val array = readAllStrings()
    val list = RingLinkedQueue<String>()
    array.forEach { list.enqueue(it) }
    println(list.joinToString())
    while (!list.isEmpty()) {
        list.dequeue()
        println(list.joinToString())
    }
}