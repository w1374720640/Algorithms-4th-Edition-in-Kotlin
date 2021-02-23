package chapter5.section1

import chapter1.section3.*
import chapter2.section2.addTailNode
import chapter2.section2.shuffleLinkedList
import chapter4.section3.addListTail

/**
 * 链表排序
 * 编写一个排序算法，接受一条以String为键值参数的结点链表并重新按顺序排列所有结点
 * （返回一个指向键值最小的结点的指针）。
 * 使用三向字符串快速排序。
 *
 * 解：创建三个新的链表，分别存放小于基准的结点、等于基准的结点、大于基准的结点
 * 随机打乱链表（参考练习2.2.18），取一个基准值，遍历链表，将所有结点分别加入三个链表中，
 * 对三个子链表递归上述操作，当链表大小为小于等于1时，重新递归拼接链表
 */
fun ex16_LinkedListSort(list: DoublyLinkedList<String>): DoublyLinkedList<String> {
    if (list.isEmpty()) return list
    // 随机打乱链表，时间复杂度O(NlgN)，空间复杂度O(lgN)，参考练习2.2.18
    list.shuffleLinkedList()
    return ex16_LinkedListSort(list, 0)
}

private fun ex16_LinkedListSort(list: DoublyLinkedList<String>, d: Int): DoublyLinkedList<String> {
    if (list.isEmpty()) return list
    val smallList = DoublyLinkedList<String>()
    val equalList = DoublyLinkedList<String>()
    val largeList = DoublyLinkedList<String>()

    val base = list.first!!
    var node = list.first
    while (node != null) {
        val result = compare(node, base, d)
        // 在将结点加入新链表前保存下一个结点的值
        val next = node.next
        when {
            result > 0 -> largeList.addTailNode(node)
            result < 0 -> smallList.addTailNode(node)
            else -> equalList.addTailNode(node)
        }
        node = next
    }

    val smallResultList = ex16_LinkedListSort(smallList, d)
    val equalResultList = if (base.item.length > d) {
        ex16_LinkedListSort(equalList, d + 1)
    } else {
        equalList
    }
    val largeResultList = ex16_LinkedListSort(largeList, d)

    smallResultList.addListTail(equalResultList)
    smallResultList.addListTail(largeResultList)
    return smallResultList
}

private fun compare(node1: DoubleNode<String>, node2: DoubleNode<String>, d: Int): Int {
    return when {
        d >= node1.item.length && d >= node2.item.length -> 0
        d >= node1.item.length -> -1
        d >= node2.item.length -> 1
        else -> node1.item[d].compareTo(node2.item[d])
    }
}

fun main() {
    val array = getMSDData()
    val list = DoublyLinkedList<String>()
    array.forEach { list.addTail(it) }
    val result = ex16_LinkedListSort(list)
    val stringBuilder = StringBuilder()
    result.forwardIterator().forEach {
        stringBuilder.append(it)
                .append("\n")
    }
    println(stringBuilder)
}